import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * This version of the RemoteControl example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */

public class RemoteControl {
	static OutputStream serialOut;

	public RemoteControl() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				serialOut = out;
				(new Thread(new SerialWriter(out))).start();

				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				System.out.print(new String(buffer, 0, len));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	@SuppressWarnings("serial")
	static class ControlFrame extends JFrame implements ActionListener {
		JPanel jpanel = new JPanel();
		JButton bt_entryopen = new JButton("Entry Gate Open");
		JButton bt_entryclose = new JButton("Entry Gate Close");
		JButton bt_exitopen = new JButton("Exit Gate Open");
		JButton bt_exitclose = new JButton("Exit Gate Close");
		JButton bt_emeron = new JButton("Emergency Mode On");
		JButton bt_emeroff = new JButton("Emergency Mode Off");
		Box box_mode = new Box(BoxLayout.X_AXIS);
		JLabel label_mode = new JLabel("Gate Controller - Normal Mode");
		boolean flag_emergency = false;

		public ControlFrame() {
			super("RemoteController");
			
			box_mode.add(label_mode);	
			
			this.add(box_mode, BorderLayout.NORTH);
			this.add(jpanel);
			jpanel.add(bt_emeron);
			jpanel.add(bt_emeroff);
			jpanel.add(bt_entryopen);
			jpanel.add(bt_entryclose);
			jpanel.add(bt_exitopen);
			jpanel.add(bt_exitclose);
			
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			 
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			jpanel.setLayout(new GridLayout(3,2));

			label_mode.setBackground(Color.GREEN);
			label_mode.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
			
			bt_entryopen.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_entryopen.setBackground(Color.WHITE);
			bt_entryopen.setEnabled(false);
			bt_entryopen.addActionListener(this);
			
			bt_entryclose.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_entryclose.setBackground(Color.WHITE);
			bt_entryclose.setEnabled(false);
			bt_entryclose.addActionListener(this);
			
			bt_exitopen.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_exitopen.setBackground(Color.WHITE);
			bt_exitopen.setEnabled(false);
			bt_exitopen.addActionListener(this);
			
			bt_exitclose.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_exitclose.setBackground(Color.WHITE);
			bt_exitclose.setEnabled(false);
			bt_exitclose.addActionListener(this);
			
			bt_emeron.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_emeron.setBackground(Color.RED);
			bt_emeron.addActionListener(this);
			
			bt_emeroff.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 50));
			bt_emeroff.setBackground(Color.GREEN);
			bt_emeroff.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (flag_emergency) {
				if (e.getSource().equals(bt_entryopen)) {
					try {
						serialOut.write('1');
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getSource().equals(bt_entryclose)) {
					try {
						serialOut.write('2');
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getSource().equals(bt_exitopen)) {
					try {
						serialOut.write('3');
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getSource().equals(bt_exitclose)) {
					try {
						serialOut.write('4');
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			if(e.getSource().equals(bt_emeron)){
				try {
					flag_emergency = true;
					bt_entryopen.setEnabled(true);
					bt_entryclose.setEnabled(true);
					bt_exitopen.setEnabled(true);
					bt_exitclose.setEnabled(true);
					label_mode.setText("Gate Controller - Emergency Mode");
					serialOut.write('5');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource().equals(bt_emeroff)){
				try {
					flag_emergency = true;
					bt_entryopen.setEnabled(false);
					bt_entryclose.setEnabled(false);
					bt_exitopen.setEnabled(false);
					bt_exitclose.setEnabled(false);
					label_mode.setText("Gate Controller - Normal Mode");
					serialOut.write('6');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ControlFrame f = new ControlFrame();
		try {
			(new RemoteControl()).connect("COM6");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}