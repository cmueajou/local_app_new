import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import javax.swing.JLabel;

public class ParkingEvent extends Thread {
	ParkingAttendantApp app;
	Ingate_server s1;
	BlockingQueue queue;
	Ingate_server ingate;
	CentralServer central_server;

	String parking_status;
	Database db;

	ParkingEvent(ParkingAttendantApp _app, BlockingQueue _queue, Ingate_server _ingate, CentralServer _central_server) {
		this.app = _app;
		this.queue = _queue;
		this.ingate = _ingate;
		this.central_server = _central_server;
		db = new Database("localhost", "root", "1234");

	}

	public void run() {
		while (true) {

			String query = "Update sure_park.parking_spot_info set " + "`" + "RESERVATION_STATE" + "`" + "=" + "'"
					+ app.parking_reserve_status + "'" + "," + "`" + "PARKING_LOT_STATE" + "`" + "=" + "'"
					+ app.parking_status + "'";
			try {

				String Command_arbitor = "";
				if (queue.isEmpty() == false) {
					Command_arbitor = (String) queue.take();

					if (Command_arbitor.charAt(0) == '1') { // 11000
						System.out.println("1 Command_arbitor : " + Command_arbitor);
						char[] rec_data = Command_arbitor.substring(2).toCharArray();// 최신
						char[] parking_status_buff = this.parking_status.toCharArray(); 
						for (int i = 0; i < this.parking_status.length(); i++) {
							if (parking_status_buff[i] == '0' && rec_data[i] == '1') {
								System.out.println("change parking lot color : red");
								app.changeParkinglotColor(i, 2);
								
								break;
							}
							

						}
						this.parking_status = new String(rec_data,0,rec_data.length);	// parking_state

					} else if (Command_arbitor.charAt(0) == '2') {
						System.out.println("2 Command_arbitor : " + Command_arbitor);
						app.broadcast = Command_arbitor.substring(1);
						app.popUpMeassage(app.broadcast);
					} else if (Command_arbitor.charAt(0) == '3') { // Release
						System.out.println("3 Command_arbitor: " + Command_arbitor);
						char[] rec_data = Command_arbitor.substring(2).toCharArray();// 최신
																						// parking
																						// status
						System.out.println("3 rec_data : " + rec_data[0] + rec_data[1] + rec_data[2] + rec_data[3]);
						char[] parking_state_buff = this.parking_status.toCharArray(); // 기존의
																						// parking
																						// status
						System.out.println("3 parking status : "+ this.parking_status);

						for (int i = 0; i < this.parking_status.length(); i++) {
							if (parking_state_buff[i] == '1' && rec_data[i] == '0') { // red
								parking_state_buff[i] = rec_data[i];
								System.out.println("changeparkinglotcolor : green");
								app.changeParkinglotColor(i, 0);
								app.parking_status = new String(parking_state_buff, 0, parking_state_buff.length);
								break;
							}
						}

					}

					else {

					}
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
