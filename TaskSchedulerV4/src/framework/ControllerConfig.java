package framework;

public class ControllerConfig 
{
	public static String IP = new String();
	public static String userName = new String();
	public static String password = new String();
	public static int port = 8181;
	public ControllerConfig(String IP_Addr, String user_Name, String pass_word, int port)
	{
		IP = IP_Addr;
		userName = user_Name;
		password = pass_word;
		this.port = port;
	}
}
