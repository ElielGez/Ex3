package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KML_Logger {
	private int stage;
	private StringBuilder content;
	public KML_Logger(int stage) {
		this.stage = stage;
		content = new StringBuilder();
		content.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"  <Document>\r\n" + 
				"    <name>stage: " + stage + " maze of waze" + "</name>" +
				"	 <Style id=\"node\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/pal3/icon49.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>" +
				"	 <Style id=\"fruit-banana\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/pal4/icon41.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>" +
				"	 <Style id=\"fruit-apple\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/pal4/icon40.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>" +
				"	 <Style id=\"robot\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/shapes/play.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"
				);
	}
	
	public void addPlaceMark(String type,String pos) {
			LocalDateTime now = LocalDateTime.now();  
			content.append(
					"    <Placemark>\r\n" + 
					"      <TimeStamp>\r\n" + 
					"        <when>" + now + "</when>\r\n" + 
					"      </TimeStamp>\r\n" + 
					"      <styleUrl>#" + type + "</styleUrl>\r\n" + 
					"      <Point>\r\n" + 
					"        <coordinates>" + pos + "</coordinates>\r\n" + 
					"      </Point>\r\n" + 
					"    </Placemark>\r\n"
					);
		
	}
	
	public void addNodePlaceMark(String pos) {
		addPlaceMark("node",pos);
	}
	
	public void closeDocument() {
		content.append(
				"  \r\n</Document>\r\n" + 
				"</kml>"
				);
		try 
		{
			PrintWriter pw = new PrintWriter(new File("data/" + this.stage + ".kml")); // change to save on data folder , and remove from git kmls
			pw.write(content.toString());
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return;
		}
	}
}
