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
		int[] directions = runAwayFromGhostsCloserThan(3);
		// bad randomization Method
		if(directions[0] == 0 && directions[1] == 0)
			directions = runTowardsPillsCloserThan(20);
			System.out.println("getPills");
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
		Queue<Tuple> startDirection = new LinkedList<Tuple>();
		ArrayList<Edge> firstDirections = start.getEdges();
		for(int i = 0; i < firstDirections.size(); i++){
			Node nextFieldToVisit = firstDirections.get(i).end;
			nextToVisit.add(nextFieldToVisit);
			startDirection.add(new Tuple(
				nextFieldToVisit.x -start.x,
				nextFieldToVisit.y -start.y)
			);
		}
		Node lastTouched;
		Tuple beginningDirection;
		while(true){
			if(nextToVisit.size() == 0)
				return new int[] {0,0};
			lastTouched = nextToVisit.remove();
			beginningDirection = startDirection.remove();
			if(alreadyVisited.contains(lastTouched))
				continue;
			if (lastTouched.hasPill()){
				break;
			}
			alreadyVisited.add(lastTouched);
			ArrayList<Edge> neighbourEdges = lastTouched.getEdges();
			for(int i = 0; i < neighbourEdges.size(); i++){
				nextToVisit.add(neighbourEdges.get(i).end);
				startDirection.add(beginningDirection);
			}
		}
		return new int[]{beginningDirection.x, beginningDirection.y};
	}

	int[] setRunningDirection(int distX, int distY){
		int[] directions = new int[] {0, 0};
		if(Math.abs(distX) > Math.abs(distY)){
			directions[0] = (int)Math.signum(distX);
		}else{
			directions[1] = (int)Math.signum(distY);
		}
		return avoidWalls(directions);
	}

	int[] avoidWalls(int[] directions){
		Node current = level.getNodeAt(pacX, pacY);
		Node toGoToNode = level.getNodeAt(pacX + directions[0], pacY + directions[1]);
		ArrayList<Edge> neighbourEdges = current.getEdges();
		for(int i = 0; i < neighbourEdges.size(); i++)
			if(neighbourEdges.get(i).start == current &&
				neighbourEdges.get(i).end == toGoToNode)
				return directions;
		System.out.println("collision detected");
		System.out.println(directions[0] + " " + directions[1]);
		int randomDirection = (int)Math.signum(r.nextFloat()-.5);
		System.out.println(randomDirection);
		return new int[]{directions[1]*randomDirection, directions[0]*randomDirection};
	}

	int[] runTowardsPillsCloserThan(int searchDepth){
		Node current = level.getNodeAt(pacX, pacY);
		return nextPillBFS(current);
	}

	int[] runAwayFromGhostsCloserThan(int radarRange){
		int[] returnDirections = new int[] {0, 0};
		int closestGhost = radarRange;
		for(int i = 0; i < ghostX.length; i++){
			// ignore inactive ghosts which still have coordinates 0,0
			if(ghostX[i] != 0 || ghostY[i] != 0){
				int distX = pacX - ghostX[i];
				int distY = pacY - ghostY[i];
				int dist = Math.abs(distX) + Math.abs(distY);
				if(dist < closestGhost){
					closestGhost = dist;
					returnDirections = setRunningDirection(distX, distY);
				}
			}
		}
		return returnDirections;
	}
}