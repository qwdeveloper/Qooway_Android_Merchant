package Notifications;

import java.util.ArrayList;

public class NotificationModelAdapter {
	public static ArrayList<NotificationItem> Items;

	public static void LoadModel(String[] userNames, String userImageFiles[],
			String checkInTime[], String crossImageFile, String checkImageFile) {

		Items = new ArrayList<NotificationItem>();
		for (int i = 0; i < userNames.length; i++) {
			Items.add(new NotificationItem(i + 1, userNames[i],
					userImageFiles[i], checkInTime[i], checkImageFile,
					crossImageFile));
		}
	}

	public static NotificationItem GetbyId(int id) {

		NotificationItem result = null;
		for (NotificationItem item : Items) {
			if (item.Id == id)
				result = item;
		}
		return result;
	}

	public static void SetCheckPressed(int id) {

		NotificationItem updateItem = null;
		for (NotificationItem item : Items) {
			if (item.Id == id)
				updateItem = item;
		}
		updateItem.setCheckPressed();
	}

	public static void SetCrossPressed(int id) {

		NotificationItem updateItem = null;
		for (NotificationItem item : Items) {
			if (item.Id == id)
				updateItem = item;
		}
		updateItem.setCrossPressed();
	}
}
