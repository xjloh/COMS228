package edu.iastate.cs228.hw5;


import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * The Transactions class simulates video transactions at a video store. 
 *
 */
public class Transactions 
{
	
	/**
	 * The main method generates a simulation of rental and return activities.  
	 *  
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{	
		// TODO 
		// 
		// 1. Construct a VideoStore object.
		// 2. Simulate transactions as in the example given in Section 4 of the 
		//    the project description. 
		VideoStore vs=new VideoStore("videoList1.txt");
		System.out.println("Transactions at a Video Store");
		System.out.println("keys: 1 (rent)     2 (bulk rent)");
		System.out.println("      3 (return)   4 (bulk return)");
		System.out.println("      5 (summary)  6 (exit)");
		System.out.println();
		Scanner in=new Scanner(System.in);
		System.out.print("Transaction: ");
		int decision=in.nextInt();
		while(decision>0 && decision<6) {
			switch(decision) {
			case 1: 
				System.out.print("Film to rent: ");
				in.nextLine();
				String rent=in.nextLine();
				String title=vs.parseFilmName(rent);
				int copy=vs.parseNumCopies(rent);
				Video film=new Video(title, copy);
				try {
					vs.videoRent(film.getFilm(), film.getNumCopies());
				}
				catch(FilmNotInInventoryException | IllegalArgumentException | AllCopiesRentedOutException x) {
					System.out.println(x.getMessage());
				}	
				break;
			case 2:
				System.out.print("Video file (rent): ");
				in.nextLine();
				String vFile=in.nextLine();
				try {
					vs.bulkRent(vFile);
				}
				catch(FilmNotInInventoryException | IllegalArgumentException | AllCopiesRentedOutException x) {
					System.out.println(x.getMessage());
				}	
				break;
			case 3:
				System.out.print("Film to return: ");
				in.nextLine();
				String fReturn=in.nextLine();
				String title1=vs.parseFilmName(fReturn);
				int copy1=vs.parseNumCopies(fReturn);
				Video toReturn=new Video(title1, copy1);
				try {
					vs.videoReturn(toReturn.getFilm(), toReturn.getNumCopies());
				}
				catch(FilmNotInInventoryException | IllegalArgumentException x) {
					System.out.println(x.getMessage());
				}	
				break;
			case 4:
				System.out.print("Video file (return): ");
				in.nextLine();
				String rFile=in.nextLine();
				try {
					vs.bulkReturn(rFile);
				}
				catch(FilmNotInInventoryException | IllegalArgumentException x) {
					System.out.println(x.getMessage());
				}	
				break;
			case 5:
				System.out.println();
				System.out.println(vs.transactionsSummary());
				break;
			}
			System.out.println();
			System.out.print("Transaction: ");
			decision=in.nextInt();

			}
		in.close();
	}
}
