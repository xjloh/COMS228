package edu.iastate.cs228.hw2;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       RotationalPointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		RotationalPointScanner[] scanners = new RotationalPointScanner[4]; 
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() and draw() 
		//        methods in the RotationalPointScanner class.  You can visualize the result of each scan.  
		//        (Windows have to be closed manually before rerun.)  
		// 
		//     c) After all four scans are done for the input, print out the statistics table (cf. Section 2). 
		//
		// A sample scenario is given in Section 2 of the project description. 
		
		Scanner p=new Scanner(System.in);
		System.out.println("Performance of Four Sorting Algorithms in Point Scanning");
		System.out.println();
		System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");
		int count=1;
		boolean exit=false;
		while(exit==false) {
			System.out.print("Trial"+" "+count + ": ");
			int k=p.nextInt();
			if(k==1) {
				System.out.print("Enter number of random points:");
				int numRand=p.nextInt();
				System.out.println();
				Random rand=new Random();
				Point[] points=CompareSorters.generateRandomPoints(numRand, rand);
				scanners[0]=new RotationalPointScanner(points, Algorithm.SelectionSort);
				scanners[1]=new RotationalPointScanner(points, Algorithm.InsertionSort);
				scanners[2]=new RotationalPointScanner(points, Algorithm.MergeSort);
				scanners[3]=new RotationalPointScanner(points, Algorithm.QuickSort);
			}
			else if(k==2) {
				System.out.println("Points from a file");
				System.out.print("File name: ");
				String in=p.next();
				try {
					scanners[0]=new RotationalPointScanner(in, Algorithm.SelectionSort);
					scanners[1]=new RotationalPointScanner(in, Algorithm.InsertionSort);
					scanners[2]=new RotationalPointScanner(in, Algorithm.MergeSort);
					scanners[3]=new RotationalPointScanner(in, Algorithm.QuickSort);
				}
				catch(Exception x){
					throw new FileNotFoundException();
				}
			}
			else {
				exit=true;
				break;
			}
			System.out.println("\n\nalgorithm\tsize\ttime"+" "+"(ns)");
			System.out.println("---------------------------------- ");
			for(int i=0; i<scanners.length; i++) {
				scanners[i].scan();
				scanners[i].draw();
				scanners[i].writePointsToFile();
				System.out.println(scanners[i].stats());
			}
			System.out.println("---------------------------------- ");
			count++;
		}
		p.close();
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] × [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		// TODO 
		if(numPts<1) {
			throw new IllegalArgumentException();
		}
		Point[] points=new Point[numPts];
		for(int i=0; i<points.length; i++) {
			points[i]=new Point(rand.nextInt(101)-50, rand.nextInt(101)-50);
		}
		return points;
	}
	
}
