package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;

/**
 * 
 * @author Loh Xin Jun
 *
 */


public class AnimalTest {
	
	private Plain p;
	private Plain a;
	
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
		a=new Plain("public2-8cycles.txt");
	}
	
	@Test
	public void testmyAge() {
		assertEquals(((Animal)p.grid[0][2]).myAge(), 0);
		assertEquals(((Animal)p.grid[0][2]).myAge(), 0);
		assertEquals(((Animal)p.grid[1][0]).myAge(), 0);
		assertEquals(((Animal)p.grid[1][2]).myAge(), 0);
		
		assertEquals(((Animal)a.grid[1][2]).myAge(), 6);
		assertEquals(((Animal)a.grid[3][0]).myAge(), 1);
		assertEquals(((Animal)a.grid[3][1]).myAge(), 2);
	}
}
