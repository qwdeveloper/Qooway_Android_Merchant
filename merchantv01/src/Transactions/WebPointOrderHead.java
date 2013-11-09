package Transactions;
import java.util.Date;
public class WebPointOrderHead {

	private int OrderID;
	private Date OrderTime;
	private String PointCardCode;
	private String CustomerID;
	private int MerchantID;
	private float SoldAmount;
	private String Reference;
	private int Points;
	private int NetPoints;
	private char OrderType;
	private char Status;
	private String Remark;
	private String OLOStoreID;
	private String OLOStoreName;
	private float FinalSoldAmount;
	private int FinalPoints;
	private int PointRate;
	private Date CreateTime;
	private float OrderFeeAmount;
	private int BillId;
	
	public WebPointOrderHead(String soldAmount, String customerID , String reference)
	{
		OrderID = 400;
		MerchantID = 500;
		Points = 3;
		NetPoints= 4;
		FinalPoints=5;
		PointRate= 6;
		BillId = 7;
		SoldAmount = Float.parseFloat(soldAmount);
		FinalSoldAmount =(float) 12.3;
		OrderFeeAmount = (float) 1241.1;
		OrderType = 'l' ;
		Status = 'o';
		OrderTime = new Date();
		CreateTime = new Date();
		PointCardCode = "123" ;
		CustomerID= customerID ;
		Reference = reference ;
		Remark = "123" ;
		OLOStoreID = "123" ;
		OLOStoreName = "123" ;
	}
}
