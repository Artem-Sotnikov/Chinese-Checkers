import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * PlayerClient
 * This class connects the player to the server
 */

public class PlayerClient implements Runnable {

    // Variables
    private static JFrame mainFrame;
    private static JFrame display;
    private static JPanel mainPanel;
    private static JButton joinGameButton, exitButton;
    private static JLabel userNameLabel, addressLabel, roomNameLabel;
    private static JTextField userNameField, addressField, roomNameField;
    private static String userName, addressIP, roomName;
    private static Socket mySocket;
    private static BufferedReader input;
    private static PrintWriter output;
    private static boolean running, connected, roomExists, userNameAvailable;
    private static BoardPanel tempBoard;

    PlayerClient() {
        running = true;
        connected = false;        
    }

    /**
     * run
     * This method creates UI and starts the method to read messages from the server when it is connected
     */
    public void run() {

        // Create all the GUI for the user to input
        createGUI();

        // Create the board to determine the best moves
        tempBoard = new BoardPanel();
        tempBoard.configureServerSetup();
        this.display = new JFrame();
        this.display.setMinimumSize(new Dimension((int) (600*Constants.scaleFactor),
        		Toolkit.getDefaultToolkit().getScreenSize().height));
        this.display.add(tempBoard);
        this.display.setVisible(true);

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
     * This method creates all the UI for the user to input username, room name, and IP address
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
     * This method attempts to connect the client to the server
     * @param ip The ip address of the server the client is attempting to connect to
     * @param port The port of the server
     * @param userName The username chosen by the client
     * @param roomName The room that the client is trying to connect to
     * @return The socket that connects to the server
     */
    public Socket connect(String ip, int port, String userName, String roomName) {
        System.out.println("Attempting to make a connection..");

        try {

            // Attempt to create a socket connection
            if (mySocket == null) {
                mySocket = new Socket(ip, port);

                // Create BufferedReader for input and PrintWriter for output
                input = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream
            }

            // Communicate the room to join to the server
            if (!roomExists) {
                // Check that the user can join the room
                System.out.println("checking room existence");
                roomExists = checkRoom(roomName);
            }

            // Communicate the username of choice to the server
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
     * This method waits for input from the server and appropriately handles instructions from the server
     */
    public void readMessagesFromServer() {

        while(running) {
                try {

                    // Check for incoming messages
                    if (input.ready()) {
                        // Read the message
                        String instructions;
                        instructions = input.readLine().trim();

                        if (instructions.contains("BOARD")) {
                            // Convert instructions into coordinates
                            convertInstructions(instructions);
                        } else {
                            System.out.println(instructions);
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Failed to receive msg from the server");
                    e.printStackTrace();
                }
        }
        try {
            // Close the input, output, and sockets
            input.close();
            output.close();
            mySocket.close();
        }catch (Exception e) {
            System.out.println("Failed to close socket");
        }

    }

    /**
     * checkRoom
     * This method checks if the room exists on the server
     * @param roomName The name of the room the client is attempting to connect to
     * @return A boolean value of whether the room exists or not
     */
    public boolean checkRoom(String roomName) {
        boolean inputExists = false;
        boolean roomExists = false;
        String connectionVerification;

        // Send the JOINROOM message to the server
        System.out.println("sent joinroom message");
        output.println("JOINROOM " + roomName);
        output.flush();

        while (!inputExists) {
            try {
                if (input.ready()) { // Wait for the server to send back a message
                    inputExists = true;
                    connectionVerification = input.readLine(); // Read the message from the server

                    // Check if the server accepted the room name
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
     * This method checks if the username is valid
     * @param userName The username the client is trying to connect with
     * @return A boolean value of whether or not the username can be used
     */
    public boolean checkName(String userName) {
        boolean inputExists = false;
        boolean userNameAvailable = false;
        String connectionVerification;

        // Send the CHOOSENAME message to the server
        System.out.println("sent choosename message");
        output.println("CHOOSENAME " + userName);
        output.flush();

        while (!inputExists) {
            try {
                if (input.ready()) { // Wait for the server to send a message
                    inputExists = true;
                    connectionVerification = input.readLine(); // Read the message from the server
                    System.out.println(connectionVerification);

                    // Check if the username was accepted
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
     * This method converts the BOARD messages from the server to coordinates
     * @param instructions The message given from the server
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
        for (int i = 0; i < coordinates.length; i ++) {
            coordinates[i].displayCoordinate();
        }

        // Run the rest of the code to determine the best moves
        findBestMove(coordinates);
    }

    /**
     * findBestMove
     * This method finds the best move based off the coordinates given by the server
     * @param coordinates The converted coordinates of all the pieces on the board
     */
    public void findBestMove(ArrayCoordinate[] coordinates){

        // Set up the board according to the instructions given by the server
        System.out.println("cleanse + set up");
        tempBoard.cleanseBoard();
        tempBoard.setUpBoard(coordinates);

        tempBoard.repaint();
//        try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        // Find the best move
        MoveCode moveToSend = tempBoard.executeByDepth(); 
        
        //tempBoard.executeRandomMove();
        
        //tempBoard.executeByEval();
        //new MoveCode(0,0,0,0);
        System.out.println("(" + moveToSend.startPosition.row + "," + moveToSend.startPosition.column + ") (" + moveToSend.targetPosition.row + "," + moveToSend.targetPosition.column + ")");
           
        tempBoard.repaint();
        
        // Send the best move to the server
        sendMovesToServer(moveToSend);

    }

    /**
     * sendMovesToServer
     * This method outputs the move the client wants to make to the server
     * @param move
     */
    public void sendMovesToServer(MoveCode move) {
        System.out.println("sent move to server");
        // Output the desired move to the server
        //System.out.println("MOVE (" + (move.startPosition.row + 1) + "," + (move.startPosition.column + 1) + ") (" + (move.targetPosition.row + 1) + "," + (move.targetPosition.column + 1) + ")");
        
//        output.println("MOVE (" + (move.startPosition.row + 1) + "," + (move.startPosition.column + 1) + ")"
//        		+ " (" + (move.targetPosition.row + 1) + "," + (move.targetPosition.column + 1) + ")");
        
        System.out.println(move.stringPath);
        output.println("MOVE " + move.stringPath);
        output.flush();
    }

    /**
     * ButtonListener
     * This class gives the buttons in the UI functionality
     */
    private class ButtonListener implements ActionListener {

        /**
         * actionPerformed
         * This method determines which button was pressed and performs actions accordingly
         * @param press the button that was pressed
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