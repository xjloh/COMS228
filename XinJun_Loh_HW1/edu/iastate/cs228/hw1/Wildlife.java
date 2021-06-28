package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife 
{
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain 
	 */
	public static void updatePlain(Plain pOld, Plain pNew)
	{
		// TODO 
		// 
		// For every life form (i.e., a Living object) in the grid pOld, generate  
		// a Living object in the grid pNew at the corresponding location such that 
		// the former life form changes into the latter life form. 
		// 
		// Employ the method next() of the Living class. 
		for(int i=0; i<pOld.grid.length; i++) {
			for(int j=0; j<pOld.grid[i].length; j++) {
				pNew.grid[i][j]=pOld.grid[i][j].next(pNew);
			}
		}
	}
	
	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{	
		// TODO 
		// 
		// Generate wildlife simulations repeatedly like shown in the 
		// sample run in the project description. 
		// 
		// 1. Enter 1 to generate a random plain, 2 to read a plain from an input
		//    file, and 3 to end the simulation. (An input file always ends with 
		//    the suffix .txt.)
		// 
		// 2. Print out standard messages as given in the project description. 
		// 
		// 3. For convenience, you may define two plains even and odd as below. 
		//    In an even numbered cycle (starting at zero), generate the plain 
		//    odd from the plain even; in an odd numbered cycle, generate even 
		//    from odd. 
		
		Plain even= null;   				 // the plain after an even number of cycles 
		Plain odd = null;                   // the plain after an odd number of cycles
		
		// 4. Print out initial and final plains only.  No intermediate plains should
		//    appear in the standard output.  (When debugging your program, you can 
		//    print intermediate plains.)
		// 
		// 5. You may save some randomly generated plains as your own test cases. 
		// 
		// 6. It is not necessary to handle file input & output exceptions for this 
		//    project. Assume data in an input file to be correctly formated. 
		int trialCount=1, input=0;
		Scanner in=new Scanner(System.in);
		System.out.println("Simulation of WildLife of the Plain");
		System.out.println("keys: 1 (random plain)  2 (file input)  3 (exit)");
		System.out.println();
		while(input!=3) {
			System.out.print("Trial"+ " "+ trialCount +":");
			input=in.nextInt();
			boolean end=false;
			int random=0, cyclesInput=0;
			String fileInput;
			switch(input) 
			{
				case 1: 
					System.out.println("Random plain");
					System.out.print("Enter grid width:");
					random=in.nextInt();
					even=new Plain(random);
					odd=new Plain(random);
					odd.randomInit();
					even.randomInit();
					break;
					
				case 2:
					System.out.println("Plain input from a file");
					System.out.print("File name:");
					fileInput=in.next();
					try {
						even=new Plain(fileInput);
						odd=new Plain(fileInput);
					}
					catch(FileNotFoundException x) {
						System.out.println("Incorrect file");
						x.printStackTrace();
					}
					break;
					
				default:
					end=true;
					break;
			}
			
			if(end) {
				break;
			}
			
			String initialPlain=even.toString();
			String finalPlain;
			boolean evenCycles=false;
			System.out.print("Enter the number of cycles:");
			cyclesInput=in.nextInt();
			for(int i=0; i<cyclesInput; i++) {
					if(i % 2 == 0) {
						updatePlain(even, odd);
						evenCycles=true;
					}
					else {
						 updatePlain(odd, even);
						 evenCycles=false;
					}
			}
			if(evenCycles) {
				finalPlain = odd.toString();
			}
			else {
				finalPlain= even.toString();
			}
			System.out.println();
			System.out.println("Initial plain:");
			System.out.println(); System.out.println();
			System.out.println(initialPlain);
			System.out.println("Final plain:");
			System.out.println(); System.out.println();
			System.out.println(finalPlain);
			
			trialCount++;
		}
		in.close();
	}
}
