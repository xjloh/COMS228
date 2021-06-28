package edu.iastate.cs228.hw1;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal 
{	
	/**
	 * Creates a Rabbit object.
	 * @param p: plain  
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Rabbit (Plain p, int r, int c, int a) 
	{
		// TODO 
		plain=p; row=r; column=c; age=a;
	}
		
	// Rabbit occupies the square.
	public State who()
	{
		// TODO  
		return State.RABBIT; 
	}
	
	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a fox.  
	 * @param pNew     plain of the next cycle 
	 * @return Living  new life form occupying the same square
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a rabbit. 
		int countRabbit[]=new int[NUM_LIFE_FORMS];
		this.census(countRabbit);
		if(myAge()>=RABBIT_MAX_AGE) {
			return new Empty(pNew, this.row, this.column);
		}
		else if(countRabbit[GRASS]==0) {
			return new Empty(pNew, this.row, this.column);
		}
		else if(countRabbit[FOX]+countRabbit[BADGER]>=countRabbit[RABBIT] && countRabbit[FOX]>countRabbit[BADGER]) {
			return new Fox(pNew, this.row, this.column, 0);
		}
		else if(countRabbit[BADGER]>countRabbit[RABBIT]) {
			return new Badger(pNew, this.row, this.column, 0);
		}
		else {
			return new Rabbit(pNew, this.row, this.column, this.age++);
		}
	}
}
