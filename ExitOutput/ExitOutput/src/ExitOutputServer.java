/******************************************************************************************************************
* File: Server.java
* Course: 2013 LG Executive Education Program
* Project: Iot (2013), Autowarehouse (2014)
* Copyright: Copyright (c) 2013 Carnegie Mellon University
* Versions:
*	1.0 Apr 2013.
*
* Description:
*
* This class serves as an example for how to write a server application that a client Arudino application can
* connect to. There is nothing uniquely specific to the Arduino in this code, other than the application level
* protocol that is used between this application and the Arduino. The assumption is that the Arduino is running
* the ClientDemo application. When this application is started it listens until a client connects. Once the client
* connects, this app reads data from the client until the client sends the string "Bye." Each string
* read from the client will be writen to the terminal. Once the "Bye." string is read, the server will send
* a single message back to the client. After this the session ends and server will listen for another client to
* connect. Note, this example server application is single threaded.
*
* Compilation and Execution Instructions:
*
*	Compiled in a command window as follows: javac Server.java
*	Execute the program from a command window as follows: java Server
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


class ExitOutputServer extends Thread{
	InputStream input;
	public static synchronized String getReleaseSlot(){
		String res = ExitOutputMain.currentReleaseSlot;
		return res;
	}
	public void run(){
		ServerSocket serverSocket = null;							// Server socket object
		Socket clientSocket = null;
		Socket localClientSocket = null;
		int msgNum = 0;												// Message to display from serverMsg[]
   		String inputLine;											// Data from client
   		String resMsg="";
   		
   		int clientPortNum = 553;
    	int	localPortNum = 1005;															// Port number for server socket
    	String localServerIP = "192.168.1.136";
		while(true){
			/*****************************************************************************
    	 	* First we instantiate the server socket. The ServerSocket class also does
    	 	* the listen()on the specified port.
		 	*****************************************************************************/
    		try
    		{
        		serverSocket = new ServerSocket(clientPortNum);
        		System.out.println ( "\n\nWaiting for connection on port " + clientPortNum + "." );
        	}
    		catch (IOException e)
        	{
        		System.err.println( "\n\nCould not instantiate socket on port: " + clientPortNum + " " + e);
        		System.exit(1);
        	}

			/*****************************************************************************
    	 	* If we get to this point, a client has connected. Now we need to
    	 	* instantiate a client socket. Once its instantiated, then we accept the
    	 	* connection.
		 	*****************************************************************************/

	    	try
    		{
        		clientSocket = serverSocket.accept();
        	}
    		catch (Exception e)
        	{
        		System.err.println("Accept failed. ");
        		System.exit(1);
        	}

			/*****************************************************************************
    	 	* At this point we are all connected and we need to create the streams so
    	 	* we can read and write.
		 	*****************************************************************************/
           
    		System.out.println ("Connection successful");
    		System.out.println ("Waiting for input.....");
    		
            try{
    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    		BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
    		BufferedWriter localOut = null;
			BufferedReader localIn = null;

			/*****************************************************************************
    		 * Now we can read and write to and from the client. Our protocol is to wait
    		 * for the client to send us strings which we read until we get a "Bye." string
    	 	 * from the client.
		 	 *****************************************************************************/
    			

    		final char PAYMENT ='5';
    		//final char OCCUPIED='';
    		final char RELEASE = '3';
    		final char EXITCLOSE = '6';
 	    	try
 	    	{
 	    		clientSocket.setSoTimeout(5000);
 	    		while ((inputLine = in.readLine()) != null)
    			{
      				System.out.println ("Client Message: " + inputLine);
      				
      				switch(inputLine.charAt(0)){
      				case RELEASE:
      					ExitOutputMain.currentReleaseSlot = inputLine.substring(1,6);
      					System.out.println(ExitOutputMain.currentReleaseSlot);
      					out.write("5Succ\n");
      					/*ReservationInputMain.inUI = true;
						while(ReservationInputMain.inUI){
							//System.out.println(ReservationInputMain.inUI);
							sleep(1000);
						};*/
						/*if(getRescode().length() != 8){
							//ReservationInputUI.In_ParkingSlotText.setText("Wrong Code");
							out.write("Deny\n");
							break;
						}*/
						//System.out.println("release :" + getRescode());
      					break;
      				case PAYMENT:    					
						localClientSocket = new Socket(localServerIP, localPortNum);
						localClientSocket.setSoTimeout(5000);
						try {
							localOut = new BufferedWriter(new OutputStreamWriter(localClientSocket.getOutputStream()));
							localIn = new BufferedReader( new InputStreamReader(localClientSocket.getInputStream()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println(getReleaseSlot().length()+"");
						
						System.out.println("5 "+getReleaseSlot()+'\n');
						localOut.write("5 "+getReleaseSlot()+'\n');
						localOut.flush();
					
						
						inputLine = localIn.readLine();
						System.out.println(inputLine);
						String[] parsingInput = inputLine.split(" ");
						//if()
						if(parsingInput[0].compareTo("5") == 0){
							ExitOutputUI.ID.setText(parsingInput[1]);
							ExitOutputUI.Time.setText(parsingInput[2]+" minutes");
							ExitOutputUI.ParkingFee.setText(parsingInput[3]+" $"); 
							out.write("5Succ\n");
							out.flush();
						}else{
							out.write("5Fail\n");
							out.flush();
						}
						//
		
						/*if(inputLine.substring(1, 5).compareTo("Auth") == 0 ){
							if(inputLine.charAt(5) == '-'){
								ReservationInputUI.In_ParkingSlotText.setText("Full");
								System.out.println("FULL");
							}
							else{
							ReservationInputUI.In_ParkingSlotText.setText(inputLine.charAt(5)+"");
							System.out.println("PASS");
							out.write(inputLine+"\n");
							}
							
						}else{
							ReservationInputUI.In_ParkingSlotText.setText("Wrong Code");
							System.out.println("FAIL");
							out.write("Deny\n");
						}*/
						localIn.close();
			 	    	localOut.close();
						//localIn.readLine()
			 	    	localClientSocket.close();
      					break;
      				case EXITCLOSE:
      					/*localClientSocket = new Socket(localServerIP, localPortNum);
						localClientSocket.setSoTimeout(3000);
						try {
							localOut = new BufferedWriter(new OutputStreamWriter(localClientSocket.getOutputStream()));
							localIn = new BufferedReader( new InputStreamReader(localClientSocket.getInputStream()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						ExitOutputUI.ID.setText("");
						ExitOutputUI.Time.setText("");
						ExitOutputUI.ParkingFee.setText(""); 
						//System.out.println(getReleaseSlot().length()+"");
						
						System.out.println("6");
						//localOut.write("6\n");
						//localOut.flush();
						out.write("Auth\n");
						out.flush();
      					break;
      				default:
      					
      				}
	   				if (inputLine.compareTo("END") == 0)
    	    		 	break;
   				} // while

			} catch (Exception e) {

				System.err.println("readLine failed::");
				//ReservationInputMain.inUI = false;
        		//System.exit(1);

			}


			/*****************************************************************************
			 * Print out the fact that we are sending a message to the client, then we
			 * send the message to the client.
			 *****************************************************************************/

 	    	/*try
 	    	{
 				System.out.println( "Sending message to client...." );
   				//out.write( serverMsg[msgNum], 0, serverMsg[msgNum].length() );
				out.write("Auth\n");
 				out.flush();

			} catch (Exception e) {
				System.err.println("write failed::");
        		//System.exit(1);

			}*/

			/*****************************************************************************
    		 * Close up the I/O ports then close up the sockets
		 	 *****************************************************************************/
 	    	
 	    	out.close();
		    in.close();
   		 	clientSocket.close();
	    	serverSocket.close();
			

			System.out.println ( "\n.........................\n" );

		}catch(Exception ex){
			System.out.println(ex.getMessage());
			
			}
		}
	}
}
	
 