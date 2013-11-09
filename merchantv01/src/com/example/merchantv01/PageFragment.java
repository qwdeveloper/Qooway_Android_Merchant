package com.example.merchantv01;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import Transactions.Transactions;
import Transactions.TransactionsItemAdapter;
import Transactions.TransactionsModelAdapter;
import Transactions.WebPointOrderHead;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Fragment that appears in the "content_frame", shows a page
 */
public class PageFragment extends Fragment {
	public static final String ARG_PAGE_NUMBER = "page_number";
	private MainActivity mainActivity;

	public PageFragment(MainActivity activity) {
		mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		int i = getArguments().getInt(ARG_PAGE_NUMBER);
		String menuItem = getResources().getStringArray(R.array.menu_item)[i];
		menuItem = menuItem.replace(" ", "");
		FragmentName name = FragmentName.valueOf(menuItem);
		View rootView = null;
		try {
			switch (name) {
			case RECORDTRANSACTIONS:
				return createRecord(inflater, container);
			case MISSINGTRANSACTIONS:
				return createMissing(inflater, container);
			case VOIDTRANSACTIONS:
				return createVoid(inflater, container);
			case SALESTRANSACTIONS:
				return createSales(inflater, container);
			default:
				return createRecord(inflater, container);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int j = getArguments().getInt(ARG_PAGE_NUMBER);
		String menuItem = getResources().getStringArray(R.array.menu_item)[j];
		menuItem = menuItem.replace(" ", "");
		FragmentName name = FragmentName.valueOf(menuItem);
		switch (name) {
		case RECORDTRANSACTIONS:
			this.setupRecord(this.getActivity());
			break;
		case MISSINGTRANSACTIONS:
			this.setupMissing(this.getActivity());
			break;
		case VOIDTRANSACTIONS:
			this.setupVoid(this.getActivity());
			break;
		case SALESTRANSACTIONS:
			this.setupSales(this.getActivity());
			break;
		}
	}

	private View createRecord(LayoutInflater inflater, ViewGroup container) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_record, container,
				false);
		mainActivity.submit = (Button) rootView.findViewById(R.id.submitButton);
		mainActivity.submit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String salesAmountString = mainActivity.salesAmount.getText().toString();
				String cardNumberString = mainActivity.cardNumber.getText().toString();
				String referenceNumberString = mainActivity.referenceNumber.getText()
						.toString();
				try {
					this.insertRecord(salesAmountString, cardNumberString,
							referenceNumberString);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private void insertRecord(String salesAmount,
					String cardNumber, String referenceNumber)
					throws JSONException {
				WebPointOrderHead tran = new WebPointOrderHead(salesAmount, cardNumber, referenceNumber);
				Gson gson = new Gson();
				String json = gson.toJson(tran);

				try {
					mainActivity.webApiPost(mainActivity.postUrl, json);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		mainActivity.salesAmount = (TextView) rootView.findViewById(R.id.salesAmount);
		mainActivity.cardNumber = (TextView) rootView.findViewById(R.id.cardNumber);
		mainActivity.referenceNumber = (TextView) rootView
				.findViewById(R.id.referenceNum);
		return rootView;
	}

	private View createMissing(LayoutInflater inflater, ViewGroup container) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_missing, container,
				false);
		mainActivity.submit = (Button) rootView.findViewById(R.id.submitButton);
		return rootView;
	}

	private View createVoid(LayoutInflater inflater, ViewGroup container)
			throws InterruptedException, ExecutionException {

		View rootView = null;
		this.mainActivity.webApiGet(this.mainActivity.serverUrl + '/'
				+ this.mainActivity.APIUrl);
		rootView = inflater.inflate(R.layout.fragment_void, container,
				false);
		mainActivity.listViewToDisplay = (ListView) rootView
				.findViewById(R.id.listView1);
		return rootView;
	}

	private View createSales(LayoutInflater inflater, ViewGroup container)
			throws InterruptedException, ExecutionException {

		View rootView = null;
		this.mainActivity.webApiGet(this.mainActivity.serverUrl + '/'
				+ this.mainActivity.APIUrl);
		rootView = inflater.inflate(R.layout.fragment_sales, container,
				false);
		mainActivity.listViewToDisplay = (ListView) rootView
				.findViewById(R.id.listView1);
		return rootView;
	}

	private void setupRecord(Activity act) {
		if (mainActivity.progress != null)
			mainActivity.progress.cancel();
	}

	private void setupMissing(Activity act) {
		mainActivity.progress.cancel();
	}

	private void setupVoid(Activity act) {

		TransactionsItemAdapter Adapter = null;
		try {
			TransactionsModelAdapter.LoadModel(mainActivity.listToDisplay);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] voidids = new String[TransactionsModelAdapter.Items.size()];
		for (int i = 0; i < voidids.length; i++) {
			voidids[i] = Integer.toString(i + 1);
		}
		Adapter = new TransactionsItemAdapter(act,
				R.layout.void_transactions_list, voidids);
		mainActivity.listViewToDisplay.setAdapter(Adapter);
	}

	private void setupSales(Activity act) {

		TransactionsItemAdapter Adapter = null;
		try {
			TransactionsModelAdapter.LoadModel(mainActivity.listToDisplay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] salesids = new String[TransactionsModelAdapter.Items
				.size()];
		for (int i = 0; i < salesids.length; i++) {
			salesids[i] = Integer.toString(i + 1);
		}
		Adapter = new TransactionsItemAdapter(act,
				R.layout.sales_history_list, salesids);
		mainActivity.listViewToDisplay.setAdapter(Adapter);
	}
	public enum FragmentName {

		RECORDTRANSACTIONS, MISSINGTRANSACTIONS, VOIDTRANSACTIONS, SALESTRANSACTIONS,

	}
}