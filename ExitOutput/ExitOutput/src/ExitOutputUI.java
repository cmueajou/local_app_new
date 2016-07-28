import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ExitOutputUI extends Thread{
 
   public static JTextField ID = new JTextField();
   public static JTextField Time = new JTextField();
   public static JTextField ParkingFee = new JTextField();

public void run() {
   
      JPanel jpanel = new JPanel();
      
      JLabel In_Bye = new JLabel("  Goodbye! Have a niceday");
      In_Bye.setFont(new Font("Arial", Font.PLAIN, 30));
      In_Bye.setForeground(new Color(0x00B5AD));
      
      JLabel User = new JLabel("Your ID:");
      User.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      User.setHorizontalAlignment(SwingConstants.CENTER);
      User.setBorder(new EmptyBorder(100, 0,10,10));
      
      JLabel totalTime = new JLabel("Total Spending Time:");
      totalTime.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      totalTime.setHorizontalAlignment(SwingConstants.CENTER);
      totalTime.setBorder(new EmptyBorder(10, 0,10,10));
     
      JLabel l_charge = new JLabel("Total Parking Fee:");
      l_charge.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      l_charge.setHorizontalAlignment(SwingConstants.CENTER);
      l_charge.setBorder(new EmptyBorder(10, 0,10,10));
     
      JLabel sureparkIcon = new JLabel(new ImageIcon("./resource/SureparkImage.png"));
      
      final JFrame jframe= new JFrame("");
      
      ID.setEditable(false);
      ID.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      ID.setBorder(new EmptyBorder(10,10,10,10));

      Time.setEditable(false);
      Time.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      Time.setBorder(new EmptyBorder(10,10,10,10));
      
      ParkingFee.setEditable(false);
      ParkingFee.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
      ParkingFee.setBorder(new EmptyBorder(10,10,10,10));
      
      jframe.setContentPane(jpanel);
      Box outerBox = new Box(BoxLayout.Y_AXIS);
      Box innerBox1 = new Box(BoxLayout.X_AXIS);
      Box innerBox2 = new Box(BoxLayout.Y_AXIS);
      
      jpanel.add(outerBox);
      outerBox.add(innerBox1);
      outerBox.add(innerBox2);
      
      innerBox1.add(sureparkIcon);
      innerBox1.add(In_Bye);
      
      innerBox2.add(User);
      innerBox2.add(ID);
      innerBox2.add(totalTime);
      innerBox2.add(Time);
      innerBox2.add(l_charge);
      innerBox2.add(ParkingFee);
       
      jpanel.setOpaque(true);
      jpanel.setBackground(new Color(0xffffff));
      jpanel.setBorder(new EmptyBorder(100, 30, 300, 30));
      
      jframe.setUndecorated(true);
      jframe.pack();
      jframe.setLocation(0,0);
      jframe.setExtendedState(JFrame.MAXIMIZED_VERT);
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jframe.setVisible(true);
   }
  
}



/*

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
      jframe.setLocationRelativeTo(null);
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


*/