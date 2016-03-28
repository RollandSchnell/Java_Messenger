import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Server extends JFrame 
{
    	private JTextField userText;
    	private JTextArea chatWindow;
    	private ObjectOutputStream output;
    	private ObjectInputStream input;
    	private ServerSocket server;
    	private Socket connection;
    	
    	
    	
    	public Server()
    	{
    		super("Messanger");
    		userText = new JTextField();
    		userText.setEditable(false);
    		userText.addActionListener(
    				new ActionListener()
    				{
    					public void actionPerformed(ActionEvent event)
    					{
    						sendMessage(event.getActionCommand());
    						userText.setText("");
    						
    					}
    				}
    	);
    		add(userText,BorderLayout.NORTH);
    		chatWindow = new JTextArea();
    		add(new JScrollPane(chatWindow));
    		setSize(300,150);
    		setVisible(true);
    		
    		
    	}
    	
    	//starting the server
    	
    	public void startRunning()
    	{
    		try
    		{
    			server = new ServerSocket(6789, 100); //port + socket
    			while(true)
    			{
    				try
    				{
    					waitForConnection();
    					setupStreams();
    					whileChatting();
    					
    				}
    				catch(EOFException eofException)
    				
    				{
    					showMessage("\n Server connection ended");
    					
    				}
    				finally
    				{
    					closeProgram();
    				}
    			}
    			
    		}
    		catch(IOException ioException)
    		{
    			ioException.printStackTrace();
    		}
    	}
    	
    	
    	//connection waiting
	private void waitForConnection() throws IOException
	{
		showMessage("Waiting for a connection...");
		connection = server.accept();
		showMessage("\nConnected to "+connection.getInetAddress().getHostName());
	}
	
	//getting the stream for data
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams are setup! ");
		
	}
	
	//conversation
	private void whileChatting() throws IOException
	{
		
		String message = (" You are connected ");
		sendMessage(message);
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
				showMessage("\n ???? ");
			}
			
		}
		while(!message.equals("CLIENT - END"));
		
		
	}
	
	//Closing the Streams
	
	private void closeProgram()
	{
		showMessage("\nClosing connections...\n");
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
	
	//sending a message

	private void sendMessage(String message)
	{
		try
		{
			output.writeObject("SERVER -  " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}
		catch(IOException e)
		{
			chatWindow.append("\nError at sending the message ");
		}
	}
	
	//update chatWindow
	
	private void showMessage(final String text)
	{
		SwingUtilities.invokeLater( //update
				new Runnable()
				{
					public void run()
					{
						chatWindow.append(text);
					}
				}
				);
		
	}
	
	//Typing into the chatWindow
	
	private void ableToType(final boolean write)
	{
		SwingUtilities.invokeLater(
		new Runnable()
		{
			public void run()
			{
				userText.setEditable(write);
			}
		}
		);
		
	}
	
	
	
	
}

 
