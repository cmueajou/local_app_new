import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ReservationInputUI extends Thread{

   public static JTextField In_ParkingSlotText = new JTextField();
   public static JTextField In_StatusText = new JTextField();
   public static JTextField In_NameText = new JTextField();

   public static synchronized void inputReservationCode(String res){
	   ReservationInputMain.resCode = res;
	   ReservationInputMain.inUI = false;
   }
   public static synchronized boolean isConnected(){
	   return ReservationInputMain.inUI;
   }
   public void run() {
	   
      JLabel In_Welcome = new JLabel("  Welcome to Sure-Park!");
      In_Welcome.setFont(new Font(Font.DIALOG, Font.PLAIN, 100));
      In_Welcome.setForeground(new Color(0x00B5AD));
      
      JLabel In_Code = new JLabel("Reservation Code");
      In_Code.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
      In_Code.setBorder(new EmptyBorder(10, 0,10,10));
   
      final JTextField In_CodeText = new JTextField("");
      In_CodeText.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_CodeText.setHorizontalAlignment(SwingConstants.CENTER);
      
      JLabel In_Name = new JLabel("Your ID:");
      In_Name.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_Name.setBorder(new EmptyBorder(10, 0,10,10));
      
      JLabel In_ParkingSlot = new JLabel("Allocated Slot #:");
      In_ParkingSlot.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_ParkingSlot.setBorder(new EmptyBorder(10, 0,10,10));
      
      JLabel In_Status = new JLabel("Authentication Result:");
      In_Status.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_Status.setBorder(new EmptyBorder(10, 0,10,10));
      
      JLabel sureparkIcon = new JLabel(new ImageIcon("./resource/SureparkImage.png"));
      
      In_StatusText.setEditable(false);
      In_StatusText.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_StatusText.setBorder(new EmptyBorder(10,10,10,10));
      
      In_ParkingSlotText.setEditable(false);
      In_ParkingSlotText.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_ParkingSlotText.setBorder(new EmptyBorder(10,10,10,10));
      
      In_NameText.setEditable(false);
      In_NameText.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
      In_NameText.setBorder(new EmptyBorder(10,10,10,10));
      
      
      JButton In_submitButton = new JButton("SUBMIT");
      In_submitButton.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
      In_submitButton.setBackground(new Color(0x00B5AD));
      In_submitButton.setForeground(Color.white);
      
      ActionListener a1 = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 
        	 String reservation_code = In_CodeText.getText();
        	 inputReservationCode(reservation_code);
        	 In_CodeText.setText("");
        	 //jframe.dispose();
         }
      };
      
      In_submitButton.addActionListener(a1);
      
      final JFrame jframe= new JFrame("");
      
     // Container container = jframe.getContentPane();
      
      JPanel jpanel = new JPanel();
      jpanel.setBorder(new EmptyBorder(100, 200, 200, 200));
      
      Box outerBox = new Box(BoxLayout.Y_AXIS);
      Box innerBox1 = new Box(BoxLayout.X_AXIS);
      Box innerBox2 = new Box(BoxLayout.X_AXIS);
      Box innerBox3 = new Box(BoxLayout.X_AXIS);
      Box innerBox4 = new Box(BoxLayout.Y_AXIS);
      
      jframe.setContentPane(jpanel);
      jpanel.add(outerBox);
      
      outerBox.add(innerBox1);
      outerBox.add(innerBox2);
      outerBox.add(innerBox3);
      outerBox.add(innerBox4);
      
      innerBox1.add(sureparkIcon);
      innerBox1.add(In_Welcome);
      
      innerBox2.add(In_Code);  

      innerBox3.add(In_CodeText);
      innerBox3.add(In_submitButton);
      
      innerBox4.add(In_Status);
      innerBox4.add(In_StatusText);
      innerBox4.add(In_Name);
      innerBox4.add(In_NameText);
      innerBox4.add(In_ParkingSlot);
      innerBox4.add(In_ParkingSlotText);
      
      jpanel.setOpaque(true);
      jpanel.setBackground(new Color(0xffffff));
      
      jframe.setUndecorated(true);
      jframe.pack();
      jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
      jframe.setVisible(true);
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      while(true){
    	  if(isConnected()){
    		  In_submitButton.setEnabled(true);
    	  }else{
    		  In_submitButton.setEnabled(false);
    	  }
      }
      
   }
}

