import java.io.IOException;

public class ExitOutputMain {
	 public static String charge;
	 public static String currentReleaseSlot;
	 public static boolean inUI;
	 public static void main(String[] args) throws IOException {
		 ExitOutputServer c1 = new ExitOutputServer();
		 ExitOutputUI u1 = new ExitOutputUI();
		 c1.start();
		 u1.start();
	   }
}
