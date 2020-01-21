package gameClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener {
	private GameClient gc;
	private GameAlgo ga;
	private int mc;

	private int MOVE_ROBOT_ID = -1;
	private int MOVE_TO_DEST = -1;

	private static final String GAME = "Game";
	private static final String MY_RESULTS = "My Results";
	private static final String STAGE_RESULTS = "Stage Results";
	private static final String GAME_RESULTS = "Game Results";
	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;

	/**
	 * Empty constructor
	 */
	public MyGameGUI() {
		gc = new GameClient();
		initGUI(WIDTH, HEIGHT);
	}

	/**
	 * Function to init new game by stage
	 * 
	 * @param stage
	 */
	private void initStage(int stage) {
		gc = new GameClient(stage);
		gc.setExportKMLOnEnd(false);
		this.ga = gc.getGameAlgo();
		this.mc = this.getG().getMC();
	}

	/**
	 * Getter for the graph
	 * 
	 * @return
	 */
	public graph getG() {
		if (this.gc == null)
			return null;
		return this.gc.getGraph();
	}

	/**
	 * Getter for mc
	 * 
	 * @return
	 */
	public int getMc() {
		return this.mc;
	}

	/**
	 * Function to init the gui object
	 * 
	 * @param width
	 * @param height
	 */
	private void initGUI(int width, int height) {
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.initMenuBar();
		this.initMcThread();

		chooseStagePopup();
	}

	/**
	 * Function to init menu bar of the window
	 */
	private void initMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("New");
		menuBar.add(menu1);
		Menu menu2 = new Menu("Results");
		menuBar.add(menu2);
		this.setMenuBar(menuBar);

		MenuItem game = new MenuItem(GAME);
		game.addActionListener(this);
		MenuItem my_results = new MenuItem(MY_RESULTS);
		my_results.addActionListener(this);
		MenuItem stage_results = new MenuItem(STAGE_RESULTS);
		stage_results.addActionListener(this);
		MenuItem game_results = new MenuItem(GAME_RESULTS);
		game_results.addActionListener(this);

		menu1.add(game);
		menu2.add(my_results);
		menu2.add(stage_results);
		menu2.add(game_results);

		this.addMouseListener(this);

	}

	/**
	 * Function to draw the graph on the JFrame
	 */
	public void paint(Graphics g) {
		// super.paint(g);
		try {
			if (this.getG() != null) {
				BufferedImage bufferedImage = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				g2d.setBackground(new Color(240, 240, 240));
				g2d.clearRect(0, 0, this.WIDTH, this.HEIGHT);
				// paint using g2d ...
				this.drawGameInfo(g2d);
				this.drawGraph(g2d);
				this.drawFruits(g2d);
				this.drawRobots(g2d);
				Graphics2D g2dComponent = (Graphics2D) g;
				g2dComponent.drawImage(bufferedImage, null, 0, 0);
			}
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Function to init mc thread This thread is listening to changes on the graph
	 * (by mc value)
	 */
	private void initMcThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (gc) {
						if (gc.getGraph() != null && gc.getGraph().getMC() != mc) {
							mc = gc.getGraph().getMC();
							repaint();
						}
					}
				}
			}
		});
		t.start();
	}

	/**
	 * Function that draw the graph with his nodes and edges
	 * 
	 * @param g1
	 */
	private void drawGraph(Graphics2D g) {

		g.setFont(new Font("Arial", 1, 15));
		for (node_data src : this.getG().getV()) {
			Point3D pSrc = src.getGuiLocation();
			g.setColor(Color.BLUE);
			Shape circle = new Arc2D.Double(pSrc.x() - 5, pSrc.y() - 5, 12, 12, 0, 360, Arc2D.CHORD);
			g.fill(circle);
			g.drawString("" + src.getKey(), pSrc.ix(), pSrc.iy() - 5);

			Collection<edge_data> e = this.getG().getE(src.getKey());
			if (e != null) {
				for (edge_data edge : e) {
					node_data dest = this.getG().getNode(edge.getDest());
					if (dest != null) {
						Point3D pDest = dest.getGuiLocation();
						g.setColor(Color.DARK_GRAY);
						g.drawLine(pSrc.ix(), pSrc.iy(), pDest.ix(), pDest.iy());

						int centerX = (pSrc.ix() + pDest.ix()) / 2;
						int centerY = (pSrc.iy() + pDest.iy()) / 2;
						for (int i = 0; i < 2; i++) {
							centerX = (centerX + pSrc.ix()) / 2;
							centerY = (centerY + pSrc.iy()) / 2;
						}
						g.setColor(Color.RED);
						g.fillOval(centerX - 3, centerY, 5, 5);
					}
				}
			}
		}

	}

	/**
	 * Function to draw fruits
	 * 
	 * @param g
	 */
	private void drawFruits(Graphics2D g) {
		synchronized (ga.fruitList()) {
			for (Fruit f : ga.fruitList()) {
				Point3D pFruit = f.getGuiLocation();
				if (pFruit != null) {
					int type = f.getType();
					if (type == -1)
						g.setColor(new Color(255, 196, 30)); // banana
					if (type == 1)
						g.setColor(Color.GREEN); // apple
					Shape fruitCircle = new Arc2D.Double(pFruit.x() - 9, pFruit.y() - 5, 20, 20, 0, 360, Arc2D.CHORD);
					g.fill(fruitCircle);
					g.setColor(Color.DARK_GRAY);
					g.drawString("" + f.getValue(), pFruit.ix(), pFruit.iy());
				}
			}
		}
	}

	/**
	 * Function to draw robots
	 * 
	 * @param g
	 */
	private void drawRobots(Graphics2D g) {
		synchronized (ga.robotList()) {
			for (Robot r : ga.robotList()) {
				BufferedImage img;
				try {
					img = ImageIO.read(new File("./images/robot.png"));
					Point3D pRobot = r.getGuiLocation();
					if (pRobot != null) {
						g.drawImage(img, pRobot.ix() - 15, pRobot.iy() - 15, null);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * Function to draw game info like time , score and game over
	 * 
	 * @param g
	 */
	private void drawGameInfo(Graphics2D g) {

		g.setColor(Color.RED);
		g.setFont(new Font("Arial", 1, 15));
		List<String> rob = gc.getGameRobots();
		for (int i = 0; i < rob.size(); i++) {
			g.drawString(rob.get(i), 130, 120 + (20 * i));
		}

		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", 1, 17));
		if (gc.getGame().isRunning())
			g.drawString("Time left: " + gc.getGameClock(), 30, 90);
		g.drawString("Score: " + gc.getGameGrade(), 900, 90);
		if (!gc.getGame().isRunning() && gc.getGameGrade() != 0) { // game over
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Game Over!", 300, 500);
			exportKML();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		moveRobotByClicking(e);
	}

	/**
	 * Function to move robot by clicking the nodes and robots 2 clicks - choose
	 * robot 1 click - tell the robot to move this node
	 * 
	 * @param e
	 */
	private void moveRobotByClicking(MouseEvent e) {
		if (gc.IsManual()) {
			int x = e.getX();
			int y = e.getY();
			Point3D p = new Point3D(x, y);
			if (e.getClickCount() == 1) {
				node_data n = findNodeByLocation(p);
				if (n != null) {
					MOVE_TO_DEST = n.getKey();
					try {
						ga.moveRobot(MOVE_ROBOT_ID, MOVE_TO_DEST, gc.getGame(), getG());
						MOVE_TO_DEST = -1;
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
			if (e.getClickCount() == 2) {
				Robot r = findRobotByLocation(p);
				if (r != null) {
					MOVE_ROBOT_ID = r.getId();
				}
			}
		}
	}

	/**
	 * Function to find node by location
	 * 
	 * @param p
	 * @return
	 */
	private node_data findNodeByLocation(Point3D p) {
		for (node_data node : gc.getGraph().getV()) {
			double distance = node.getGuiLocation().distance2D(p);
			if (distance >= 0 && distance <= 15)
				return node;
		}
		return null;
	}

	/**
	 * Function to find robot by location
	 * 
	 * @param p
	 * @return
	 */
	private Robot findRobotByLocation(Point3D p) {
		for (Robot robot : ga.robotList()) {
			double distance = robot.getGuiLocation().distance2D(p);
			if (distance >= 0 && distance <= 25)
				return robot;
		}
		return null;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Override action listener function , to listen the menu clicks
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		String id_str = "";
		int id = 999;
		switch (str) {
		case GAME: {
			chooseStagePopup();
			break;
		}
		case MY_RESULTS: {
			id_str = JOptionPane.showInputDialog(this, "Please insert id");
			try {
				id = Integer.parseInt(id_str);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			showMyResults(id);
			break;
		}
		case STAGE_RESULTS: {
			id_str = JOptionPane.showInputDialog(this, "Please insert id");
			try {
				id = Integer.parseInt(id_str);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			showStageResults(id, gc.getStage());
			break;
		}
		case GAME_RESULTS: {
			showAllResults();
			break;
		}
		}

	}

	/**
	 * Function that ask from user to enter stage , this function repaint the graph
	 * with the selected stage After that , create thread of game client , and call
	 * thread.start to start game
	 */
	private void chooseStagePopup() {
		String s = JOptionPane.showInputDialog("Please insert number of stage between 0-23");
		int stage;
		try {
			stage = Integer.parseInt(s);
			this.initStage(stage);
			repaint();
			int reply = JOptionPane.showConfirmDialog(null, "You want to run manually?", "Choose mode",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				gc.setIsManual(true);
			} else {
				gc.setIsManual(false);
			}
			Thread t = new Thread(gc);
			t.start();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function to choose if you want to export kml when game over or not
	 */
	private void exportKML() {
		try {
			int reply = JOptionPane.showConfirmDialog(null, "You want to export KML log?", "Export kml log",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				gc.getKMLog().closeDocument();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showMyResults(int id) {
		String[] columnNames = { "UserID", "LevelID", "score", "moves", "time" };
		JFrame frame1 = new JFrame("My Game Results, Games Played: " + DBQueries.gamesPlayed(id));
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.setLayout(new BorderLayout());
		DefaultTableModel tableModel = new DefaultTableModel();
		for (String columnName : columnNames) {
			tableModel.addColumn(columnName);
		}
		TreeMap<Integer, String> tp = DBQueries.myBestResults(id);
		for (Map.Entry<Integer, String> entry : tp.entrySet()) {
			tableModel.addRow(entry.getValue().split(","));
		}

		JTable table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame1.add(scroll);
		frame1.setSize(600, 400);
		frame1.setVisible(true);
	}

	private void showStageResults(int id, int stage) {
		String[] columnNames = { "Rank", "UserID", "LevelID", "score", "moves", "time" };
		JFrame frame1 = new JFrame("Stage: " + stage + " Results");
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.setLayout(new BorderLayout());
		DefaultTableModel tableModel = new DefaultTableModel();
		for (String columnName : columnNames) {
			tableModel.addColumn(columnName);
		}
		LinkedHashMap<String, String> tp = DBQueries.stageBestResults(stage, ExpectedResults.moves[stage]);
		int rank = 1;
		for (Map.Entry<String, String> entry : tp.entrySet()) {
			Vector<String> v1 = new Vector<String>();
			v1.add("" + rank);
			Vector<String> v2 = new Vector<String>(Arrays.asList(entry.getValue().split(",")));
			v1.addAll(v2);
			tableModel.addRow(v1);
			rank++;
		}

		JTable table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame1.add(scroll);
		frame1.setSize(400, 300);
		frame1.setVisible(true);
	}

	private void showAllResults() {
		String[] columnNames = { "UserID", "LevelID", "score", "moves", "time" };
		JFrame frame1 = new JFrame("Game Results");
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.setLayout(new BorderLayout());
		DefaultTableModel tableModel = new DefaultTableModel();
		for (String columnName : columnNames) {
			tableModel.addColumn(columnName);
		}
		TreeMap<String, String> tp = DBQueries.gameBestResults();
		for (Map.Entry<String, String> entry : tp.entrySet()) {
			tableModel.addRow(entry.getValue().split(","));
		}

		JTable table = new JTable(tableModel);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame1.add(scroll);
		frame1.setSize(400, 300);
		frame1.setVisible(true);
	}

}
