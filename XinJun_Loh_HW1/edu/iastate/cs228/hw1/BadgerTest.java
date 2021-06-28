package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * 
 * @author Loh Xin Jun
 *
 */
public class BadgerTest {
	
	private Badger badger, badger2;
	private Plain p, p1, p2, p3;
	
	//Test plain with 1 badger and 2 rabbit
	@Before
	public void Plain() {
		p=new Plain(4);
		badger=new Badger(p, 0, 0, 3);
		p.grid[0][0]=badger;
		p.grid[1][0]=new Rabbit(p, 1, 0, 1);
		p.grid[2][0]=new Rabbit(p, 2, 0, 1);
	}

	@Test
	public void testWho() {
		assertEquals(badger.who(), State.BADGER);
		assertEquals(p.grid[0][0].who(), State.BADGER);
	}
	
	//Badger dies due to age
	@Test
	public void testNext() {
		Plain cycle1=new Plain(4);
		Living a = badger.next(cycle1);
		assertEquals(a.who(), State.BADGER); //Badger at age 4, and will die in the next cycle
		Plain cycle2=new Plain(4);
		Living b = a.next(cycle2);
		assertEquals(b.who(), State.EMPTY);
	}
	
	@Before
	public void Plain2() {
		p1=new Plain(3);
		badger2=new Badger(p1, 0, 0, 2);//badger2 at age 2
		p1.grid[0][0]=badger2;
		p1.grid[0][1]=new Badger(p1, 0, 1, 3);//Badger3 at age 3
		p1.grid[1][0]=new Rabbit(p1, 1, 0, 1);
		p1.grid[1][1]=new Rabbit(p1, 1, 1, 2);
		p1.grid[2][0]=new Rabbit(p1, 2, 0, 2);
	}
	
	@Test
	public void testWho2() {
		assertEquals(badger2.who(), State.BADGER);
		assertEquals(p1.grid[0][1].who(), State.BADGER);
	}
	
	//Test multiple badger
	@Test
	public void testNext2() {
		Plain cycle1=new Plain(3);
		Living a=badger2.next(cycle1);
		Living b=p1.grid[0][1].next(cycle1);
		assertEquals(a.who(), State.BADGER);//Badger2 at age 3
		assertEquals(b.who(), State.BADGER);//Badger3 at age 4
		
		Plain cycle2=new Plain(3);
		Living c=b.next(cycle2);
		assertEquals(c.who(), State.EMPTY);//Badger3 died to age
		Living d=a.next(cycle2);
		assertEquals(d.who(), State.BADGER);//Badger2 at age 4
		
		Plain cycle3=new Plain(3);
		Living e=d.next(cycle3);
		assertEquals(e.who(), State.EMPTY);//Badger2 died to age
	}
	
	@Before
	public void Plain3() {
		p2=new Plain(3);
		p2.grid[0][0]=new Badger(p2, 0, 0, 1);
		p2.grid[1][0]=new Fox(p2, 1, 0, 1);
		p2.grid[2][0]=new Rabbit(p2, 2, 0, 1);
	}
	
	//Badger dies due to hunger
	@Test
	public void testNextandWho() {
		assertEquals(p2.grid[0][0].who(), State.BADGER);
		Plain cycle1=new Plain(3);
		Living a=p2.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.EMPTY);
	}
	
	
	@Before
	public void Plain4() {
		p3=new Plain(3);
		p3.grid[0][0]=new Badger(p3,0,0,1);
		p3.grid[1][0]=new Fox(p3, 1,0,1);
		p3.grid[0][1]=new Fox(p3, 0,1,1);
	}
	
	//Badger geys eaten by many fox
	@Test
	public void testNextandWho2() {
		Plain cycle1=new Plain(3);
		Living a=p3.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.FOX);
	}
}
