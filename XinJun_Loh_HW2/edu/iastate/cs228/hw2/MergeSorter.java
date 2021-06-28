package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		// TODO  
		super(pts);
		algorithm="mergesort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		// TODO 
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		if(pts.length<=1) {
			return;
		}
		int n=pts.length;
		int m=n/2;
		Point[] left=new Point[m];
		Point[] right=new Point[n-m];
		
		for(int i=0; i<m; i++) {
			left[i]=pts[i];
		}
		
		for(int j=m; j<n; j++) {
			right[j-m]=pts[j];
		}
		
		mergeSortRec(left);
		mergeSortRec(right);
		Point[] merged=merge(left, right);
		for(int i=0; i<merged.length; i++) {
			pts[i]=merged[i];
		}
	}
	// Other private methods in case you need ...
	private Point[] merge(Point[] left, Point[] right) {
		int i=0,j=0,k=0;
		Point[] merged=new Point[left.length+right.length];
		while(i<left.length && j<right.length) {
			if(pointComparator.compare(left[i], right[j])!=1) {
				merged[k]=left[i];
				i++;
				k++;
			}
			else {
				merged[k]=right[j];
				k++;
				j++;
			}
		}
		if(i>=left.length) {
			while(j<right.length) {
				merged[k]=right[j];
				j++;
				k++;
			}
		}
		else if(j>=right.length) {
			while(i<left.length) {
				merged[k]=left[i];
				i++;
				k++;
			}
		}
		return merged;
	}

}
