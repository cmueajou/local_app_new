import java.io.IOException;

public class ReservationInputMain {
	 public static String resCode;
	 public static boolean inUI;
	 public static void main(String[] args) throws IOException {
		 ReservationInputServer s1 = new ReservationInputServer();
		 ReservationInputUI u1 = new ReservationInputUI();
		 s1.start();
		 u1.start();
	   }
}
