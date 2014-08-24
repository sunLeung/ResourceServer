package utils;

public class Def {
	/*****************************货币**********************************/
	/**CNY(Chinese Yuan)人民币*/
	public static final String CNY="CNY";
	/**USD(United States Dollar)美元*/
	public static final String USD="USD";
	/**NTD(New Taiwan Dollar)新台币*/
	public static final String NTD="NTD";
	
	
	
	/*****************************支付状态**********************************/
	public static final int PaySucceed=0;
	public static final int PayFail=1;
	
	
	/*****************************请求控制**********************************/
	/**不连通服务器的情况下默认请求次数*/
	public static final int RequestLoop=3;
	
	/*****************************登陆验证返回码**********************************/
	public static final int LoginSucceed=0;
	public static final int LoginFail=1;
	public static final int LoginException=2;
}
