# Ariel OOP - Ex2 The Maze of Waze

<p align="center">
  <img alt="graph" src="https://github.com/ElielGez/Ex2/blob/master/images_examples/Capture.JPG">
</p>

## Introduction:
This project is part of assignment in Ariel University.

Main purpose of the project is to:
* Create directed weighted graph
* Display the graph on graphic user interface , 
* Run algorithms on the graph.

## How to use GUI

* Create instance of GraphGUI class with width , height and graph object.

* Add by code nodes with Point3D (for location) and connect between edges , see examples below..

* Menu bar options:
	- File
		- Save: after drawing the graph , you can save it as txt file
		- Load: load the graph from txt file.
	- Algorithms
		- Shortest Path Distance: get the shortest path distance between 2 nodes (if path exist)
		- Shortest Path: get directions of the shortest path between 2 nodes (if path exist)
		- Graph connected?: check if the graph have strong conncetion
		- TSP: check if have path between targets of nodes
	- Node
		- Add node: For adding new node , just double click on the window to place new node
		- Remove node: insert key on node to remove
	- Edge
		- Add edge: insert src key , dest key and weight to connect with edge between nodes
		- Remove edge : insert src key and dest key to remove edge
	
## Examples:

#### Node Class:
```
Node n = new Node();
```

#### Edge Class:
```
int src = 1;
int dest = 2;
double weight = 5.3;
Edge e = new Edge(src,dest,weight);
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

#### GraphGUI Class:
```
DGraph g = new DGraph();
g.addNode(new Node(new Point3D(30, 500)));
g.addNode(new Node(new Point3D(270, 80)));
g.addNode(new Node(new Point3D(50, 100)));
g.addNode(new Node(new Point3D(250, 250)));
g.addNode(new Node(new Point3D(500, 250)));
g.addNode(new Node(new Point3D(450, 550)));
g.connect(1, 3, 14);
g.connect(1, 4, 9);
g.connect(1, 6, 7);
g.connect(3, 2, 9);
g.connect(3, 4, 2);
g.connect(4, 1, 2);
g.connect(4, 3, 2);
g.connect(4, 5, 11);
g.connect(4, 6, 10);
g.connect(5, 2, 6);
g.connect(6, 5, 15);
GraphGUI gui = new GraphGUI(1000, 1000, g);
```
### **NOTE: More details about classes and interfaces of the project can be found on Wiki**
