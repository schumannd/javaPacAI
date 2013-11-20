package pacman;

import java.util.Random;
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

	public int[] getMove(){
		int[] directions = runAwayFromGhostsCloserThan(5);
		// bad randomization Method
		if(directions[0] == 0 && directions[1] == 0){
			System.out.print("random");
			switch(r.nextInt(4)){
				case 0: //left
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
		}
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
	public int[] runAwayFromGhostsCloserThan(int radarRange){
		int dx = 0;
		int dy = 0;
		int closestGhost = radarRange;
		for(int i = 0; i < ghostX.length; i++){
			int distX = pacX - ghostX[i];
			int distY = pacY - ghostY[i];
			int dist = Math.abs(distX) + Math.abs(distY);
			if(dist < closestGhost){
				closestGhost = dist;
				if(Math.abs(distX) > Math.abs(distY)){
					dx = (int)Math.signum(distX);
					dy = 0;
				}else{
					dy = (int)Math.signum(distY);
					dx = 0;				}
			}
		}
		return new int[] {dx, dy};
	}
}