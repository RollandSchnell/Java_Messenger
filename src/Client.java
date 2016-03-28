import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Client extends JFrame 
{
    	private JTextField userText;
    	private JTextArea chatWindow;
    	private ObjectOutputStream output;
    	private ObjectInputStream input;
    	private String message = "";
    	private String serverIP;
    	private Socket connection;
    	
    	
    	
    	
public Client(String host)
{
	super("Client Info");
	serverIP = host;
	userText = new JTextField();
	userText.setEditable(false);
	userText.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					sendMessage(e.getActionCommand());
					userText.setText("");
				}
			}
			
			);
	
	add(userText, BorderLayout.NORTH);
	chatWindow = new JTextArea();
	add(new JScrollPane(chatWindow),BorderLayout.CENTER);
	setSize(300,150);
	setVisible(true);
	
	
}
    	

//connect to server

public void startRunning()
{
	
try
{
	connectToServer();
	setupStreams();
	whileChatting();
	
}
catch(EOFException e)
{
	showMessage("\nClient terminated connection ");
	
	
	
}
catch(IOException e)
{
	e.printStackTrace();
}
finally
{
	closeProgram();
}

}


//connect to server

public void connectToServer() throws IOException
{
	
	showMessage("Attempting connection...\n");
	
	connection = new Socket(InetAddress.getByName(serverIP),6789);
	
	showMessage("Connected to:"+ connection.getInetAddress().getHostName());
	
	
}

//setting up the streams

private void setupStreams() throws IOException
{

	output = new ObjectOutputStream(connection.getOutputStream());
	output.flush();
	input = new ObjectInputStream(connection.getInputStream());
	showMessage("\nStreams ready to go !");
	
}

//while chatting
private void whileChatting() throws IOException
{
	ableToType(true);
	do
	{
		try
		{
			message = (String) input.readObject();
			showMessage("\n" + message);
			
		}
		catch(ClassNotFoundException e)
		{
			showMessage("\n unidentified object type...");
			
		}
		
	}
	while(!message.equals("SERVER - END"));
	
	
	
	
}


//closing the client

public void closeProgram()
{
	
	
	showMessage("\n Closing the program... ");
	ableToType(false);
	
	try
	{
		output.close();
		input.close();
		connection.close();
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
}

//sending message to server

public void sendMessage(String message)
{
	try
	{
		output.writeObject("CLIENT - " + message);
		output.flush();
		showMessage("\n CLIENT - " + message);
		
	}
	catch(IOException e)
	{
		chatWindow.append("\nError in sending the message to server.");
		
	}
	
}

//change/update chatWindow

private void showMessage(final String m)
{
	SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					chatWindow.append(m);
				}
			});
}


//user permission

private void ableToType(final boolean write)
{
	SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					userText.setEditable(write);
				}
			});
}
    	
}

    	