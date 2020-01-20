package Tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import gameClient.DBQueries;

public class DBQueriesTest {

	public static void main(String[] args) {
//		DBQueries.queryMetaData("select distinct levelID from Logs where userID = 999");
//		DBQueries.gameBestResults();
		HashMap<String, ResultSet> hp = DBQueries.myBestResults(999);
		for (Map.Entry<String, ResultSet> entry : hp.entrySet()) {
			try {
				System.out.println(entry.getKey() + " = " + entry.getValue().getInt("score"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
