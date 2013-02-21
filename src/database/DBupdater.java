package database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;

public class DBupdater {

	private Connection con;
	private ResultSet resultater;
	public static final String url = "jdbc:mysql://mysql.stud.ntnu.no";
	private static final String username = "rikardbe_bruker";
	private static final String password = "rikeid90";

	private static String ipAdresse;
	private static String kommentar;

	private long tidsIntervall = 60000;

	public void establishConnection(){
		try {
			con = (Connection) DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE rikardbe_ipadresser");

		} catch (Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public void updateIP(){
		setIP();
		updateDateComment();

		try {
			Statement statement = con.createStatement();
			statement.executeUpdate("INSERT INTO IPadresser(Adresse,Comment) VALUES(\""+ ipAdresse +"\",\"" + kommentar + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setIP(){
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		ipAdresse = ip.getHostAddress();
	}

	public Date getCurrentDate(){
		Date date = new Date();
		return date;
	}

	public void updateDateComment(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		kommentar = dateFormat.format(getCurrentDate());
	}

	//	24 hours in millisec: 86400000
	//	1 second: 1000ms
	//	1 minute: 600000

	public void timeTracker() {
		Timer timer = new Timer();
		int antReps = 0;

		TimerTask toDO = new TimerTask() {			
			@Override
			public void run() {
				try {
					establishConnection();
					updateIP();
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(toDO, getCurrentDate(), tidsIntervall);

	}

	public static void main(String[] args) throws UnknownHostException {
		DBupdater test = new DBupdater();
		test.timeTracker();
//		test.printTabell();
	}

	public void printTabell(){

		try {
			establishConnection();
			Statement stmt = con.createStatement();
			resultater = stmt.executeQuery("SELECT * FROM IPadresser");

			ResultSetMetaData metaData = (ResultSetMetaData) resultater.getMetaData();
			int rader = metaData.getColumnCount();


			for (int i = 1; i <= rader; i++) {
				String columnName = metaData.getColumnName(i);
				if(i > 1) System.out.print(" | ");
				System.out.print(columnName);

			}
			System.out.print("\n-------------------------------");

			while(resultater.next()){
				System.out.println();
				for (int i = 1; i <= rader; i++) {
					if(i > 1) System.out.print(" | ");
					String columnValue = resultater.getString(i);
					System.out.print(columnValue);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
