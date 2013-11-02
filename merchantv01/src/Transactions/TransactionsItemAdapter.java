package Transactions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.example.merchantv01.TransactionListItem;
import com.example.merchantv01.R;
import com.example.merchantv01.R.id;

public class TransactionsItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	public TransactionsItemAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);	
		TextView transactionDate= (TextView) rowView.findViewById(R.id.transactionDate);
		TextView transactionTime= (TextView) rowView.findViewById(R.id.transactionTime);
		TextView salesAmount = (TextView) rowView.findViewById(R.id.salesAmount);
		TextView pointsEarned = (TextView) rowView.findViewById(R.id.pointsEarned);
        final int id = Integer.parseInt(Ids[position]);

        final TransactionListItem item = TransactionsModelAdapter.GetbyId(id);
        transactionDate.setText(item.Info[0]);
        transactionTime.setText(item.Info[1]);
        salesAmount.setText(item.Info[2]);
        pointsEarned.setText(item.Info[3]);
    
        if(this.rowResourceId == 2130903049)
        {
        	final ImageButton IMB = (ImageButton) rowView.findViewById(R.id.voidCheckImage);
        	IMB.setOnClickListener(new OnClickListener() {
   			 
    			@Override
    			public void onClick(View arg0) {
    	 				if(!item.pressed)
    				{
    					IMB.setBackgroundResource(context.getResources()
    							.getIdentifier("record_checkins_x_no__icon_active", "drawable",
    									context.getPackageName()));
    					item.PressButton();
    				}
    			}
    		});
        }
		return rowView;
	}

	protected static Object GetbyId(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
