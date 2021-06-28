package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;

/**
 * 
 * @author Loh Xin Jun
 *
 */

public class LivingTest {
	private Plain p, p1;
	
	@Before
	public void Plain() throws FileNotFoundException {
		p=new Plain("public1-3x3.txt");
	}
	
	@Test
	public void testCensus() {
		int newPopulation[]=new int[Living.NUM_LIFE_FORMS];
		p.grid[1][1].census(newPopulation);
		assertEquals(newPopulation[Living.BADGER], 1);
		assertEquals(newPopulation[Living.FOX], 4);
		assertEquals(newPopulation[Living.RABBIT], 1);
		assertEquals(newPopulation[Living.GRASS], 2);
		assertEquals(newPopulation[Living.EMPTY], 1);
		
		int count[]=new int[Living.NUM_LIFE_FORMS];
		p.grid[0][0].census(count);
		assertEquals(count[Living.BADGER], 1);
		assertEquals(count[Living.FOX], 2);
		assertEquals(count[Living.GRASS], 1);
		
		int count2[]=new int[Living.NUM_LIFE_FORMS];
		p.grid[2][0].census(count2);
		assertEquals(count2[Living.FOX], 3);
		assertEquals(count2[Living.EMPTY], 1);
		
	}
	
	@Before
	public void Plain1() throws FileNotFoundException{
		p1=new Plain("public1-5cycles.txt");
	}
	
	@Test
	public void testCensus1() {
		int count[]=new int[Living.NUM_LIFE_FORMS];
		p1.grid[1][1].census(count);
		assertEquals(count[Living.BADGER], 0);
		assertEquals(count[Living.FOX], 4);
		assertEquals(count[Living.RABBIT], 0);
		assertEquals(count[Living.GRASS], 2);
		assertEquals(count[Living.EMPTY], 3);
		
		int count2[]=new int[Living.NUM_LIFE_FORMS];
		p1.grid[0][0].census(count2);
		assertEquals(count2[Living.FOX], 1);
		assertEquals(count2[Living.EMPTY], 2);
	}
}
