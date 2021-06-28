package edu.iastate.cs228.hw1;

/**
 *  
 * @author Loh Xin Jun
 *
 */

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner; 
import java.util.Random; 

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain 
{
	private int width; // grid size: width X width 
	
	public Living[][] grid; 
	
	/**
	 *  Default constructor reads from a file 
	 */
	public Plain(String inputFileName) throws FileNotFoundException
	{		
        // TODO 
		// 
		// Assumption: The input file is in correct format. 
		// 
		// You may create the grid plain in the following steps: 
		// 
		// 1) Reads the first line to determine the width of the grid.
		// 
		// 2) Creates a grid object. 
		// 
		// 3) Fills in the grid according to the input file. 
		// 
		// Be sure to close the input file when you are done. 
		File file=new File(inputFileName);
		Scanner scanner=new Scanner(file);
		int findWidth=-1, i=0;
		while(scanner.hasNextLine())
		{
			String s=scanner.nextLine(); 
			if(findWidth<0) 
			{
				findWidth=s.length()/3;
				width=findWidth;
				grid=new Living[findWidth][findWidth];
			}
			
			s.replace("  "," ").replace('\n',' ').trim();
			Scanner scan=new Scanner(s);
			scan.useDelimiter(" ");
			for(int j=0; scan.hasNext(); j++) 
			{
				String fill=scan.next();
				fill.trim().replace(" ", "");
				if(fill.length()>0) 
				{
					char input=fill.charAt(0);
					if(fill.length()==1) 
					{
						if(input=='E') {
							grid[i][j]=new Empty(this, i, j);
						}
						else if(input=='G') {
							grid[i][j]=new Grass(this, i, j);
						}
					}
					else if(fill.length()==2) 
					{
						int findAge=Character.getNumericValue(fill.charAt(1));
						if(input=='B') {
							grid[i][j]=new Badger(this, i, j, findAge);
						}
						else if(input=='F') {
							grid[i][j]=new Fox(this, i, j, findAge);
						}
						else if(input=='R') {
							grid[i][j]=new Rabbit(this, i, j, findAge);
						}
					}
				}
				else {
					j--;
					continue;
				}
			}
			i++;
			scan.close();
		}
		scanner.close();	
	}
	
	/**
	 * Constructor that builds a w x w grid without initializing it. 
	 * @param width  the grid 
	 */
	public Plain(int w)
	{
		// TODO 
		width=w;
		grid=new Living[w][w];
	}
	
	
	public int getWidth()
	{
		// TODO  
		return width;  // to be modified 
	}
	
	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, FOX, RABBIT, GRASS, or EMPTY.  
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit()
	{
		Random generator = new Random(); 
		 
		// TODO 
		for(int i=0; i<grid.length; i++) 
		{
			for(int j=0; j<grid[i].length; j++) 
			{
				int animal=generator.nextInt(Living.NUM_LIFE_FORMS);
				if(animal==Living.BADGER) {
					grid[i][j]=new Badger(this, i, j, 0);
				}
				else if(animal==Living.EMPTY) {
					grid[i][j]=new Empty(this, i ,j);
				}
				else if(animal==Living.FOX) {
					grid[i][j]=new Fox(this, i, j, 0);
				}
				else if(animal==Living.GRASS) {
					grid[i][j]=new Grass(this, i, j);
				}
				else if(animal==Living.RABBIT) {
					grid[i][j]=new Rabbit(this, i, j, 0);
				}
			}
		}
	}
	
	
	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.  
	 */
	public String toString()
	{
		// TODO
		String type="";
		for(int i=0; i<grid.length; i++) 
		{
			for(int j=0; j<grid[i].length; j++) 
			{
				if(grid[i][j].who()==State.EMPTY)
				{
					type+= "E"+"  ";
				}
				else if(grid[i][j].who()==State.GRASS) 
				{
					type+= "G"+"  ";
				}
				else if(grid[i][j].who()==State.BADGER) 
				{
					type+= "B"+((Animal)grid[i][j]).myAge()+" ";
				}
				else if(grid[i][j].who()==State.FOX) 
				{
					type+= "F"+((Animal)grid[i][j]).myAge()+" ";
				}
				else if(grid[i][j].who()==State.RABBIT) 
				{
					type+= "R"+((Animal)grid[i][j]).myAge()+" ";
				}
			}
			type +="\n";
		}
		return type;
	}
	

	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose. 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws FileNotFoundException
	{
		// TODO 
		// 
		// 1. Open the file. 
		// 
		// 2. Write to the file. The five life forms are represented by characters 
		//    B, E, F, G, R. Leave one blank space in between. Examples are given in
		//    the project description. 
		// 
		// 3. Close the file. 
		File file=new File(outputFileName);
		PrintWriter output=new PrintWriter(file);
		String name=this.toString();
		output.write(name);
		output.close();
	}			
}
