package Notifications;

public class NotificationItem {

    public int Id;
    public String userImageFile_;
    public String crossImageFile_;
    public String checkImageFile_;
    public String checkInMessage_;
    public static String hasCheckedIn = "\nhas checked in.\n"; 
    public Boolean checkPressed =false;
    public Boolean crossPressed =false;

    public NotificationItem(int id , String userName, String userImageFile,  String time ,String crossImageFile, String checkImageFile ) 
    {
    	Id = id;
    	checkInMessage_ =userName + hasCheckedIn +time;
    	userImageFile_ = userImageFile;
    	crossImageFile_= crossImageFile;
    	checkImageFile_ = checkImageFile;
    }
    public void setCheckPressed()
    {
    	checkPressed =true;
    }
    public void setCrossPressed()
    {
    	crossPressed =true;
    }
}
