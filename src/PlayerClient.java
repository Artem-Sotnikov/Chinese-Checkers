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
  private static JLabel userNameLabel, addressLabel, portNumLabel;
  private static JTextField userNameField, addressField, portNumField;
  private static String userName, addressIP;
  private static int portNum;
  private static Socket mySocket;
  private static BufferedReader input;
  private static PrintWriter output;
  
  public void run() {
    createGUI();
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
          portNum = Integer.parseInt(portNumField.getText());
          if ((!userName.equals(""))&&(!addressIP.equals(""))&&(portNum > 0)){
            //do something
            System.out.print(userName+" "+addressIP+" "+portNum);
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
    portNumLabel = new JLabel("Please enter the port number");
    portNumField = new JTextField();
    //portNumLabel.setLabelFor(portNumLabel);
    
    mainPanel.add(userNameLabel);
    mainPanel.add(userNameField);
    mainPanel.add(addressLabel);
    mainPanel.add(addressField);
    mainPanel.add(portNumLabel);
    mainPanel.add(portNumField);
    mainPanel.add(joinGameButton);
    mainPanel.add(exitButton);
    
    mainFrame.add(mainPanel);
    mainFrame.setVisible(true);
  }//end of createGUI
  
  
  public Socket connect(String ip, int port) { 
    System.out.println("Attempting to make a connection..");
    
    try {
      mySocket = new Socket(ip, port); //attempt socket connection (local address). This will wait until a connection is made
      
      InputStreamReader stream1= new InputStreamReader(mySocket.getInputStream()); //Stream for network input
      input = new BufferedReader(stream1);     
      output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream
      
    } catch (IOException e) {  //connection error occured
      System.out.println("Connection to Server Failed");
      e.printStackTrace();
    }
    System.out.println("Connection made.");
    return mySocket;
  }//end of connect
}//end of PlayerClient