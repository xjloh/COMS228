package edu.iastate.cs228.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import edu.iastate.cs228.hw5.SplayTree.Node; 

/**
 * 
 * @author Loh Xin Jun 
 *
 */

public class VideoStore 
{
	protected SplayTree<Video> inventory=new SplayTree<>();     // all the videos at the store
	
	// ------------
	// Constructors 
	// ------------
	
    /**
     * Default constructor sets inventory to an empty tree. 
     */
    public VideoStore()
    {
    	// no need to implement. 
    }
    
    
	/**
	 * Constructor accepts a video file to create its inventory.  Refer to Section 3.2 of  
	 * the project description for details regarding the format of a video file. 
	 * 
	 * Calls setUpInventory(). 
	 * 
	 * @param videoFile  no format checking on the file
	 * @throws FileNotFoundException
	 */
    public VideoStore(String videoFile) throws FileNotFoundException  
    {
    	// TODO 
    	inventory=new SplayTree();
    	setUpInventory(videoFile);
    }
    
    
   /**
     * Accepts a video file to initialize the splay tree inventory.  To be efficient, 
     * add videos to the inventory by calling the addBST() method, which does not splay. 
     * 
     * Refer to Section 3.2 for the format of video file. 
     * 
     * @param  videoFile  correctly formated if exists
     * @throws FileNotFoundException 
     */
    public void setUpInventory(String videoFile) throws FileNotFoundException
    {
    	// TODO 
    	if(inventory!=null) {//to initialize the splay tree inventory
    		inventory.clear();
    	}
    	//I use bulkImport because the instruction is similar
    	bulkImport(videoFile);//import videoFile
    }
	
    
    // ------------------
    // Inventory Addition
    // ------------------
    
    /**
     * Find a Video object by film title. 
     * 
     * @param film
     * @return
     */
	public Video findVideo(String film) 
	{
		// TODO 
		Video temp=new Video(film);
		Video fv=inventory.findElement(temp);
		return fv; 
	}


	/**
	 * Updates the splay tree inventory by adding a number of video copies of the film.  
	 * (Splaying is justified as new videos are more likely to be rented.) 
	 * 
	 * Calls the add() method of SplayTree to add the video object.  
	 * 
	 *     a) If true is returned, the film was not on the inventory before, and has been added.  
	 *     b) If false is returned, the film is already on the inventory. 
	 *     
	 * The root of the splay tree must store the corresponding Video object for the film. Update 
	 * the number of copies for the film.  
	 * 
	 * @param film  title of the film
	 * @param n     number of video copies 
	 */
	public void addVideo(String film, int n)  
	{
		// TODO 
		Video vid=new Video(film, n);
		boolean tf=inventory.add(vid);
		if(!tf) {//updating the number of copies 
			Video temp=inventory.findElement(vid);
			temp.addNumCopies(n);
		}
	}
	

	/**
	 * Add one video copy of the film. 
	 * 
	 * @param film  title of the film
	 */
	public void addVideo(String film)
	{
		// TODO 
		addVideo(film, 1);
	}
	

	/**
     * Update the splay trees inventory by adding videos.  Perform binary search additions by 
     * calling addBST() without splaying. 
     * 
     * The videoFile format is given in Section 3.2 of the project description. 
     * 
     * @param videoFile  correctly formated if exists 
     * @throws FileNotFoundException
     */
    public void bulkImport(String videoFile) throws FileNotFoundException 
    {
    	// TODO 
    	File vFile=new File(videoFile);
    	Scanner scan=new Scanner(vFile);
    	while(scan.hasNextLine()) {
    		String line=scan.nextLine();
    		String title=parseFilmName(line);
    		int numCopies=parseNumCopies(line);
    		if(numCopies<=0) {//A negative number or zero following a film title is treated as one
    			numCopies=1;
    		}
    		Video obj=new Video(title, numCopies);
    		inventory.addBST(obj);
    	}
    	scan.close();
    }

    
    // ----------------------------
    // Video Query, Rental & Return 
    // ----------------------------
    
	/**
	 * Search the splay tree inventory to determine if a video is available. 
	 * 
	 * @param  film
	 * @return true if available
	 */
	public boolean available(String film)
	{
		// TODO 
		Video obj=new Video(film);
		if(null==inventory.root) {//if inventory is empty
			return false;
		}
		if(inventory.findElement(obj).getNumAvailableCopies()>0) {//available if available copies>0
			return true;
		}
		else {
			return false;
		}
	}

	
	
	/**
     * Update inventory. 
     * 
     * Search if the film is in inventory by calling findElement(new Video(film, 1)). 
     * 
     * If the film is not in inventory, prints the message "Film <film> is not 
     * in inventory", where <film> shall be replaced with the string that is the value 
     * of the parameter film.  If the film is in inventory with no copy left, prints
     * the message "Film <film> has been rented out".
     * 
     * If there is at least one available copy but n is greater than the number of 
     * such copies, rent all available copies. In this case, no AllCopiesRentedOutException
     * is thrown.  
     * 
     * @param film   
     * @param n 
     * @throws IllegalArgumentException      if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException   if film is not in the inventory
	 * @throws AllCopiesRentedOutException   if there is zero available copy for the film.
	 */
	public void videoRent(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException,  
									     			 AllCopiesRentedOutException 
	{
		// TODO 
		if(n<=0 || film==null || film.isEmpty()) {
			throw new IllegalArgumentException("Film " + film + " has an invalid request");
		}
		Video obj=new Video(film, n);
		if(inventory!=null) {
			Video temp=inventory.findElement(obj);
			if(temp==null) {//if temp is not in inventory
				throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
			}
			else if(temp.getNumCopies()==temp.getNumRentedCopies()) {//no more copies available
				throw new AllCopiesRentedOutException("Film " + film + " has been rented out");
			}
			else {
				if(temp.getNumAvailableCopies()<=n) {//n is greater than the number of such copies, rent all available copies
					temp.rentCopies(temp.getNumAvailableCopies());
				}
				else {
					temp.rentCopies(n);
				}
			}
		}
	}

	
	/**
	 * Update inventory.
	 * 
	 *    1. Calls videoRent() repeatedly for every video listed in the file.  
	 *    2. For each requested video, do the following: 
	 *       a) If it is not in inventory or is rented out, an exception will be 
	 *          thrown from videoRent().  Based on the exception, prints out the following 
	 *          message: "Film <film> is not in inventory" or "Film <film> 
	 *          has been rented out." In the message, <film> shall be replaced with 
	 *          the name of the video. 
	 *       b) Otherwise, update the video record in the inventory.
	 * 
	 * For details on handling of multiple exceptions and message printing, please read Section 3.4 
	 * of the project description. 
	 *       
	 * @param videoFile  correctly formatted if exists
	 * @throws FileNotFoundException
     * @throws IllegalArgumentException     if the number of copies of any film is <= 0
	 * @throws FilmNotInInventoryException  if any film from the videoFile is not in the inventory 
	 * @throws AllCopiesRentedOutException  if there is zero available copy for some film in videoFile
	 */
	public void bulkRent(String videoFile) throws FileNotFoundException, IllegalArgumentException, 
												  FilmNotInInventoryException, AllCopiesRentedOutException 
	{
		// TODO 
		String msg="";//to concatenate the exception messages
		File file=new File(videoFile);
		Scanner fScan=new Scanner(file);
		boolean[] errors=new boolean[3];//for handling exceptions with different rankings
		while(fScan.hasNextLine()) {
			String line=fScan.nextLine();
			String title=parseFilmName(line);
			int numCopy=parseNumCopies(line);
			Video vid=new Video(title, numCopy);
			try {
				videoRent(vid.getFilm(), vid.getNumCopies());
			}
			catch(IllegalArgumentException x) {//second rank exception
				errors[0]=true;
				msg+=x.getMessage() + "\n";
			}
			catch(FilmNotInInventoryException x) {//third rank exception
				errors[1]=true;
				msg+=x.getMessage() + "\n";
			}
			catch(AllCopiesRentedOutException x) {//fourth rank exception
				errors[2]=true;
				msg+=x.getMessage() + "\n";
			}
		}
		//the exception with the highest rank is thrown 
		if(errors[0]) {
			fScan.close();
			throw new IllegalArgumentException(msg.trim());
		}
		if(errors[1]) {
			fScan.close();
			throw new FilmNotInInventoryException(msg.trim());
		}
		if(errors[2]) {
			fScan.close();
			throw new AllCopiesRentedOutException(msg.trim());
		}
		fScan.close();
	}

	
	/**
	 * Update inventory.
	 * 
	 * If n exceeds the number of rented video copies, accepts up to that number of rented copies
	 * while ignoring the extra copies. 
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException     if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException  if film is not in the inventory
	 */
	public void videoReturn(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException 
	{
		// TODO
		if(n<=0 || film==null || film.isEmpty()) {
			throw new IllegalArgumentException("Film " + film + " has an invalid request");
		}
		Video temp=new Video(film, n);
		Video movie=inventory.findElement(temp);
		if(movie==null) {
			throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		}
		else {
			if(movie.getNumRentedCopies() < n) {//If n exceeds the number of rented video copies, accepts up to that number of rented copies while ignoring the extra copies.
				movie.returnCopies(movie.getNumCopies());
			}
			else {
				movie.returnCopies(n);
			}
		}
	}
	
	
	/**
	 * Update inventory. 
	 * 
	 * Handles excessive returned copies of a film in the same way as videoReturn() does.  See Section 
	 * 3.4 of the project description on how to handle multiple exceptions. 
	 * 
	 * @param videoFile
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of return copies of any film is <= 0
	 * @throws FilmNotInInventoryException if a film from videoFile is not in inventory
	 */
	public void bulkReturn(String videoFile) throws FileNotFoundException, IllegalArgumentException,
													FilmNotInInventoryException												
	{
		// TODO 
		String msg="";//for concatenate the messages of all the exceptions into one string
		File file=new File(videoFile);
		Scanner fscan=new Scanner(file);
		boolean[] errors=new boolean[2];//for handling exception with different ranking
		while(fscan.hasNextLine()) {
			String line=fscan.nextLine();
			String title=parseFilmName(line);
			int num=parseNumCopies(line);
			Video temp=new Video(title, num);
			try {
				videoReturn(temp.getFilm(), temp.getNumCopies());
			}
			catch(IllegalArgumentException x) {//rank second exception
				errors[0]=true;
				msg+=x.getMessage() + "\n";
			}
			catch(FilmNotInInventoryException x) {//rank third exception
				errors[1]=true;
				msg+=x.getMessage() + "\n";
			}
		}
		//the exception with the highest rank is thrown
		if(errors[0]) {
			fscan.close();
			throw new IllegalArgumentException(msg.trim());
		}
		if(errors[1]) {
			fscan.close();
			throw new FilmNotInInventoryException(msg.trim());
		}
		fscan.close();
	}
		
	

	// ------------------------
	// Methods without Splaying
	// ------------------------
		
	/**
	 * Performs inorder traversal on the splay tree inventory to list all the videos by film 
	 * title, whether rented or not.  Below is a sample string if printed out: 
	 * 
	 * 
	 * Films in inventory: 
	 * 
	 * A Streetcar Named Desire (1) 
	 * Brokeback Mountain (1) 
	 * Forrest Gump (1)
	 * Psycho (1) 
	 * Singin' in the Rain (2)
	 * Slumdog Millionaire (5) 
	 * Taxi Driver (1) 
	 * The Godfather (1) 
	 * 
	 * 
	 * @return
	 */
	public String inventoryList()
	{
		// TODO 
		String list="Films in inventory:" +"\n"+"\n";
		Iterator<Video> it=inventory.iterator();
		Node cur=inventory.root;
		//for inorder traversal, first traverses the left subtree, 
		//then visit the root, and
		//finally traverses the right subtree
		while(cur.left!=null) {//start from the smallest
			cur=cur.left;
		}
		while(it.hasNext()) {
			Video movie=it.next();
			String title=movie.getFilm();
			int copy=movie.getNumCopies();
			list+= title +" "+ "(" + copy + ")" +"\n";
		}
		return list; 
	}

	
	/**
	 * Calls rentedVideosList() and unrentedVideosList() sequentially.  For the string format, 
	 * see Transaction 5 in the sample simulation in Section 4 of the project description. 
	 *   
	 * @return 
	 */
	public String transactionsSummary()
	{
		// TODO 
		String sum=rentedVideosList()+"\n";
		sum+=unrentedVideosList();
		return sum; 
	}	
	
	/**
	 * Performs inorder traversal on the splay tree inventory.  Use a splay tree iterator.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * Rented films: 
	 * 
	 * Brokeback Mountain (1)
	 * Forrest Gump (1) 
	 * Singin' in the Rain (2)
	 * The Godfather (1)
	 * 
	 * 
	 * @return
	 */
	private String rentedVideosList()
	{
		// TODO 
		String list="Rented films:" +"\n" + "\n";
		Node cur=inventory.root;
		Iterator<Video> it=inventory.iterator();
		//for inorder traversal, first traverses the left subtree, 
		//then visit the root, and
		//finally traverses the right subtree
		while(cur.left!=null) {//start from smallest element
			cur=cur.left;
		}
		while(it.hasNext()) {
			Video movie=it.next();
			if(movie.getNumRentedCopies()>0) {
				String title=movie.getFilm();
				int copy=movie.getNumRentedCopies();
				list+= title +" "+ "(" + copy + ")" +"\n";
			}
		}
		return list; 
	}

	
	/**
	 * Performs inorder traversal on the splay tree inventory.  Use a splay tree iterator.
	 * Prints only the films that have unrented copies. 
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * 
	 * Films remaining in inventory:
	 * 
	 * A Streetcar Named Desire (1) 
	 * Forrest Gump (1)
	 * Psycho (1) 
	 * Slumdog Millionaire (4) 
	 * Taxi Driver (1) 
	 * 
	 * 
	 * @return
	 */
	private String unrentedVideosList()
	{
		// TODO 
		String list="Films remaining in inventory:" +"\n" +"\n";
		Iterator<Video> it=inventory.iterator();
		Node cur=inventory.root;
		//for inorder traversal, first traverses the left subtree, 
		//then visit the root, and
		//finally traverses the right subtree
		while(cur.left!=null) {//start from smallest element
			cur=cur.left;
		}
		while(it.hasNext()) {
			Video movie=it.next();
			if(movie.getNumAvailableCopies()>0) {
				String title=movie.getFilm();
				int copies=movie.getNumAvailableCopies();
				list+= title +" "+ "(" + copies + ")" +"\n";
			}
		}
		return list.trim(); 
	}	

	
	/**
	 * Parse the film name from an input line. 
	 * 
	 * @param line
	 * @return
	 */
	public static String parseFilmName(String line) 
	{
		// TODO 
		Scanner scan=new Scanner(line);
		String title="";
		while(scan.hasNext()) {
			String s=scan.next();
			if(s.charAt(0)=='(') {//breaks when (
				break;
			}
			title+=s;
			title+=" ";
		}
		scan.close();
		title=title.trim();
		return title; 
	}
	
	
	/**
	 * Parse the number of copies from an input line. 
	 * 
	 * @param line
	 * @return
	 */
	public static int parseNumCopies(String line) 
	{
		// TODO 
		Scanner scan = new Scanner(line);
		while (scan.hasNext()) {
			String s = scan.next();
			String val = "";
			int index = 1;
			char[] chars = s.toCharArray();//converts s to array 
			if (s.charAt(0) == '(' && !scan.hasNext()) {
				while (chars[index] != ')') {//scans the number of copies
					val+=chars[index];
					++index;
				}
				scan.close();
				int temp=Integer.parseInt(val);
				return temp;
			}
		}
		scan.close();
		return 1;//if there is nothing after film title, return 1.
	}
}
