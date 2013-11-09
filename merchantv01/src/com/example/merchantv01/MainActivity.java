package com.example.merchantv01;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.example.merchantv01.R;
import com.example.merchantv01.R.color;

import Drawer.DrawerItemAdapter;
import Drawer.DrawerModelAdapter;
import Notifications.NotificationItemAdapter;
import Notifications.NotificationModelAdapter;
import Transactions.Transactions;
import Transactions.TransactionsItemAdapter;
import Transactions.TransactionsModelAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	public String serverUrl = "http://192.168.1.96:50364";
	public  String APIUrl = "api/transaction?MerchantID=141";
	public String stringUrl = serverUrl + "/" + APIUrl;
	public String postUrl = serverUrl + "/" + "api/transaction";
	public TextView displayText;
	public static List<Entry> listToDisplay;
	public Activity currentActvity;
	public static ListView listViewToDisplay;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static ListView notificationDisplayList;
	public static Button submit = null;
	public static TextView salesAmount = null;
	public static TextView cardNumber = null;
	public static TextView referenceNumber = null;
	public Runnable mPendingRunnable;
	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// convert java object to JSON format,
		// and returned as JSON formatted string

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.action_bar);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.menu_item);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// mDrawerList.setBackgroundColor(color.Red);
		// mDrawerLayout
		mDrawerLayout.setDrawerShadow(color.DarkGray, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		String[] mMenuItem = getResources().getStringArray(R.array.menu_item);
		String[] mMenuItemFiles = getResources().getStringArray(
				R.array.menu_item_image_name);

		DrawerModelAdapter.LoadModel(mMenuItem, mMenuItemFiles);
		String[] ids = new String[DrawerModelAdapter.Items.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.toString(i + 1);
		}

		DrawerItemAdapter adapter = new DrawerItemAdapter(this,
				R.layout.drawer_list_item, ids);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_icon, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
				final Handler mHandler = new Handler();
				if (mPendingRunnable != null) {
					mHandler.post(mPendingRunnable);
					mPendingRunnable = null;
				}

				progress = ProgressDialog.show(MainActivity.this,
						"Downloading Data",
						"Please wait while Data are downloaded");
				progress.setCancelable(false);
				progress.isIndeterminate();
				progress.show();

			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		currentActvity = this;
		// code for testing HTTP / XML fetch

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */

		notificationDisplayList = (ListView) findViewById(R.id.notificationList);
		String testname[] = new String[] { "DouMing Mok", "Alan chan",
				"Fiona Wong", "Kenji McNabb", "Jessica Dekoning" };
		String testImageFiles[] = new String[] { "record_user_icon",
				"record_user_icon", "record_user_icon", "record_user_icon",
				"record_user_icon" };
		Date d = new Date();
		String stringNow = d.toString();
		String testTime[] = new String[] { stringNow, stringNow, stringNow,
				stringNow, stringNow };
		NotificationModelAdapter.LoadModel(testname, testImageFiles, testTime,
				"record_checkins_y_yes__icon", "record_checkins_x_no__icon");
		String[] NotificationIds = new String[NotificationModelAdapter.Items
				.size()];
		for (int i = 0; i < NotificationIds.length; i++) {
			NotificationIds[i] = Integer.toString(i + 1);
		}
		NotificationItemAdapter Adapter = new NotificationItemAdapter(this,
				R.layout.notification_list, NotificationIds);
		notificationDisplayList.setAdapter(Adapter);
		changeFragment(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setQueryHint(getString(R.string.query_hint));
		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		// Getting the 'search_plate' LinearLayout.
		View searchPlate = searchView.findViewById(searchPlateId);
		searchPlate.setBackgroundResource(R.drawable.searchbar);

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.search:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(final int position) {
		mPendingRunnable = new Runnable() {
			@Override
			public void run() {
				// update the main content by replacing fragments
				changeFragment(position);
			}
		};
		// update selected item and title, then close the drawer

		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void changeFragment(final int position) {
		Fragment fragment = new PageFragment(MainActivity.this);
		Bundle args = new Bundle();
		args.putInt(PageFragment.ARG_PAGE_NUMBER, position);
		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	

	// private classes

	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */
				break;
			}
		}
	}

	
	public void webApiPost(String url, String json)
			throws InterruptedException, ExecutionException {
		String para[] = { url, json };
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			String result = new HttpPostTask(this).execute(para).get();
			Toast toast = null;
			String text = "";
			int duration = Toast.LENGTH_SHORT;
			if (result.equals("Created")) {
				text = "Record Added";
			} else {
				text = "Record Can Not be added";
			}
			toast = Toast.makeText(this, text, duration);
			toast.show();
		} else {
			displayText.setText("No network connection available.");
		}
	}

	public void webApiGet(String url) throws InterruptedException,
			ExecutionException {
		String para[] = { url };
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			this.listToDisplay = new HttpRequestTask(this).execute(para).get();
		} else {
			displayText.setText("No network connection available.");
		}
	}

}