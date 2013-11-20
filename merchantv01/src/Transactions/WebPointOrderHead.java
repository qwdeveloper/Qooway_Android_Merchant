package Transactions;
import java.util.Date;
public class WebPointOrderHead {

	private Integer OrderID;
	private Date OrderTime;
	private String PointCardCode;
	private String CustomerID;
	private Integer MerchantID;
	private Float SoldAmount;
	private String Reference;
	private Integer Points;
	private Integer NetPoints;
	private String OrderType;
	private String Status;
	private String Remark;
	private String OLOStoreID;
	private String OLOStoreName;
	private Float FinalSoldAmount;
	private Integer FinalPoints;
	private Integer PointRate;
	private Date CreateTime;
	private Float OrderFeeAmount;
	private Integer BillId;
	
	public WebPointOrderHead(String soldAmount, String customerID , String reference , Integer merchantID)
	{
		OrderID = null;
		MerchantID = merchantID;
		Points = null;
		NetPoints= null;
		FinalPoints=null;
		PointRate= null;
		BillId = 0;
		SoldAmount = Float.parseFloat(soldAmount);
		FinalSoldAmount =null;
		OrderFeeAmount = (float) 0.00;
		OrderType = "I" ;
		Status = null;
		OrderTime = new Date();
		CreateTime = new Date();
		PointCardCode = null;
		CustomerID= customerID ;
		Reference = reference ;
		Remark = null ;
		OLOStoreID = "" ;
		OLOStoreName = "" ;
	}
}
