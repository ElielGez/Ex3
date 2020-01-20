package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class represents a simple example of using MySQL Data-Base. Use this
 * example for writing solution.
 * 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser = "student";
	public static final String jdbcUserPassword = "OOP2020student";
	private static Connection connection = null;
	private static Statement statement = null;
	private static final int id = 999;

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(gamesPlayed());
	}

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

	public static int gamesPlayed() {
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

	/**
	 * simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "select * from Logs where userID = " + id + " order by score desc;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while (resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID") + "," + resultSet.getInt("levelID") + ","
						+ resultSet.getInt("moves") + "," + resultSet.getInt("score") + ","
						+ resultSet.getDate("time"));
			}
			resultSet.close();
			statement.close();
			connection.close();
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this function returns the KML string as stored in the database (userID,
	 * level);
	 * 
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID=" + id + ";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if (resultSet != null && resultSet.next()) {
				ans = resultSet.getString("kml_" + level);
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while (resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
}
