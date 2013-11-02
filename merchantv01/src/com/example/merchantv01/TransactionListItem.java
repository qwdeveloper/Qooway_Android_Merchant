package com.example.merchantv01;

public class TransactionListItem {

	public int Id;
    public String ThumbNail;
    public String[] Info;
    public Boolean pressed=false;

	public TransactionListItem(int id,  String[] info) {
		Id = id;
		Info = info;
	}
	
    public void PressButton()
    {
    	if(pressed)
    	pressed =false;
    	else
    		pressed=true;
    }
}
