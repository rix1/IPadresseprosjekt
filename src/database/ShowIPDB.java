package database;

import java.awt.BorderLayout;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;

public class ShowIPDB extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection con;
	private ResultSet resultater;
	public static final String url = "jdbc:mysql://mysql.stud.ntnu.no";
	private static final String username = "KLOVN";
	private static final String password = "DUST";
	private JPanel panel;
	private JScrollPane scroller;
	
	
	public void establishConnection(){
		try {
			con = (Connection) DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE rikardbe_ipadresser");

		} catch (Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
	
	
	public void printTabell(){
		
		JFrame frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JTextArea textOmr = new JTextArea(50, 25);
		
		scroller = new JScrollPane(textOmr);
		
		panel.add(scroller);
		
		try {
			establishConnection();
			Statement stmt = con.createStatement();
			resultater = stmt.executeQuery("SELECT * FROM IPadresser");

			ResultSetMetaData metaData = (ResultSetMetaData) resultater.getMetaData();
			int rader = metaData.getColumnCount();


			for (int i = 1; i <= rader; i++) {
				String columnName = metaData.getColumnName(i);
				if(i > 1) textOmr.append(" | ");
				textOmr.append(columnName);

			}
			textOmr.append("\n-------------------------------");

			while(resultater.next()){
				textOmr.append("\n");
				for (int i = 1; i <= rader; i++) {
					if(i > 1) textOmr.append(" | ");
					String columnValue = resultater.getString(i);
					textOmr.append(columnValue);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		textOmr.setEditable(false);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		ShowIPDB print = new ShowIPDB();
		print.printTabell();
	}

}
