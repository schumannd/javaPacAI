import java.util.ArrayList;

public class Node{
	private int x;
	private int y;
	private boolean hasPill;
	private ArrayList<Edge> edges = new ArrayList<Edge>();

	Node(boolean hasPill){
		self.hasPill = hasPill;
	}
	public void connectTo(Node node, int weight){
		Edge e = new Edge(self, node, weight);
		Edge e2 = new Edge(node, self, weight);
		edges.add(e);
		node.getEdges.add(e2);
	}
	public ArrayList<Edge> getEdges(){
		return edges;
	}
}