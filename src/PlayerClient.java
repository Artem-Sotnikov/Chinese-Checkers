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

/**
 * PlayerClient
 * This class connects the player to the server
 */

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

    PlayerClient() {
        running = true;
        connected = false;
    }

    /**
     * This method creates UI and starts the method to read messages from the server when it is connected
     */
    public void run() {
        createGUI();
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


    public Socket connect(String ip, int port, String userName, String roomName) {
        System.out.println("Attempting to make a connection..");

        try {
            boolean roomExists = false;
            boolean userNameAvailable = false;
            boolean inputExists = false;
            String connectionVerification;

            // Attempt to create a socket connection
            mySocket = new Socket(ip, port);

            // Create BufferedReader for input and PrintWriter for output
            input = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream

            // Communicate the room to join to the server
            output.println("JOINROOM " + roomName);
            output.flush();

            // Check that the user can join the room
            roomExists = checkRoom();

            // Communicate the username of choice to the server
            output.println("CHOOSENAME " + userName);
            output.flush();

            if (roomExists) {
                // Check that the username is usable
                userNameAvailable = checkRoom();

                // State if the client can be connected or not
                if (userNameAvailable && roomExists) {
                    connected = true;
                }
            }



        } catch (IOException e) {
            System.out.println("Connection to Server Failed");
            e.printStackTrace();
        }
        System.out.println("Connection made.");

        if (connected) {
            return mySocket;
        } else {
            return null;
        }
    }//end of connect

    public void readMessagesFromServer() {


        while(running) {
                try {

                    // Check for incoming messages
                    if (input.ready()) {
                        // Read the message
                        String instructions;
                        instructions = input.readLine().trim();

                        // Convert instructions into coordinates
                        convertInstructions(instructions);
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

    public boolean checkRoom() {
        boolean inputExists = false;
        boolean roomExists = false;
        String connectionVerification;
        while (!inputExists) {
            try {
                if (input.ready()) {
                    inputExists = true;
                    connectionVerification = input.readLine();
                    if (connectionVerification.contains("OK")) {
                        roomExists = true;
                    } else {
                        roomExists = false;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return roomExists;
    }

    public boolean checkName() {
        boolean inputExists = false;
        boolean userNameAvailable = false;
        String connectionVerification;
        while (!inputExists) {
            try {
                if (input.ready()) {
                    inputExists = true;
                    connectionVerification = input.readLine();
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

    public void convertInstructions(String instructions) {

        // Trim the instructions received to just the coordinates
        instructions = instructions.substring(10);

        // Use regex to get rid of the brackets and commas for the brackets and store the individual numbers into an array
        String[] splitString = instructions.replaceAll("[(),]", "").split(" ");

        // Create an array of ints that is the same size as the array of Strings
        int[] splitIntegers = new int[splitString.length];

        // Create an array of ArrayCoordinates
        ArrayCoordinate[] coordinates = new ArrayCoordinate[splitIntegers.length/2];

        // Convert the Strings to ints
        for (int o = 0; o < splitString.length; o++) {
            splitIntegers[o] = Integer.parseInt(splitString[o]);
        }

        // Store each pair of numbers as ArrayCoordinates
        for (int i = 0; i < coordinates.length; i ++) {
            coordinates[i] = new ArrayCoordinate((splitIntegers[2 * i] - 1), (splitIntegers[(2 * i) + 1] - 1));
        }
        for (int k = 0; k < coordinates.length; k++) {
            coordinates[k].displayCoordinate();
        }

        // Run the rest of the code to determine the best moves
        findBestMove();
    }

    public void findBestMove(){
        Display disp = new Display();
        disp.refresh();
        System.out.println("executed");
        while (!disp.exitFlag) {
            disp.refresh();
        }
    }

    public void sendMovesToServer() {
        // Output the move we want
        output.println("MOVE (" + "," + ") (" + "," + ")");
        output.flush();
    }

    private class ButtonListener implements ActionListener {
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