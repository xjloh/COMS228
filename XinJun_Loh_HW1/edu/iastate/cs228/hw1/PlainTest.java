package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;
/**
 * 
 * @author Loh Xin Jun
 *
 */
public class PlainTest {
	private Plain p;
	
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
	}
	
	@Test
	public void test() {
		assertEquals(p.grid[0][0].who(), State.GRASS);
		assertEquals(p.grid[0][1].who(), State.BADGER);
		assertEquals(p.grid[0][2].who(), State.FOX);
		assertEquals(p.grid[1][2].who(), State.RABBIT);
		assertEquals(p.grid[2][1].who(), State.EMPTY);
		
		assertEquals(p.getWidth(), 3);
		assertEquals(((Animal)p.grid[0][1]).myAge(), 0);
	}
	
	@Test
	public void test2() throws FileNotFoundException{
		Plain p1=new Plain(3);
		
		p1.randomInit();
		
		Plain p2=p1;
		assertEquals(p1.toString(), p2.toString());
		
		String p3="test3x3.txt";
		p1.write(p3);
		Plain p4=new Plain(p3);
		assertEquals(p1.toString(), p4.toString());
		assertEquals(p1.getWidth(), p4.getWidth());
		
	}
	
}
