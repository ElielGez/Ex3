# Ariel OOP - Ex3 The Maze of Waze

<p align="center">
  <img alt="graph" src="https://github.com/ElielGez/Ex3/blob/master/images/Capture.JPG">
</p>

## Introduction:
This project is part of assignment in Ariel University.

Main purpose of the project is to:
* Create user interface to GameServer , the game is to collect fruits with robots .
* Create KML log in order to see the process of the game in google earth.

## How to play the game:

* The game have 2 options of running : manually or automatically .
	- Manually:
		1. Create instance of MyGameGUI.
		2. Choose stage.
		3. And click yes when you asked to play manually.
		4. Now the game is running , in order to move robots for next node , double click on the robot you want to move .
		Then one click on the next node you want to go (must be edge between them)
	- Automatically (GUI):
		1. Create instance of MyGameGUI.
		2. Choose stage.
		3. And click no when you asked to play manually.
		4. Now the game is running , and the robots move by themself with auto algorithm.
	- Automatically (Without GUI):
		1. Create instance of GameClient and insert stage to the constructor
		2. Then create thread and insert GameClient instance , and do t.start();
		3. Now the game is running , and the robots move by themself with auto algorithm.
		4. Score will be on the console.	

## How to use GUI

* Menu bar options:
	- New
		- Game: Create new game and choose stage.
	
## Examples:

#### Fruit Class:
```
String json = "{"Fruit":{ "pos":"","value":"", "type":""}}";
Fruit f = new Fruit(json);
```

#### Robot Class:
```
String json = "{"Robot":{"pos":"","value":"","id":"","src":"","dest":"","speed":""}}";
Robot r = new Robot(json);
```

#### DGraph Class:
```
DGraph d = new DGraph();
d.addNode(new Node()); // key 1
d.addNode(new Node()); // key 2
int src = 1;
int dest = 2;
double weight = 5.3;
d.connect(src,dest,weight);
```

#### Graph_Algo Class:
```
graph g = new DGraph(d); // copy d graph
Graph_Algo ga = new Graph_Algo();
ga.init(g); // init the graph

double spd = ga.shortestPathDist(1,2);

List<node_data> l = ga.shortestPath(1,2);

boolean flag = ga.isConnected();

LinkedList<Integer> targets = new LinkedList<Integer>();
targets.add(1);
targets.add(4);
targets.add(3);
targets.add(2);
List<node_data> l2 = ga.TSP(targets);
```

#### MyGameGUI Class:
```
MyGameGUI gui = new MyGameGUI();
```

#### GameClient Class:
```
GameClient gc = new GameClient(0); // stage between 0-23
Thread t = new Thread(gc);
t.start();
```

#### KML_Logger Class:
```
KML_Logger log = new KML_Logger("name");
log.addNodePlaceMark("pos");
log.addRobotPlaceMark("pos");
log.addFruitPlaceMark("banana or apple","pos");
log.closeDocument(); // close the document string and save file in data folder
```

### **NOTE: More details about classes and interfaces of the project can be found on Wiki**
