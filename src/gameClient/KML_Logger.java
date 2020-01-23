package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class KML_Logger {
	private String name;
	private StringBuilder content;

	private static final String NODE_STYLE_ID = "node";
	private static final String BANANA_STYLE_ID = "fruit-banana";
	private static final String APPLE_STYLE_ID = "fruit-apple";
	private static final String ROBOT_STYLE_ID = "robot";

	/**
	 * Constructor to create the begining of the kml file
	 * 
	 * @param name
	 */
	public KML_Logger(String name) {
		this.name = name;
		content = new StringBuilder();
		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		content.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n");
		content.append("  <Document>\r\n");
		content.append("    <name>stage: " + name + " maze of waze" + "</name>\r\n");
		content.append("	 <Style id=\"" + NODE_STYLE_ID + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append(
				"          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + BANANA_STYLE_ID + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + APPLE_STYLE_ID + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/paddle/red-circle.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + ROBOT_STYLE_ID + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/pal4/icon62.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>\r\n");
	}

	/**
	 * Function to add place mark by type (style id)
	 * 
	 * @param type
	 * @param pos
	 */
	private void addPlaceMark(String type, String pos) {
		LocalDateTime now = LocalDateTime.now();
		content.append("    <Placemark>\r\n");
		if (type != NODE_STYLE_ID) {
			content.append("      <TimeStamp>\r\n");
			content.append("        <when>" + now + "</when>\r\n");
			content.append("      </TimeStamp>\r\n");
		}
		content.append("      <styleUrl>#" + type + "</styleUrl>\r\n");
		content.append("      <Point>\r\n");
		content.append("        <coordinates>" + pos + "</coordinates>\r\n");
		content.append("      </Point>\r\n");
		content.append("    </Placemark>\r\n");

	}

	/**
	 * Function to create node place mark
	 * 
	 * @param pos
	 */
	public void addNodePlaceMark(String pos) {
		addPlaceMark(NODE_STYLE_ID, pos);
	}

	/**
	 * Function to create robot place mark
	 * 
	 * @param pos
	 */
	public void addRobotPlaceMark(String pos) {
		addPlaceMark(ROBOT_STYLE_ID, pos);
	}

	/**
	 * Function to create fruit place mark , banana or apple
	 * 
	 * @param fruit
	 * @param pos
	 */
	public void addFruitPlaceMark(String fruit, String pos) {
		addPlaceMark(fruit == "apple" ? APPLE_STYLE_ID : BANANA_STYLE_ID, pos);
	}

	/**
	 * Function to add edges on kml file
	 * @param posSrc
	 * @param posDest
	 */
	public void addEdgePlacemark(String posSrc, String posDest) {
		content.append("    <Placemark>\r\n");
		content.append("      <LineString>\r\n");
		content.append("      <extrude>5</extrude>\r\n");
		content.append("      <altitudeMode>clampToGround</altitudeMode>\r\n");
		content.append("      <coordinates>" + posSrc + "\r\n");
		content.append("      " + posDest + "</coordinates>\r\n");
		content.append("      </LineString>\r\n");
		content.append("    </Placemark>\r\n");
	}

	/**
	 * Function to close kml file and save it on data folder
	 */
	public void closeDocument() {
		content.append("  </Document>\r\n");
		content.append("</kml>");
		try {
			PrintWriter pw = new PrintWriter(new File("data/" + name + ".kml")); // change to save on data folder
																					// , and remove from git kmls
			pw.write(content.toString());
			pw.close();
		} catch (FileNotFoundException e) {
		}
	}

	public String getContent() {
		return content.toString();
	}
}
