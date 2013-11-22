package pacman;

import java.lang.Math;
import java.util.ArrayList;

public class LevelGraph{
	private int dimension;
	private ArrayList<Node> nodes = new ArrayList<Node>();

	LevelGraph(short leveldata[]){
		dimension = (int)Math.sqrt(leveldata.length);
		createGraph(leveldata);
	}

	void createGraph(short leveldata[]){
		for(int i = 0; i < leveldata.length; i++){
			// add node with Pill if value is over 15
			Node current = new Node(leveldata[i] > 15, i%dimension, i/dimension);
			nodes.add(current);
			addEdges(i, leveldata, dimension, current);
		}
	}

	void addEdges(int i, short leveldata[], int dimension, Node current){
		if(i % dimension != 0 && leveldata[i-1] > 15){
			current.connectTo(nodes.get(i-1), 0);
		}
		if(i >= dimension && leveldata[i-dimension] > 15){
			current.connectTo(nodes.get(i-dimension), 0);
		}
	}

	void eatPill(int x, int y){
		getNodeAt(x, y).setPill(false);
	}

	Node getNodeAt(int x, int y){
		return nodes.get(x + y*dimension);
	}
	ArrayList<Node> getNodes(){
		return nodes;
	}
}