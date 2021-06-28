package edu.iastate.cs228.hw1;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		// TODO
		plain=p; row=r; column=c; age=a;
	}
	
	/**
	 * A badger occupies the square. 	 
	 */
	public State who()
	{
		// TODO 
		return State.BADGER;
	}
	
	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a badger. 
		int count[]=new int[Living.NUM_LIFE_FORMS];
		this.census(count);
		
		if(myAge()>=BADGER_MAX_AGE) {
			return new Empty(pNew, this.row, this.column);
		}
		else if(count[BADGER]==1 && count[FOX]>1) {
			return new Fox(pNew, this.row, this.column, 0);
		}
		else if(count[BADGER] + count[Living.FOX] > count[RABBIT]) {
			return new Empty(pNew, this.row, this.column);
		}
		else {
			return new Badger(pNew, this.row, this.column, this.age+1);
		}
	}
}
