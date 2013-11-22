package pacman;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;

public class PacManAI{
	Random r = new Random();
	int pacX, pacY;
	int[] ghostX, ghostY;
	LevelGraph level;

	PacManAI(short leveldata[], int ghostNum){
		ghostX = new int[ghostNum];
		ghostY = new int[ghostNum];
		level = new LevelGraph(leveldata);
	}

	int[] translateDirections(int dir){
		int[] directions = new int[2];
		switch(dir){
			case 0: // left
				directions[0]=-1;
				directions[1]=0;
				break;
			case 1: // right
				directions[0]=1;
				directions[1]=0;
				break;
			case 2: // up
				directions[0]=0;
				directions[1]=-1;
				break;
			case 3: // down
				directions[0]=0;
				directions[1]=1;
				break;
		}
		return directions;
	}

	public int[] getMove(){
		int[] directions = runAwayFromGhostsCloserThan(5);
		// bad randomization Method
		if(directions[0] == 0 && directions[1] == 0)
			directions = runTowardsPillsCloserThan(20);
			// directions = translateDirections(r.nextInt(4));
		return directions;
	}

	public void eatPill(int x, int y){
		level.eatPill(x, y);
	}

	public void setGhostPositionAndSpeed(int[] ghostX, int[] ghostY, int[] ghostSpeed, int blocksize){
		for(int i = 0; i < this.ghostX.length; i++){
			this.ghostX[i] = ghostX[i]/blocksize;
			this.ghostY[i] = ghostY[i]/blocksize;
			// System.out.println("ghost "+i+": X="+this.ghostX[i]+" Y="+this.ghostY[i]);
		}
	}
	public void setPacmanPosition(int x, int y, int blocksize){
		pacX = x/blocksize;
		pacY = y/blocksize;
		// System.out.println("Pacman: X="+pacX+" Y="+pacY);
	}

	int[] nextPillBFS(Node start){
		ArrayList<Node> alreadyVisited = new ArrayList<Node>();
		Queue<Node> nextToVisit = new LinkedList<Node>();
		nextToVisit.add(start);
		Node lastTouched;
		while(true){
			lastTouched = nextToVisit.remove();
			if(alreadyVisited.contains(lastTouched))
				continue;
			if (lastTouched.hasPill()){
				break;
			}
			alreadyVisited.add(lastTouched);
			ArrayList<Edge> neighbours = lastTouched.getEdges();
			for(int i = 0; i < neighbours.size(); i++)
				nextToVisit.add(neighbours.get(i).end);
		}
		int distX = lastTouched.x - start.x;
		int distY  = lastTouched.y - start.y;
		return setRunningDirection(distX, distY);
	}

	int[] setRunningDirection(int distX, int distY){
		if(Math.abs(distX) > Math.abs(distY)){
			return new int[] {(int)Math.signum(distX), 0};
		}else{
			return new int[] {0, (int)Math.signum(distY)};
		}
	}

	int[] runTowardsPillsCloserThan(int searchDepth){
		Node current = level.getNodeAt(pacX, pacY);
		return nextPillBFS(current);
	}

	int[] runAwayFromGhostsCloserThan(int radarRange){
		int[] returnDirections = new int[2];
		int closestGhost = radarRange;
		for(int i = 0; i < ghostX.length; i++){
			int distX = pacX - ghostX[i];
			int distY = pacY - ghostY[i];
			int dist = Math.abs(distX) + Math.abs(distY);
			if(dist < closestGhost){
				closestGhost = dist;
				returnDirections = setRunningDirection(distX, distY);
			}
		}
		return returnDirections;
	}
}