package edu.iastate.cs228.hw2;

import java.io.File;


/**
 * 
 * @author Loh Xin Jun
 *
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class RotationalPointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
	protected String outputFileName;   // "select.txt", "insert.txt", "merge.txt", or "quick.txt"
	
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[]. Set outputFileName. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if(pts==null || pts.length==0) {
			throw new IllegalArgumentException();
		}
		points=new Point[pts.length];
		for(int i=0; i<pts.length; i++) {
			points[i]=pts[i];
		}
		sortingAlgorithm=algo;
		
		if(algo.equals(Algorithm.InsertionSort)) {
			outputFileName="insert.txt";
		}
		else if(algo.equals(Algorithm.SelectionSort)) {
			outputFileName="select.txt";
		}
		else if(algo.equals(Algorithm.MergeSort)) {
			outputFileName="merge.txt";
		}
		else if(algo.equals(Algorithm.QuickSort)) {
			outputFileName="quick.txt";
		}		
	}

	
	/**
	 * This constructor reads points from a file. Set outputFileName. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		// TODO
		sortingAlgorithm=algo;
		if(algo.equals(Algorithm.InsertionSort)) {
			outputFileName="insert.txt";
		}
		else if(algo.equals(Algorithm.SelectionSort)) {
			outputFileName="select.txt";
		}
		else if(algo.equals(Algorithm.MergeSort)) {
			outputFileName="merge.txt";
		}
		else if(algo.equals(Algorithm.QuickSort)) {
			outputFileName="quick.txt";
		}
		
		File file=new File(inputFileName);
		Scanner s=new Scanner(file);
		int count=0;
		try {
			while(s.hasNextInt()) {
				s.nextInt();
				count++;
			}
			if(count%2!=0) {
				s.close();
				throw new InputMismatchException();
			}
			s.close();
			
			Scanner scan=new Scanner(file);
			int i=0;
			points=new Point[count/2];
			while(scan.hasNextInt()) {
				Point p=new Point(scan.nextInt(), scan.nextInt());
				points[i]=p;
				i++;
			}
			scan.close();
		}
		catch(FileNotFoundException x) {
			throw new FileNotFoundException();
		}
	}

	
	/**
	 * Carry out three rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates. 
	 *     d) Sort points[] again by the polar angle with respect to medianCoordinatePoint.
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting. Copy the sorting result back onto the array points[] by calling 
	 * the method getPoints() in AbstractSorter. 
	 *      
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		// TODO  
		AbstractSorter aSorter; 
		
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the three 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0, 1, or 2. in case it is 2, must have made 
		//        the call setReferencePoint(medianCoordinatePoint) already. 
		//
		//     b) call sort(). 		
		// 
		// sum up the times spent on the three sorting rounds and set the instance variable scanTime. 
		aSorter=null;
		if(sortingAlgorithm==Algorithm.SelectionSort) {
			aSorter=new SelectionSorter(points);
		}
		
		else if(sortingAlgorithm==Algorithm.InsertionSort) {
			aSorter=new InsertionSorter(points);
		}
		
		else if(sortingAlgorithm==Algorithm.MergeSort) {
			aSorter=new MergeSorter(points);
		}
		
		else if(sortingAlgorithm==Algorithm.QuickSort) {
			aSorter=new QuickSorter(points);
		}
		
		aSorter.setComparator(0);
		long start=System.nanoTime();
		aSorter.sort();
		scanTime=System.nanoTime()-start;
		aSorter.setComparator(1);
		long start1=System.nanoTime();
		aSorter.sort();
		scanTime=System.nanoTime()-start1;
		medianCoordinatePoint=aSorter.getMedian();
		aSorter.setReferencePoint(medianCoordinatePoint);
		aSorter.setComparator(2);
		long start2=System.nanoTime();
		aSorter.sort();
		scanTime=System.nanoTime()-start2;
		aSorter.getPoints(points);
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		String stat="";
		stat+=sortingAlgorithm +"\t" + " " + points.length + "\t" +scanTime;
		
		return stat; 
		// TODO 
	}
	
	
	/**
	 * Write points[] after a call to scan().  When printed, the points will appear 
	 * in order of polar angle with respect to medianCoordinatePoint with every point occupying a separate 
	 * line.  The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		String s="";
		for(int i=0; i<points.length; i++) {
			s+=points[i].toString()+"\n";
		}
		return s; 
		// TODO
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException
	{
		// TODO 
		try {
			PrintWriter file=new PrintWriter(outputFileName);
			file.print(toString());
			file.close();
		}
		catch(FileNotFoundException x) {
			throw new FileNotFoundException();
		}
	}	

	
	/**
	 * This method is called after each scan for visually check whether the result is correct.  You  
	 * just need to generate a list of points and a list of segments, depending on the value of 
	 * sortByAngle, as detailed in Section 4.1. Then create a Plot object to call the method myFrame().  
	 */
	public void draw()
	{		
		int numSegs = 0;  // number of segments to draw 

		// Based on Section 4.1, generate the line segments to draw for display of the sorting result.
		// Assign their number to numSegs, and store them in segments[] in the order. 
//		Segment[] segments=new Segment[numSegs];
		
		// TODO
		ArrayList<Segment> seglist=new ArrayList<Segment>();
		for(int i=0; i<points.length-1; i++) {
			
			if(points[i].compareTo(points[i+1])!=0) {
				seglist.add(new Segment(points[i], points[i+1]));
			}
			
			if(points[0].compareTo(points[points.length-1])!=0) {
				seglist.add(new Segment(points[0], points[points.length-1]));
			}
			
			seglist.add(new Segment(medianCoordinatePoint, points[i]));
		}
		numSegs=seglist.size();
		Segment[] segments=new Segment[numSegs];
		seglist.toArray(segments);
		
		String sort = null; 

		switch(sortingAlgorithm)
		{
		case SelectionSort: 
			sort = "Selection Sort"; 
			break; 
		case InsertionSort: 
			sort = "Insertion Sort"; 
			break; 
		case MergeSort: 
			sort = "Mergesort"; 
			break; 
		case QuickSort: 
			sort = "Quicksort"; 
			break; 
		default: 
			break; 		
		}
		
		// The following statement creates a window to display the sorting result.
		Plot.myFrame(points, segments, sort);
	}
		
}
