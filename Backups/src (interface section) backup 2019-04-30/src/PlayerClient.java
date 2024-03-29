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

    public void run() {
        createGUI();
//            while (connected) {
//                readMessagesFromServer();
//            }
    }

    public void createGUI(){
        mainFrame = new JFrame();
        mainFrame.setSize(500, 300);

        mainPanel = new JPanel();
        mainPanel.setSize(500, 300);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        joinGameButton = new JButton("Join Game");

        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    userName = userNameField.getText();
                    addressIP = addressField.getText();
                    roomName = roomNameField.getText();
                    if ((!userName.equals(""))&&(!addressIP.equals(""))&&(!roomName.equals(""))){
                        //do something
                        System.out.print(userName+" "+addressIP+" "+roomName);
                        userNameField.setText("");
                        addressField.setText("");
                        roomNameField.setText("");
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
            }
        });

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        userNameLabel = new JLabel("Please enter your username");
        userNameField = new JTextField();
        //userNameLabel.setLabelFor(userNameField);
        addressLabel = new JLabel("Please enter your IP adress");
        addressField = new JTextField();
        //addressLabel.setLabelFor(addressField);
        roomNameLabel = new JLabel("Please enter the room name");
        roomNameField = new JTextField();
        //portNumLabel.setLabelFor(portNumLabel);

        mainPanel.add(userNameLabel);
        mainPanel.add(userNameField);
        mainPanel.add(roomNameLabel);
        mainPanel.add(roomNameField);
        mainPanel.add(addressLabel);
        mainPanel.add(addressField);
        mainPanel.add(joinGameButton);
        mainPanel.add(exitButton);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

       // connect(addressIP, 6666, userName, roomName);
    }//end of createGUI


    public Socket connect(String ip, int port, String userName, String roomName) {
        System.out.println("Attempting to make a connection..");

        try {
            mySocket = new Socket(ip, port); //attempt socket connection (local address). This will wait until a connection is made

            InputStreamReader stream1= new InputStreamReader(mySocket.getInputStream()); //Stream for network input
            input = new BufferedReader(stream1);
            output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream

            output.println("JOINROOM " + roomName);
            output.flush();
            output.println("JOIN " + userName);
            output.flush();

            connected = true;

        } catch (IOException e) {  //connection error occured
            System.out.println("Connection to Server Failed");
            e.printStackTrace();
        }
        System.out.println("Connection made.");
        readMessagesFromServer();
        return mySocket;
    }//end of connect

    public void readMessagesFromServer() {

        while(running) {  // loop unit a message is received
            try {

                if (input.ready()) { //check for an incoming messge
                    String instructions;
                    instructions = input.readLine(); //read the message
                    convertInstructions(instructions);
                }

            }catch (IOException e) {
                System.out.println("Failed to receive msg from the server");
                e.printStackTrace();
            }
        }
        try {  //after leaving the main loop we need to close all the sockets
            input.close();
            output.close();
            mySocket.close();
        }catch (Exception e) {
            System.out.println("Failed to close socket");
        }

    }

    public void convertInstructions(String instructions) {
        instructions = instructions.substring(10);
        String[] splitString = instructions.replaceAll("[(),]", "").split(" ");
        int[] splitIntegers = new int[splitString.length];
        ArrayCoordinate[] coordinates = new ArrayCoordinate[splitIntegers.length/2];
        for (int o = 0; o < splitString.length; o++) {
            splitIntegers[o] = Integer.parseInt(splitString[o]);
        }
        for (int i = 0; i < coordinates.length; i ++) {
            coordinates[i] = new ArrayCoordinate(splitIntegers[2 * i], splitIntegers[(2 * i) + 1]);
        }
        for (int k = 0; k < coordinates.length; k++) {
            coordinates[k].displayCoordinate();
        }
    }

    public void sendMovesToServer() {

    }
}//end of PlayerClient