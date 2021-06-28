package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.*;
/**
 * 
 * @author Loh Xin Jun
 *
 */
public class RabbitTest {
	private Plain p;
	private Rabbit rabbit;
	
	@Before
	public void Plain() {
		p=new Plain(3);
		rabbit=new Rabbit(p, 0, 0, 2);
		p.grid[0][1]=new Grass(p, 0, 1);
		p.grid[1][0]=new Grass(p, 1, 0);
	}
	
	@Test
	public void testWho() {
		//Rabbit die due to age
		assertEquals(rabbit.who(), State.RABBIT);
		Plain cycle1=new Plain(3);
		Living a=rabbit.next(cycle1);
		assertEquals(a.who(), State.RABBIT);
		Plain cycle2=new Plain(3);
		Living b=a.next(cycle2);
		assertEquals(b.who(), State.EMPTY);
		
		//Rabbit die due to hunger
		Plain p1=new Plain(3);
		p1.grid[0][0]=new Rabbit(p1, 0, 0, 1);
		Plain cycle=new Plain(3);
		Living c=p1.grid[0][0].next(cycle);
		assertEquals(c.who(), State.EMPTY);
		
		//Rabbit gets eaten by fox
		Plain p2=new Plain(3);
		p2.grid[1][0]=new Rabbit(p2, 1, 0, 1);
		p2.grid[0][1]=new Fox(p2, 0, 1, 1);
		p2.grid[1][1]=new Badger(p2, 1, 1, 1);
		p2.grid[0][0]=new Fox(p2, 0, 0, 1);
		p2.grid[2][0]=new Grass(p2, 2, 0);
		Plain nextCycle=new Plain(3);
		Living d=p2.grid[1][0].next(nextCycle);
		assertEquals(d.who(), State.FOX);
		
		//Rabbit gets eaten by Badger
		Plain p3=new Plain(3);
		p3.grid[0][0]=new Rabbit(p3, 0, 0, 1);
		p3.grid[0][1]=new Grass(p3, 0, 1);
		p3.grid[1][1]=new Badger(p3, 1, 1, 1);
		p3.grid[1][0]=new Badger(p3, 1, 0, 1);
		Plain nextCycle1=new Plain(3);
		Living e=p3.grid[0][0].next(nextCycle1);
		assertEquals(e.who(), State.BADGER);
	}

}
