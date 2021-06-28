package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;
/**
 * 
 * @author Loh Xin Jun
 *
 */
public class GrassTest {
	private Plain p, p1, p2, p3;
	private Grass g;
	
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
	}
	
	@Test
	public void Whotest() {
		assertEquals(p.grid[0][0].who(), State.GRASS);
		assertEquals(p.grid[2][2].who(), State.GRASS);
	}
	
	@Before
	public void Plain1() {
		p1=new Plain(3);
		g=new Grass(p1, 0,0);
		p1.grid[0][0]=g;
		p1.grid[0][1]=new Rabbit(p1, 0, 1, 1);
		p1.grid[1][0]=new Rabbit(p1, 1, 0, 1);
		p1.grid[1][1]=new Rabbit(p1, 1, 1, 1);
	}
	
	//Grass gets eaten out by too many rabbit
	@Test
	public void testNext() {
		assertEquals(p1.grid[0][0].who(), State.GRASS);
		Plain cycle1=new Plain(3);
		Living a=p1.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.EMPTY);
	}

	@Before
	public void Plain2() {
		p2=new Plain(3);
		p2.grid[0][0]=new Rabbit(p2, 0, 0, 1);
		p2.grid[1][0]=new Rabbit(p2, 1, 0, 1);
		p2.grid[0][1]=new Rabbit(p2, 0, 1, 1);
		p2.grid[1][1]=new Grass(p2, 1, 1);
		p2.grid[1][2]=new Rabbit(p2, 1, 2, 1);
	}
	
	//Rabbits may also multiply fast enough to take over Grass.
	@Test
	public void testNext1() {
		assertEquals(p2.grid[1][1].who(), State.GRASS);
		Plain cycle1=new Plain(3);
		Living a=p2.grid[1][1].next(cycle1);
		assertEquals(a.who(), State.RABBIT);
	}
	
	@Before
	public void Plain3() {
		p3=new Plain(3);
		p3.grid[0][0]=new Badger(p3, 0, 0, 1);
		p3.grid[1][0]=new Fox(p3, 1, 0, 1);
		p3.grid[0][1]=new Rabbit(p3, 0, 1, 1);
		p3.grid[1][1]=new Grass(p3, 1, 1);
		p3.grid[1][2]=new Empty(p3, 1, 2);
	}
	
	//Will remain the same
	@Test
	public void testNext2() {
		assertEquals(p3.grid[1][1].who(), State.GRASS);
		Plain cycle1=new Plain(3);
		Living a=p3.grid[1][1].next(cycle1);
		assertEquals(a.who(), State.GRASS);
	}
	
}
