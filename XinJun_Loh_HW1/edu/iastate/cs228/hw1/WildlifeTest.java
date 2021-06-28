package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;

/**
 * 
 * @author Loh Xin Jun
 *
 */
public class WildlifeTest {
	private Plain p;
	private Plain pFinal;
	
	@Before
	public void Plain() throws FileNotFoundException{
		p=new Plain("public1-3x3.txt");
		pFinal=new Plain("public1-5cycles.txt");
	}
	@Test
	public void test() {
		
		assertEquals(State.GRASS, p.grid[0][0].next(p).who());
		
		assertEquals(State.FOX, p.grid[0][1].next(p).who());
		
		assertEquals(State.EMPTY, p.grid[0][2].next(p).who());
		
		assertEquals(State.EMPTY, p.grid[1][0].next(p).who());
		
		assertEquals(State.EMPTY, p.grid[1][1].next(p).who());
		
		assertEquals(State.FOX, p.grid[1][2].next(p).who());
		
		assertEquals(State.EMPTY, p.grid[2][0].next(p).who());
		
		assertEquals(State.FOX, p.grid[2][1].next(p).who());
		
		assertEquals(State.GRASS, p.grid[2][2].next(p).who());
		
		String finalP=pFinal.toString();
		Plain p2=new Plain(p.getWidth());
		for(int i=0; i<5; i++) {
			if(i%2==0) {
				Wildlife.updatePlain(p, p2);
			}
			else {
				Wildlife.updatePlain(p2, p);
			}
		}
		assertEquals(p2.toString(), finalP);
	}

}
