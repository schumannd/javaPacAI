package pacman;

import java.util.ArrayList;

public class Node{
	int x;
	int y;
	private boolean hasPill;
	private ArrayList<Edge> edges = new ArrayList<Edge>();

	Node(boolean hasPill, int x, int y ){
		this.x = x;
		this.y = y;
		this.hasPill = hasPill;
	}
	public void connectTo(Node node, int weight){
		Edge e = new Edge(this, node, weight);
		Edge e2 = new Edge(node, this, weight);
		edges.add(e);
		node.getEdges().add(e2);
	}
	public ArrayList<Edge> getEdges(){
		return edges;
	}
	public boolean hasPill(){
		return hasPill;
	}
	public void setPill(boolean pill){
		hasPill = pill;
	}
}