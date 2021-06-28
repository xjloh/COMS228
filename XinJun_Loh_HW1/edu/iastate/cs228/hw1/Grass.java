package edu.iastate.cs228.hw1;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is eaten. 
 *
 */
public class Grass extends Living 
{
	public Grass (Plain p, int r, int c) 
	{
		// TODO 
		plain=p; row=r; column=c;
	}
	
	public State who()
	{
		// TODO  
		return State.GRASS; 
	}
	
	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast enough to take over Grass.
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for grass. 
		int count[]=new int[Living.NUM_LIFE_FORMS];
		this.census(count);
		if(count[RABBIT]/count[GRASS]==3) {
			return new Empty(pNew, this.row, this.column);
		}
		else if(count[RABBIT]>=3) {
			return new Rabbit(pNew, this.row, this.column, 0);
		}
		else {
			return new Grass(pNew, this.row, this.column);
		}
	}
}
