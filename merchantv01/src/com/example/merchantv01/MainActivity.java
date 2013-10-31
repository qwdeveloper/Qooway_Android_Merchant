package com.example.merchantv01;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.example.merchantv01.R;
import com.example.merchantv01.R.color;

import Drawer.DrawerItemAdapter;
import Drawer.DrawerModelAdapter;
import Notifications.NotificationItemAdapter;
import Notifications.NotificationModelAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity  implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	private String serverUrl = "http://192.168.1.96:50364";
	private String APIUrl = "api/merchant";
	private String stringUrl = serverUrl + "/" + APIUrl;
	private TextView displayText;
	private static List<Entry> listToDisplay ;
	private Activity currentActvity;
	private static ListView listViewToDisplay ;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static Location mCurrentLocation;
    private static LocationClient mLocationClient ;
    private static ListView notificationDisplayList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(stringUrl);
		} else {
			displayText.setText("No network connection available.");
		}
	       /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        
		  notificationDisplayList = (ListView) findViewById(R.id.notificationList);
	        String testname[] = new String[]{"DouMing Mok" , "Alan chan" , "Fiona Wong" ,"Kenji McNabb" ,"Jessica Dekoning"};
	        String testImageFiles[] = new String[]{"record_user_icon" , "record_user_icon" , "record_user_icon" ,"record_user_icon" ,"record_user_icon"};
	        Date d = new Date();
	        String stringNow =d.toString();
	        String testTime[] =new String[] { stringNow,stringNow,stringNow,stringNow,stringNow};
			NotificationModelAdapter.LoadModel(testname,testImageFiles,testTime , "record_checkins_y_yes__icon" , "record_checkins_x_no__icon" );
			String[] NotificationIds = new String[NotificationModelAdapter.Items.size()];
			for (int i = 0; i < NotificationIds.length; i++) {
				NotificationIds[i] = Integer.toString(i + 1);
			}
			NotificationItemAdapter Adapter = new NotificationItemAdapter(this, R.layout.notification_list, NotificationIds);
			notificationDisplayList.setAdapter(Adapter);
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

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PageFragment();
		Bundle args = new Bundle();
		args.putInt(PageFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
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
	private class DownloadWebpageTask extends
			AsyncTask<String, Void, List<Entry>> {

		@SuppressWarnings("finally")
		@Override
		protected List<Entry> doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			List<Entry> result=null;
			try {
				result= loadXml(urls[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(List<Entry> result) {
			listToDisplay = result;

		}

		@SuppressWarnings("unchecked")
		private List<Entry> loadXml(String urlString)
				throws XmlPullParserException, IOException {
			InputStream stream = null;
			List<Entry> entries = null;
			FeedParser parser = new FeedParser();

			try {
				stream = downloadUrl(urlString);			
				entries = parser.parse(stream, currentActvity);
				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
				if (stream != null) {
					stream.close();
				}
			
			return entries;

		}

		private InputStream downloadUrl(String urlString) throws IOException {
		    URL url = new URL(urlString);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(10000 /* milliseconds */);
		    conn.setConnectTimeout(15000 /* milliseconds */);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    conn.setRequestProperty("Accept", "text/xml");
		    // Starts the query
		    conn.connect();
		    
		    return conn.getInputStream();
		}

	}

//private classes
	
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
	    protected void onActivityResult(
	            int requestCode, int resultCode, Intent data) {
	        // Decide what to do based on the original request code
	        switch (requestCode) {

	            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
	            /*
	             * If the result code is Activity.RESULT_OK, try
	             * to connect again
	             */
	                switch (resultCode) {
	                    case Activity.RESULT_OK :
	                    /*
	                     * Try the request again
	                     */

	                    break;

	        }
	     }
	  }
	    private boolean servicesConnected() {
	        // Check that Google Play services is available
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	        // If Google Play services is available
	        if (ConnectionResult.SUCCESS == resultCode) {
	            // In debug mode, log the status
	            Log.d("Location Updates",
	                    "Google Play services is available.");
	            // Continue
	            return true;
	        // Google Play services was not available for some reason
	        } else {
	            // Get the error code
	            // Get the error dialog from Google Play services
	            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
	                    resultCode,
	                    this,
	                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

	            // If Google Play services can provide an error dialog
	            if (errorDialog != null) {
	                // Create a new DialogFragment for the error dialog
	                ErrorDialogFragment errorFragment =
	                        new ErrorDialogFragment();
	                // Set the dialog in the DialogFragment
	                errorFragment.setDialog(errorDialog);
	                // Show the error dialog in the DialogFragment
	                errorFragment.show(getFragmentManager(),
	                        "Location Updates");
	                
	            }
	            return false;
	        }
	
	    }
	    
	    /*
	     * Called when the Activity becomes visible.
	     */
	    @Override
	    protected void onStart() {
	        super.onStart();
	        // Connect the client.
	        mLocationClient.connect();

	        
	    }
	    /*
	     * Called when the Activity is no longer visible.
	     */
	    @Override
	    protected void onStop() {
	        // Disconnecting the client invalidates it.
	        mLocationClient.disconnect();
	        super.onStop();
	    }
	 
	 
	 
	 
	/**
	 * Fragment that appears in the "content_frame", shows a page
	 */
	public static class PageFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";
		public PageFragment() {
			// Empty constructor required for fragment subclasses
		}

		 @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		        
		        int i = getArguments().getInt(ARG_PLANET_NUMBER);
		        String menuItem= getResources().getStringArray(R.array.menu_item)[i];
		        menuItem = menuItem.replace(" ", "");
		        FragmentName name = FragmentName.valueOf(menuItem);
		        	switch (name) {
		            case RECORDTRANSACTIONS:  return inflater.inflate(R.layout.fragment_record, container, false);
		            case MISSINGTRANSACTIONS:  return inflater.inflate(R.layout.fragment_missing, container, false);
		            case VOIDTRANSACTIONS:  return inflater.inflate(R.layout.fragment_void, container, false);
		            case SALESTRANSACTIONS:  return inflater.inflate(R.layout.fragment_sales, container, false);
		            default: return inflater.inflate(R.layout.fragment_record, container, false);
		        	}
		}
		 
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                /*
                int j = getArguments().getInt(ARG_PLANET_NUMBER);
                String menuItem = getResources().getStringArray(R.array.menu_item)[j];
                if(menuItem.equals("Near By"))
                {
                	mCurrentLocation = mLocationClient.getLastLocation();
            		NearByModelAdapter.LoadModel(listToDisplay , mCurrentLocation);
            		String[] ids = new String[NearByModelAdapter.Items.size()];
            		for (int i = 0; i < ids.length; i++) {

            			ids[i] = Integer.toString(i + 1);
            		}
                    NearByItemAdapter Adapter = new NearByItemAdapter(getActivity(), R.layout.merchant_list_item, ids);
                    listViewToDisplay.setAdapter(Adapter);
 //                   listViewToDisplay.setOnItemClickListener(new NearByItemClickListener());

                }
                
                */}



public enum FragmentName {

    RECORDTRANSACTIONS,
    MISSINGTRANSACTIONS,
    VOIDTRANSACTIONS,
    SALESTRANSACTIONS,

 }
		}
		

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
}