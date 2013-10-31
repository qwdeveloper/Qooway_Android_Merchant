package Notifications;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.example.merchantv01.R;

public class NotificationItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;


	public NotificationItemAdapter(Context context, int textViewResourceId,
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
		ImageView userImageView = (ImageView) rowView
				.findViewById(R.id.userImage);
		final ImageButton crossImageView = (ImageButton) rowView
				.findViewById(R.id.crossImage);
		final ImageButton checkImageView = (ImageButton) rowView
				.findViewById(R.id.checkImage);
		TextView checkInTextView = (TextView) rowView
				.findViewById(R.id.checkInText);
		final int id = Integer.parseInt(Ids[position]);
		
		crossImageView.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {

				if(!NotificationModelAdapter.GetbyId(id).checkPressed)
				{
					crossImageView.setBackgroundResource(context.getResources()
							.getIdentifier("record_checkins_x_no__icon_active", "drawable",
									context.getPackageName()));
					NotificationModelAdapter.SetCrossPressed(id);
				}
			}
		});

		checkImageView.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				if(!NotificationModelAdapter.GetbyId(id).crossPressed)
				{
				checkImageView.setBackgroundResource(context.getResources()
						.getIdentifier("record_checkins_y_yes__icon_active", "drawable",
								context.getPackageName()));
				NotificationModelAdapter.SetCheckPressed(id);
				}
			}

		});


		String userImageFile = NotificationModelAdapter.GetbyId(id).userImageFile_;
		String crossImageFile = NotificationModelAdapter.GetbyId(id).crossImageFile_;
		String checkImageFile = NotificationModelAdapter.GetbyId(id).checkImageFile_;

		checkInTextView
				.setText(NotificationModelAdapter.GetbyId(id).checkInMessage_);

		userImageView.setBackgroundResource(context.getResources()
				.getIdentifier(userImageFile, "drawable",
						context.getPackageName()));
		crossImageView.setBackgroundResource(context.getResources()
				.getIdentifier(crossImageFile, "drawable",
						context.getPackageName()));
		checkImageView.setBackgroundResource(context.getResources()
				.getIdentifier(checkImageFile, "drawable",
						context.getPackageName()));
		return rowView;

	}

}