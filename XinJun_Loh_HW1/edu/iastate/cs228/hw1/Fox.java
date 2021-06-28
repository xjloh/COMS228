package edu.iastate.cs228.hw1;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * A fox eats rabbits and competes against a badger. 
 */
public class Fox extends Animal 
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Fox (Plain p, int r, int c, int a) 
	{
		// TODO 
		plain=p; row=r; column=c; age=a;
	}
		
	/**
	 * A fox occupies the square. 	 
	 */
	public State who()
	{
		// TODO 
		return State.FOX; 
	}
	
	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior badgers. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a fox. 
		int count[]=new int[Living.NUM_LIFE_FORMS];
		this.census(count);
		if(myAge()>=FOX_MAX_AGE) {
			return new Empty(pNew, this.row, this.column);
		}
		else if(count[BADGER] > count[FOX]) {
			return new Badger(pNew, this.row, this.column, 0);
		}
		else if(count[BADGER] + count[FOX] > count[RABBIT]) {
			return new Empty(pNew, this.row, this.column);
		}
		else {
			return new Fox(pNew, this.row, this.column, this.age+1);
		}
	}
}
