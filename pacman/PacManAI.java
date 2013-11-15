package pacman;

import java.util.Random;

public class PacManAI{
	Random r = new Random();
	LevelGraph level;

	PacManAI(short leveldata[]){
		level = new LevelGraph(leveldata);
	}

	public int getMove(){
		return r.nextInt(4);
	}
}