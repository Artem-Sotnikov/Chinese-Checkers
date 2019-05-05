/* PlayerClient.java 
 * Purpose: Connects player to the server 
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class PlayerClient implements Runnable {
  
  // Variables
  private static JFrame mainFrame;
  private static JPanel mainPanel;
  private static JButton joinGameButton, exitButton;
  private static JLabel userNameLabel, addressLabel, roomNameLabel;
  private static JTextField userNameField, addressField, roomNameField;
  private static String userName, addressIP, roomName;
  private static Socket mySocket;
  private static BufferedReader input;
  private static PrintWriter output;
  private static boolean running, connected;
  private static BoardPanel tempBoard;
  
  /** 
   * PlayerClient
   * Constructor that simply creates a client
   */
  PlayerClient() {
    running = true;
    connected = false;
  }
  
  /**
   * run
   * This method creates UI and starts the method to read messages from the server when it is connected
   */
  public void run() {
    createGUI();
    tempBoard = new BoardPanel();
    tempBoard.configureServerSetup();
    do {
      // Add a short delay
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      // Start reading messages from the server if the client is connected
      if (connected) {
        readMessagesFromServer();
      }
    } while (!connected);
  }
  
  /** 
   * createGUI
   * Creates a panel that asks for info to join the game
   */
  public void createGUI(){
    // Create the main frame containing the UI to connect to the server
    mainFrame = new JFrame();
    mainFrame.setSize(500, 300);
    
    // Create the panel containing the UI to connect to the server
    mainPanel = new JPanel();
    mainPanel.setSize(500, 300);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    
    // Create buttons
    joinGameButton = new JButton("Join Game");
    exitButton = new JButton("Exit");
    
    // Add action listeners to the buttons
    ActionListener listener = new ButtonListener();
    joinGameButton.addActionListener(listener);
    exitButton.addActionListener(listener);
    
    // Create labels to describe the input fields
    userNameLabel = new JLabel("Please enter your username");
    roomNameLabel = new JLabel("Please enter the room name");
    addressLabel = new JLabel("Please enter your IP adress");
    
    // Create text fields to collect information from the user
    userNameField = new JTextField();
    roomNameField = new JTextField();
    addressField = new JTextField();
    
    // Add all UI components to the main panel
    mainPanel.add(userNameLabel);
    mainPanel.add(userNameField);
    mainPanel.add(roomNameLabel);
    mainPanel.add(roomNameField);
    mainPanel.add(addressLabel);
    mainPanel.add(addressField);
    mainPanel.add(joinGameButton);
    mainPanel.add(exitButton);
    
    // Add main panel to the main frame and set the frame to visible
    mainFrame.add(mainPanel);
    mainFrame.setVisible(true);
  }//end of createGUI
  
  /** 
   * connect
   * Connects the client to the server
   * @param String ip, the ip address of the server
   * @param int port, the port number to be used to connect
   * @param String userName, the name of the player
   * @param String roomName, the name of the room to join
   * @return Socket, the socket created
   */
  public Socket connect(String ip, int port, String userName, String roomName) {
    System.out.println("Attempting to make a connection..");
    
    try {
      //Initialize some variables  
      System.out.println("making variables");
      boolean roomExists = false;
      boolean userNameAvailable = false;
      boolean inputExists = false;
      String connectionVerification;
      
      // Attempt to create a socket connection
      System.out.println("created socket");
      mySocket = new Socket(ip, port);
      
      // Create BufferedReader for input and PrintWriter for output
      input = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
      output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream
      
      // Communicate the room to join to the server
      output.println("JOINROOM" + " " + roomName);
      output.flush();
      
      // Check that the user can join the room
      System.out.println("checking room existence");
      roomExists = checkRoom(roomName);
      
      // Communicate the username of choice to the server
      
      output.println("CHOOSENAME" + " " + userName);
      output.flush();
      
      if (roomExists) {
        System.out.println("room exists");
        // Check that the username is usable
        System.out.println("checking name existence");
        userNameAvailable = checkName(userName);
        
        // State if the client can be connected or not
        if (userNameAvailable) {
          System.out.println("name available");
          connected = true;
        } else {
          System.out.println("name unavailable");
        }
      } else {
        System.out.println("room does not exist");
      }
      
    } catch (IOException e) {
      System.out.println("Connection to Server Failed");
      e.printStackTrace();
    }
    
    if (connected) {
      System.out.println("Connection made.");
      return mySocket;
    } else {
      return null;
    }
  }//end of connect
  
  /** 
   * readMessagesFromServer
   * This method reads messages sent from the server
   */
  public void readMessagesFromServer() {
    while(running) {
      try {
        // Check for incoming messages
        if (input.ready()) {
          // Read the message
          String instructions;
          instructions = input.readLine().trim();
          
          if (instructions.contains("OK")) {
            System.out.println("accepted ok message");
          } else {
            // Convert instructions into coordinates
            convertInstructions(instructions);
          }
        }
        
      } catch (IOException e) {
        System.out.println("Failed to receive msg from the server");
        e.printStackTrace();
      }
    }
    try {
      // Close all the sockets
      input.close();
      output.close();
      mySocket.close();
    }catch (Exception e) {
      System.out.println("Failed to close socket");
    }  
  }
  
  /** 
   * checkRoom
   * Verifies validity of a room the usr wants to join
   * @param String roomName, the name of the room user wants to join
   * @return boolean, true if the room is valid, false if it isn't
   */
  public boolean checkRoom(String roomName) {
    boolean inputExists = false;
    boolean roomExists = false;
    String connectionVerification;
    
    System.out.println("sent joinroom message");
    output.println("JOINROOM " + roomName); 
    output.flush();
    
    while (!inputExists) {
      try {
        if (input.ready()) {
          inputExists = true;
          connectionVerification = input.readLine();
          //Verify whether or not connection has been made
          if (connectionVerification.contains("OK")) {
            System.out.println("ok");
            roomExists = true;
          } else {
            System.out.println("not ok");
            roomExists = false;
          }
        }
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return roomExists;
  }
  
  /** 
   * checkName
   * Checks if usename is valid
   * @param String userName, the desired username for the player
   * @return boolean, true if the name is valid, false if it is not
   */
  public boolean checkName(String userName) {
    boolean inputExists = false;
    boolean userNameAvailable = false;
    String connectionVerification;
    
    output.println("CHOOSENAME " + userName);
    output.flush();
    
    while (!inputExists) {
      try {
        if (input.ready()) {
          inputExists = true;
          connectionVerification = input.readLine();
          //Check that the server verifies the name
          if (connectionVerification.contains("OK")) {
            userNameAvailable = true;
          } else {
            userNameAvailable = false;
          }
        }
      } catch (Exception e) {
        
      }
    }
    return userNameAvailable;
  }
  
  /** 
   * convertInstructions
   * Converts the instructions from the server to an array of coordinates for our use 
   * @param String instructions, the instructions from the server
   */
  public void convertInstructions(String instructions) {
    System.out.println(instructions);
    
    // Trim the instructions received to just the coordinates
    instructions = instructions.substring(10);
    
    // Use regex to get rid of the brackets and commas for the brackets and store the individual numbers into an array
    String[] splitString = instructions.replaceAll("[()]", "").replaceAll(",", " ").split(" ");
    
    // Create an array of ints that is the same size as the array of Strings
    int[] splitIntegers = new int[splitString.length];
    
    // Create an array of ArrayCoordinates
    ArrayCoordinate[] coordinates = new ArrayCoordinate[splitIntegers.length/2];
    
    // Convert the Strings to ints
    for (int o = 0; o < splitString.length; o++) {
      try {
        splitIntegers[o] = Integer.parseInt(splitString[o]);
      } catch (NumberFormatException e) {
        //e.printStackTrace();
      }
    }
    
    // Store each pair of numbers as ArrayCoordinates
    for (int i = 0; i < coordinates.length; i ++) {
      coordinates[i] = new ArrayCoordinate((splitIntegers[2 * i] - 1), (splitIntegers[(2 * i) + 1] - 1));
    }
    
    tempBoard.cleanseBoard();
    tempBoard.setUpBoard(coordinates);
    
    // Run the rest of the code to determine the best moves
    findBestMove();
  }
  
  /** 
   * findBestMove
   * Calls methods to find optimal move for the turn
   */
  public void findBestMove(){
//        Display disp = new Display();
//        disp.refresh();
//        System.out.println("executed");
//        while (!disp.exitFlag) {
//            disp.refresh();
//        }\
    MoveCode moveToSend = tempBoard.executeBestMove();
    System.out.println("(" + moveToSend.startPosition.row + "," + moveToSend.startPosition.column + ") (" + moveToSend.targetPosition.row + "," + moveToSend.targetPosition.column + ")");
    sendMovesToServer(moveToSend);
    
  }
  
  /** 
   * sendMovesToServer
   * Communicates what moves we want to make for the team
   * @param MoveCode move, the move to be sent
   */
  public void sendMovesToServer(MoveCode move) {
    System.out.println("sent move to server");
    // Output the move we want
    System.out.println("MOVE (" + (move.startPosition.row + 1) + "," + (move.startPosition.column + 1) + ") (" + (move.targetPosition.row + 1) + "," + (move.targetPosition.column + 1) + ")");
    output.println("MOVE (" + (move.startPosition.row + 1) + "," + (move.startPosition.column + 1) + ") (" + (move.targetPosition.row + 1) + "," + (move.targetPosition.column + 1) + ")");
    output.flush();
  }
  
  /* ButtonListener.java 
   * Purpose: To gauge user actions and movement
   * Creators: Artem, Joyce, Shi Han
   * Date: 2019-05-04
   */
  private class ButtonListener implements ActionListener {
    /** 
     * actionPerformed
     * Manages user actions
     * @param ActionEvent press, the response from the user
     */
    public void actionPerformed(ActionEvent press) {
      if (press.getSource() == joinGameButton) {
        try {
          // Get information from the text boxes
          userName = userNameField.getText();
          addressIP = addressField.getText();
          roomName = roomNameField.getText();
          
          // Check that all the fields are filled in
          if ((!userName.equals(""))&&(!addressIP.equals(""))&&(!roomName.equals(""))){
            //do something
            System.out.print(userName+" "+addressIP+" "+roomName);
            // Empty the text fields
            userNameField.setText("");
            addressField.setText("");
            roomNameField.setText("");
            
            // Attempt to connect
            connect(addressIP, 6666, userName, roomName);
          }
//          } else { //if some fields are left blank
//            warningBox.setSize(100,200);
//            JOptionPane.showMessageDialog(warningBox, "Not all of the fields were filled!", "Error!", JOptionPane.ERROR_MESSAGE);
//          }
        } catch (NumberFormatException e) { //warning for improper data entry
//            warningBox.setSize(100, 200);
//            JOptionPane.showMessageDialog(warningBox, "Numbers were not entered properly!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
      } else if (press.getSource() == exitButton) {
        System.exit(0);
      }
    }
  }
}//end of PlayerClient