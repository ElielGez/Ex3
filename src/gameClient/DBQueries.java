package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.ResultSetMetaData;

/**
 * This class represents a simple example of using MySQL Data-Base. Use this
 * example for writing solution.
 * 
 * @author boaz.benmoshe
 *
 */
public class DBQueries {
	public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser = "student";
	public static final String jdbcUserPassword = "OOP2020student";
	private static Connection connection = null;
	private static Statement statement = null;

	private static ResultSet doQuery(String query) {
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static void queryMetaData(String query) {
		ResultSet resultSet = doQuery(query);
		try {
			ResultSetMetaData rsmd = (ResultSetMetaData) resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();

			// The column count starts from 1
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				System.out.println(name);
				// Do stuff with name
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		closeQuery(resultSet);
	}

	private static void closeQuery(ResultSet resultSet) {
		try {
			resultSet.close();
			connection.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int gamesPlayed(int id) {
		ResultSet resultSet = doQuery("select * from Logs where userID = " + id);
		int count = 0;
		try {
			while (resultSet.next()) {
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeQuery(resultSet);
		return count;
	}

	public static HashMap<String, ResultSet> myBestResults(int id) {
		String query = "SELECT * FROM Logs as logs inner join (" + "SELECT max(score) as score, levelID FROM Logs"
				+ " where userID = " + id + " group by levelID" + ") as groupedLogs"
				+ " on logs.levelID = groupedLogs.levelID and logs.score = groupedLogs.score" + " where userID = " + id
				+ " order by logs.levelID desc";
		ResultSet resultSet = doQuery(query);
		HashMap<String, ResultSet> hp = new HashMap<String, ResultSet>();
		try {

			while (resultSet.next()) {
				hp.put(resultSet.getInt("levelID") + "," + resultSet.getInt("userID"), resultSet);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeQuery(resultSet);
		return hp;
	}

	public static HashMap<String, ResultSet> gameBestResults() {
		String query = "SELECT * FROM Logs as logs inner join ("
				+ "SELECT max(score) as score, levelID, userID FROM Logs" + "	group by levelID,userID"
				+ ") as groupedLogs"
				+ " on logs.userID = groupedLogs.userID and logs.levelID = groupedLogs.levelID and logs.score = groupedLogs.score"
				+ " order by logs.userID desc";
		ResultSet resultSet = doQuery(query);
		HashMap<String, ResultSet> hp = new HashMap<String, ResultSet>();
		try {

			while (resultSet.next()) {
				hp.put(resultSet.getInt("levelID") + "," + resultSet.getInt("userID"), resultSet);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeQuery(resultSet);
		return hp;
	}

	/**
	 * this function returns the KML string as stored in the database (userID,
	 * level);
	 * 
	 * @param id
	 * @param level
	 * @return
	 */
//	public static String getKML(int id, int level) {
//		String ans = null;
//		String allCustomersQuery = "SELECT * FROM Users where userID=" + id + ";";
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
//			if (resultSet != null && resultSet.next()) {
//				ans = resultSet.getString("kml_" + level);
//			}
//		} catch (SQLException sqle) {
//			System.out.println("SQLException: " + sqle.getMessage());
//			System.out.println("Vendor Error: " + sqle.getErrorCode());
//		}
//
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return ans;
//	}
//
//	public static int allUsers() {
//		int ans = 0;
//		String allCustomersQuery = "SELECT * FROM Users;";
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
//			while (resultSet.next()) {
//				System.out.println("Id: " + resultSet.getInt("UserID"));
//				ans++;
//			}
//			resultSet.close();
//			statement.close();
//			connection.close();
//		} catch (SQLException sqle) {
//			System.out.println("SQLException: " + sqle.getMessage());
//			System.out.println("Vendor Error: " + sqle.getErrorCode());
//		}
//
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return ans;
//	}
}
