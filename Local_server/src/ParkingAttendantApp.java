import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ParkingAttendantApp implements Runnable {
	protected static final JFrame jframe = new JFrame("Parking-Attendant-App");
	protected static JPanel PAUI_p2_parkingLot_space[] = new JPanel[4];
	protected static JLabel PAUI_p3_popup = new JLabel("");
	protected static JPanel PAUI_p2_Info_gatestate_gate[] = new JPanel[2];
	protected static JLabel PAUI_p2_info_carNum = new JLabel("Total Car: ");

	public int id = -1;
	protected JTextField AccountText;
	protected JTextField PINText;

	protected JLabel AccountLabel;
	protected JLabel PINLabel;
	protected JLabel Title;

	protected String ID = "admin";
	protected String Pin = "1234";

	String parking_status = "0000";
	String parking_reserve_status = "0000";
	String broadcast = "";
	BlockingQueue queue;

	public ParkingAttendantApp(int id, BlockingQueue _queue) {
		this.id = id;
		this.queue = _queue;

	}

	public void run() {

		jframe.setLayout(new BorderLayout());

		final JPanel LoginPanel = new JPanel();

		
		Title = new JLabel("   Management");
		Title.setFont(new Font(Font.DIALOG, Font.BOLD, 60));
		Title.setForeground(new Color(0x00B5AD));
		JLabel sureparkIcon = new JLabel(new ImageIcon("./resource/SureparkImage.png"));
		  
		AccountText = new JTextField(5);
		AccountText.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
		PINText = new JPasswordField(5);
		PINText.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
		JLabel Blank = new JLabel("-----");
		Blank.setForeground(Color.WHITE);
		Blank.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		AccountLabel = new JLabel("Account number:");
		AccountLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
		PINLabel = new JLabel("PIN number:");
		PINLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
		AccountLabel.setBorder(new EmptyBorder(100, 10, 10, 10));
		JButton App_submitButton = new JButton("Submit");
		App_submitButton.setForeground(new Color(0xffffff));
		App_submitButton.setBackground(new Color(0x00B5AD));
		App_submitButton.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
	
		
		Box outterBox = new Box(BoxLayout.Y_AXIS);
		Box box2 = new Box(BoxLayout.X_AXIS);
		Box box1 = new Box(BoxLayout.Y_AXIS);
		LoginPanel.setBorder(new EmptyBorder(100, 50, 300, 50));
		LoginPanel.setOpaque(true);
	    LoginPanel.setBackground(new Color(0xffffff));  
		LoginPanel.add(outterBox);
		outterBox.add(box2);
		box2.add(sureparkIcon);
		box2.add(Title);
		outterBox.add(box1);
		box1.add(AccountLabel);
		box1.add(AccountText);
		box1.add(PINLabel);
		box1.add(PINText);
		box1.add(Blank);
		box1.add(App_submitButton);
		
		jframe.add(LoginPanel);
		
		jframe.pack();
		jframe.setSize(800, 900);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);

		ActionListener a1 = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (AccountText.getText().equals(ID) && PINText.getText().equals(Pin)) {
					jframe.remove(LoginPanel);
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");

					JLabel time = new JLabel("Time :" + df.format(calendar.getTime()));
					;

					PAUI UI = new PAUI(id, queue, time);
					jframe.revalidate();
					jframe.repaint();

				}

				else {
					JOptionPane.showMessageDialog(null, String.format("Wrong Id or pin number"));
				}
			}

		};

		App_submitButton.addActionListener(a1);

		/*
		 * cp.setLayout(new FlowLayout());
		 * 
		 * JPanel ParkingPanel = new JPanel();
		 * ParkingPanel.setBackground(Color.green); JLabel ParkingLabel = new
		 * JLabel("Parking"); ParkingPanel.add(ParkingLabel);
		 * 
		 * JPanel emptyPanel = new JPanel();
		 * emptyPanel.setBackground(Color.red); JLabel emptyLabel = new
		 * JLabel("empty"); emptyPanel.add(emptyLabel);
		 * 
		 * 
		 * 
		 * cp.add(ParkingPanel); cp.add(emptyPanel);
		 */

	}

	public void changeParkinglotColor(int no, int state) {
		if (state == 2)
			PAUI_p2_parkingLot_space[no].setBackground(Color.red); // 점유
		else if (state == 1)
			PAUI_p2_parkingLot_space[no].setBackground(Color.blue); // 예약
		if (state == 0)
			PAUI_p2_parkingLot_space[no].setBackground(Color.green); // 빈공간
	}

	public void popUpMeassage(String popUp) {
		PAUI_p3_popup.setText(popUp);
	}

	public void chageGateState(int inORout, int openclose) {
		PAUI_p2_Info_gatestate_gate[inORout].setBackground(Color.RED);
	}

	public void currentCarNum(int TotalCar) {
		PAUI_p2_info_carNum.setText("Total Car: " + Integer.toString(TotalCar));
	}

}
