package Transactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Notifications.NotificationItem;

import com.example.merchantv01.TransactionListItem;
import com.example.merchantv01.Entry;

import java.text.ParseException;
import 	java.text.SimpleDateFormat;

public class TransactionsModelAdapter {
	public static ArrayList<TransactionListItem> Items;

	@SuppressWarnings("deprecation")
	public static void LoadModel(List<Entry> list ) throws ParseException {
		
		Items = new ArrayList<TransactionListItem>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		int i = 1;
		for (Entry item : list)
		{
			String OrderID = item.OrderID;
			String MerchantID = (String)item.info.get("MerchantID");
			String Points = (String)item.info.get("Points");
			String SoldAmount = (String)item.info.get("SoldAmount");
			String OrderTime = (String)item.info.get("OrderTime");
			Date dateTime = sdf.parse(OrderTime);
			int year = dateTime.getYear()+1900;
			int month = dateTime.getMonth()+1;
			int day = dateTime.getDate();
			int hours  = dateTime.getHours();
			int minuets  = dateTime.getMinutes();
			String Date= year + "-" + month +"-" + day;
			String time = hours + "-" + minuets ;
			String info[] = {Date  ,time ,SoldAmount , Points ,OrderID ,MerchantID };
			Items.add(new TransactionListItem(i, info));
			i++;
		}	
	}

	public static TransactionListItem GetbyId(int id) {
		TransactionListItem current = null;
		for (TransactionListItem item : Items) {
			if (item.Id == id) {
				current =item;
				return current;
			}
		}
		return current;
	}
	
	public static void SetPressed(int id) {

		TransactionListItem updateItem = null;
		for (TransactionListItem item : Items) {
			if (item.Id == id)
				updateItem = item;
		}
		updateItem.PressButton();
	}
}