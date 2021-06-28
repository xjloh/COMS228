package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.FileNotFoundException;

/**
 * 
 * @author Loh Xin Jun
 *
 */
public class FoxTest {
	private Plain p, p1, p2, p3;
	
	private Fox fox;
	
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
	}
	@Test
	public void Whotest() {
		assertEquals(p.grid[0][2].who(), State.FOX);
	}
	
	@Before
	public void Plain1() {
		p1=new Plain(3);
		fox=new Fox(p1, 0, 0, 5);
	}
	
	//Fox dies of old age
	@Test
	public void testNextandAge() {
		Plain cycle1=new Plain(3);
		Living a=fox.next(cycle1);
		assertEquals(a.who(), State.FOX);
		Plain cycle2=new Plain(3);
		Living b=a.next(cycle2);
		assertEquals(b.who(), State.EMPTY);
	}
	
	@Before
	public void Plain2() {
		p2=new Plain(3);
		p2.grid[0][0]=new Fox(p2, 0, 0, 1);
		p2.grid[0][1]=new Badger(p2, 0, 1, 1);
		p2.grid[1][0]=new Badger(p2, 1, 0, 1);
		p2.grid[1][1]=new Grass(p2, 1, 1);
	}
	
	//Fox gets eaten by badger
	@Test
	public void testNext() {
		assertEquals(p2.grid[0][0].who(), State.FOX);
		Plain cycle1=new Plain(3);
		Living a=p2.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.BADGER);
	}
	
	@Before
	public void Plain3() {
		p3=new Plain(3);
		p3.grid[0][0]=new Fox(p3, 0, 0, 1);
		p3.grid[1][0]=new Rabbit(p3, 1, 0, 1);
		p3.grid[0][1]=new Badger(p3, 0, 1, 1);
	}
	
	//Fox dies of hunger
	@Test
	public void testNext1() {
		assertEquals(p3.grid[0][0].who(), State.FOX);
		Plain cycle1=new Plain(3);
		Living a=p3.grid[0][0].next(cycle1);
		assertEquals(a.who(), State.EMPTY);
	}
}
