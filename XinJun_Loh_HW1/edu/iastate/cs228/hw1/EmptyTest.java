package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.FileNotFoundException;

/**
 * 
 * @author Loh Xin Jun
 *
 */
public class EmptyTest {
	private Plain p, p1, p2, p3, p4, p5;
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
	}
	
	@Test
	public void testWho() {
		assertEquals(p.grid[2][1].who(), State.EMPTY);
	}
	
	@Before
	public void Plain2() {
		p1=new Plain(3);
		p1.grid[0][0]=new Empty(p1, 0, 0);
		p1.grid[1][0]=new Rabbit(p1, 1, 0, 1);
		p1.grid[0][1]=new Fox(p1, 0, 1, 1);
		p1.grid[1][1]=new Badger(p1, 1, 1, 1);
		p1.grid[2][0]=new Grass(p1, 2, 0);
	}
	
	@Test
	public void testNextCycle() {
		assertEquals(p1.grid[0][0].who(), State.EMPTY);
		Plain cycle1=new Plain(3);
		Living a=p1.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.EMPTY);
	}
	
	@Before
	public void Plain3() {
		p2=new Plain(3);
		p2.grid[0][0]=new Empty(p2, 0, 0);
		p2.grid[0][1]=new Rabbit(p2, 0, 1, 1);
		p2.grid[1][0]=new Rabbit(p2, 1, 0, 1);
		p2.grid[1][1]=new Badger(p2, 1, 1, 1);
		p2.grid[2][0]=new Grass(p2, 2, 0);
	}
	
	@Test
	public void testNextCycle1() {
		assertEquals(p2.grid[0][0].who(), State.EMPTY);
		Plain cycle1=new Plain(3);
		Living a=p2.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.RABBIT);
	}
	
	@Before
	public void Plain4() {
		p3=new Plain(3);
		p3.grid[0][0]=new Empty(p3, 0, 0);
		p3.grid[0][1]=new Fox(p3, 0, 1, 1);
		p3.grid[1][0]=new Fox(p3, 1, 0, 1);
		p3.grid[1][1]=new Badger(p3, 1, 1, 1);
		p3.grid[2][0]=new Grass(p3, 2, 0);
	}
	
	@Test
	public void testNextCycle2() {
		assertEquals(p3.grid[0][0].who(), State.EMPTY);
		Plain cycle1=new Plain(3);
		Living a=p3.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.FOX);
	}

	@Before
	public void Plain5() {
		p4=new Plain(3);
		p4.grid[0][0]=new Empty(p4, 0, 0);
		p4.grid[0][1]=new Badger(p4, 0, 1, 1);
		p4.grid[1][0]=new Badger(p4, 1, 0, 1);
		p4.grid[1][1]=new Badger(p4, 1, 1, 1);
		p4.grid[2][0]=new Grass(p4, 2, 0);
	}
	
	@Test
	public void testNextCycle3() {
		assertEquals(p4.grid[0][0].who(), State.EMPTY);
		Plain cycle1=new Plain(3);
		Living a=p4.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.BADGER);
	}
	
	@Before
	public void Plain6() {
		p5=new Plain(3);
		p5.grid[0][0]=new Empty(p5, 0, 0);
		p5.grid[0][1]=new Grass(p5, 0, 1);
		p5.grid[1][0]=new Grass(p5, 1, 0);
		p5.grid[1][1]=new Grass(p5, 1, 1);
		p5.grid[2][0]=new Rabbit(p5, 2, 0, 1);
	}
	
	@Test
	public void testNextCycle4() {
		assertEquals(p5.grid[0][0].who(), State.EMPTY);
		Plain cycle1=new Plain(3);
		Living a=p5.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.GRASS);
	}
}
