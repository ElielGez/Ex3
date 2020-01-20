package Tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import gameClient.DBQueries;

public class DBQueriesTest {

	public static void main(String[] args) {
//		DBQueries.queryMetaData("select distinct levelID from Logs where userID = 999");
//		TreeMap<String, String> tp = DBQueries.gameBestResults();

		TreeMap<Integer, String> tp = DBQueries.myBestResults(316519966);
		for (Map.Entry<Integer, String> entry : tp.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

	}

}
