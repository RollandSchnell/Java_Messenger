import javax.swing.JFrame;


public class ClientTest 
{
	public static void main(String [] args)
	{
		Client one;
		one = new Client("127.0.0.1");
		one.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		one.startRunning();
	}

}
