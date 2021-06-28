package edu.iastate.cs228.hw1;

/**
 * 
 * @author Loh Xin Jun
 *
 */

/** 
 * Empty squares are competed by various forms of life.
 */
public class Empty extends Living 
{
	public Empty (Plain p, int r, int c) 
	{
		// TODO  
		plain=p; row=r; column=c;
	}
	
	public State who()
	{
		// TODO 
		return State.EMPTY; 
	}
	
	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or Grass, or remain empty. 
	 * @param pNew     plain of the next life cycle.
	 * @return Living  life form in the next cycle.   
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for corresponding survival rules. 
		int count[]=new int[Living.NUM_LIFE_FORMS];
		this.census(count);
		if(count[RABBIT]>1) {
			return new Rabbit(pNew, this.row, this.column, 0);
		}
		else if(count[FOX]>1) {
			return new Fox(pNew, this.row, this.column, 0);
		}
		else if(count[BADGER]>1) {
			return new Badger(pNew, this.row, this.column, 0);
		}
		else if(count[GRASS]>1) {
			return new Grass(pNew, this.row, this.column);
		}
		else {
			return new Empty(pNew, this.row, this.column);
		}
	}
}
