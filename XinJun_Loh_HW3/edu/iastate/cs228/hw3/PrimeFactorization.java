package edu.iastate.cs228.hw3;

import java.util.ArrayList;

/**
 *  
 * @author Loh Xin Jun
 *
 */

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PrimeFactorization implements Iterable<PrimeFactor>
{
	private static final long OVERFLOW = -1;
	private long value; 	// the factored integer 
							// it is set to OVERFLOW when the number is greater than 2^63-1, the
						    // largest number representable by the type long. 
	
	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;
	  
	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;
	
	private int size;     	// number of distinct prime factors


	// ------------
	// Constructors 
	// ------------
	
    /**
	 *  Default constructor constructs an empty list to represent the number 1.
	 *  
	 *  Combined with the add() method, it can be used to create a prime factorization.  
	 */
	public PrimeFactorization() 
	{	 
		// TODO 
		head=new Node();
		tail=new Node();
		head.next=tail;
		tail.previous=head;
		value=1;
		size=0;
	}

	
	/** 
	 * Obtains the prime factorization of n and creates a doubly linked list to store the result.   
	 * Follows the direct search factorization algorithm in Section 1.2 of the project description. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException 
	{
		// TODO 
		if(n<1) {
			throw new IllegalArgumentException();
		}
		head=new Node();
		tail=new Node();
		head.next=tail;
		tail.previous=head;
		value=n;
		Node temp;
		int power=0, prime=0;
		long m=n;
		for(int d=2; d*d<=m; ++d) {//direct search factorization
			if(isPrime(d)) {
				if(m%d==0) {
					prime=d;
					power=1;
					m=m/d;
					while(m%d==0) {
						power++;
						m=m/d;
					}
					temp=new Node(new PrimeFactor(prime, power));
					link(tail, temp);
					size++;
				}
			}
		}
		if(m>1) {//the remainder
			PrimeFactor pf=new PrimeFactor((int)m, 1);
			Node newNode=new Node(pf);
			link(tail, newNode);
			size++;
		}
	}
	
	
	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf)
	{
		// TODO
		Node temp;
		head=new Node();
		tail=new Node();
		size=pf.size;
		tail.previous=head;
		head.next=tail;
		PrimeFactorizationIterator iter1=pf.iterator();
		while(iter1.hasNext()) {
			temp=new Node(iter1.next());
			link(tail, temp);
		}
	}
	
	/**
	 * Constructs a factorization from an array of prime factors.  Useful when the number is 
	 * too large to be represented even as a long integer. 
	 * 
	 * @param pflist
	 */
	public PrimeFactorization (PrimeFactor[] pfList)
	{
		// TODO 
		this();
		size=pfList.length;
		for(int j=0; j<pfList.length; j++) {
			add(pfList[j].prime, pfList[j].multiplicity);
		}
	}
	
	

	// --------------
	// Primality Test
	// --------------
	
    /**
	 * Test if a number is a prime or not.  Check iteratively from 2 to the largest 
	 * integer not exceeding the square root of n to see if it divides n. 
	 * 
	 *@param n
	 *@return true if n is a prime 
	 * 		  false otherwise 
	 */
    public static boolean isPrime(long n) 
	{
	    // TODO 
    	if(n==2) {
    		return true;
    	}
    	else if(n<2 || n%2==0) {
    		return false;
    	}
    	for(int k=2; k*k<=n; k++) {
    		if(n%k==0) {
    			return false;
    		}
    	}
		return true; 
	}   

   
	// ---------------------------
	// Multiplication and Division 
	// ---------------------------
	
	/**
	 * Multiplies the integer v represented by this object with another number n.  Note that v may 
	 * be too large (in which case this.value == OVERFLOW). You can do this in one loop: Factor n and 
	 * traverse the doubly linked list simultaneously. For details refer to Section 3.1 in the 
	 * project description. Store the prime factorization of the product. Update value and size. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException 
	{
		// TODO
		if(n<1) {
			throw new IllegalArgumentException();
		}
		PrimeFactorizationIterator iter1=this.iterator();
		Node temp;
		int power=0;
		long m=n;
		for(int d=2; d*d<=m; d++) {//direct search factorization
			if(isPrime(d)) {
				if(m%d==0) {
					power=1;
					int prime=d;
					m=m/d;
					while(m%d==0) {
						power++;
						m=m/d;
					}
					temp=new Node(new PrimeFactor(prime, power));
					if(tail==iter1.cursor) {//if list is empty
						link(tail, temp);
						size++;
					}
					else {
						while(temp.pFactor.prime>iter1.cursor.pFactor.prime) {
							iter1.next();
						}
						if(temp.pFactor.prime==iter1.cursor.pFactor.prime) {
							iter1.cursor.pFactor.multiplicity+=temp.pFactor.multiplicity;
						}
						else if(temp.pFactor.prime<iter1.cursor.pFactor.prime) {
							iter1.add(temp.pFactor);
						}
					}
				}
			}
		}
		updateValue();
	}
	
	/**
	 * Multiplies the represented integer v with another number in the factorization form.  Traverse both 
	 * linked lists and store the result in this list object.  See Section 3.1 in the project description 
	 * for details of algorithm. 
	 * 
	 * @param pf 
	 */
	public void multiply(PrimeFactorization pf)
	{
		// TODO
		PrimeFactorizationIterator iterPf=pf.iterator();//traverse the 2 linked list
		PrimeFactorizationIterator iterThis=this.iterator();
		while(iterThis.hasNext()&&iterPf.hasNext()) {
				while(iterPf.cursor.pFactor.prime>iterThis.cursor.pFactor.prime) {
					iterThis.next();
				}
				if(iterPf.cursor.pFactor.prime<iterThis.cursor.pFactor.prime) {
					iterThis.add(iterPf.cursor.pFactor);
					iterPf.next();
				}
				else if(iterPf.cursor.pFactor.prime==iterThis.cursor.pFactor.prime) {
					iterThis.cursor.pFactor.multiplicity+=iterPf.cursor.pFactor.multiplicity;
					iterPf.next();
				}
		}
		while(iterPf.hasNext() && !iterThis.hasNext()) {
			iterThis.add(iterPf.cursor.pFactor);
			iterPf.next();
		}
		updateValue();
	}
	
	
	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.  
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product 
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2)
	{
		// TODO 
		PrimeFactorization product=pf1;
		product.multiply(pf1);
		product.multiply(pf2);
		return product; 
	}

	
	/**
	 * Divides the represented integer v by n.  Make updates to the list, value, size if divisible.  
	 * No update otherwise. Refer to Section 3.2 in the project description for details. 
	 *  
	 * @param n
	 * @return  true if divisible 
	 *          false if not divisible 
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException
	{
		// TODO 
		if(n<=0) {
			throw new IllegalArgumentException();
		}
		else if(this.value !=-1 && this.value<n) {
			return false;
		}
		else if(n==this.value) {
			this.clearList();
			value=1;
			return true;
		}
		PrimeFactorization obj=new PrimeFactorization(n);
		this.dividedBy(obj);
		updateValue();
		return true;
	}

	
	/**
	 * Division where the divisor is represented in the factorization form.  Update the linked 
	 * list of this object accordingly by removing those nodes housing prime factors that disappear  
	 * after the division.  No update if this number is not divisible by pf. Algorithm details are 
	 * given in Section 3.2. 
	 * 
	 * @param pf
	 * @return	true if divisible by pf
	 * 			false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf)
	{
		// TODO 
		if(this.value!=-1 && pf.value!=-1 && this.value<pf.value) {
			return false;
		}
		if(this.value!=-1 && pf.value==-1) {
			return false;
		}
		if(this.value==pf.value) {
			clearList();
			value=1;
			return true;
		}
		PrimeFactorization copyPf=new PrimeFactorization(this);
		PrimeFactorizationIterator iterCopy=copyPf.iterator();
		PrimeFactorizationIterator iterPf=pf.iterator();
		while(iterCopy.hasNext() && iterPf.hasNext()) {
			while(iterCopy.cursor.pFactor.prime<iterPf.cursor.pFactor.prime) {
				iterCopy.next();
			}
			if(iterCopy.cursor.pFactor.prime>iterPf.cursor.pFactor.prime) {
				return false;
			}
			else if(iterCopy.cursor.pFactor.prime==iterPf.cursor.pFactor.prime && iterCopy.cursor.pFactor.multiplicity<iterPf.cursor.pFactor.multiplicity) {
				return false;
			}
			else if(iterCopy.cursor.pFactor.prime==iterPf.cursor.pFactor.prime && iterCopy.cursor.pFactor.multiplicity>=iterPf.cursor.pFactor.multiplicity) {
				iterCopy.cursor.pFactor.multiplicity=iterCopy.cursor.pFactor.multiplicity-iterPf.cursor.pFactor.multiplicity;
				if(iterCopy.cursor.pFactor.multiplicity==0) {
					copyPf.remove(iterCopy.cursor.pFactor.prime, iterCopy.cursor.pFactor.multiplicity);
					iterCopy.next();
					iterPf.next();
				}
			}
		}
		this.head=copyPf.head;
		this.tail=copyPf.tail;
		this.size=copyPf.size;
		this.updateValue();
		return true;
	}

	
	/**
	 * Divide the integer represented by the object pf1 by that represented by the object pf2. 
	 * Return a new object representing the quotient if divisible. Do not make changes to pf1 and 
	 * pf2. No update if the first number is not divisible by the second one. 
	 *  
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible
	 *         null otherwise 
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2)
	{
		// TODO 
		PrimeFactorization quotientPF=pf1;
		quotientPF.dividedBy(pf1);
		quotientPF.dividedBy(pf2);
		return quotientPF;
	}

	
	// -------------------------------------------------
	// Greatest Common Divisor and Least Common Multiple 
	// -------------------------------------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and an input integer n.
	 * Returns the result as a PrimeFactor object.  Calls the method Euclidean() if 
	 * this.value != OVERFLOW.
	 *     
	 * It is more efficient to factorize the gcd than n, which can be much greater. 
	 *     
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException
	{
		// TODO 
		long gcd=0;
		if(n<1) {
			throw new IllegalArgumentException();
		}
		if(this.value!=OVERFLOW) {
			gcd=Euclidean(this.value, n);
		}
		PrimeFactorization result=new PrimeFactorization(gcd);
		return result; 
	}
	

	/**
	  * Implements the Euclidean algorithm to compute the gcd of two natural numbers m and n. 
	  * The algorithm is described in Section 4.1 of the project description. 
	  * 
	  * @param m
	  * @param n
	  * @return gcd of m and n. 
	  * @throws IllegalArgumentException if m < 1 or n < 1
	  */
 	public static long Euclidean(long m, long n) throws IllegalArgumentException
	{
 		// TODO 
 		if(m<1 || n<1) {
 			throw new IllegalArgumentException();
 		}
 		long big=Math.max(m, n);
 		long small=Math.min(m, n);
 		while(small!=0) {
 			long division=big%small;
 			big=small;
 			small=division;
 		}
 		return big; 
	}

 	
	/**
	 * Computes the gcd of the values represented by this object and pf by traversing the two lists.  No 
	 * direct computation involving value and pf.value. Refer to Section 4.2 in the project description 
	 * on how to proceed.  
	 * 
	 * @param  pf
	 * @return prime factorization of the gcd
	 * @throws IllegalArgumentException if pf == null
	 */
	public PrimeFactorization gcd(PrimeFactorization pf)throws IllegalArgumentException
	{
		// TODO 
		if(pf==null) {
			throw new IllegalArgumentException();
		}
		//traverse the two list
		PrimeFactorizationIterator iterPf=pf.iterator();
		PrimeFactorizationIterator iterThis=this.iterator();
		PrimeFactorization result=new PrimeFactorization();//object to represent the gcd
		while(iterPf.hasNext()) {
			PrimeFactor pfPf=iterPf.next();
			PrimeFactor thisPf=iterThis.next();
			while(iterThis.hasNext() && pfPf.prime!=thisPf.prime) {
				thisPf=iterThis.next();
			}
			if(pfPf.prime==thisPf.prime) {
				result.add(thisPf.prime, Math.min(thisPf.multiplicity, pfPf.multiplicity));
			}
			iterThis=this.iterator();//continues iterating
		}
		return result; 
	}
	
	
	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and pf2
	 * @throws IllegalArgumentException if pf1 == null or pf2 == null
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2)throws IllegalArgumentException
	{
		// TODO 
		if(pf1==null || pf2==null) {
			throw new IllegalArgumentException();
		}
		PrimeFactorization temp=pf1;
		temp.gcd(pf1);
		temp.gcd(pf2);
		return temp; 
	}

	
	/**
	 * Computes the least common multiple (lcm) of the two integers represented by this object 
	 * and pf.  The list-based algorithm is given in Section 4.3 in the project description. 
	 * 
	 * @param pf  
	 * @return factored least common multiple  
	 * @throws IllegalArgumentException if pf == null 
	 */
	public PrimeFactorization lcm(PrimeFactorization pf)throws IllegalArgumentException
	{
		// TODO 
		if(pf==null) {
			throw new IllegalArgumentException();
		}
		PrimeFactorization result=new PrimeFactorization();
		PrimeFactorizationIterator iterPf=pf.iterator();
		PrimeFactorizationIterator iterThis=this.iterator();
		while(iterPf.hasNext() && iterThis.hasNext()) {
			if(iterPf.cursor.pFactor.prime!=iterThis.cursor.pFactor.prime) {
				if(iterPf.cursor.pFactor.prime > iterThis.cursor.pFactor.prime) {
					PrimeFactor temp=new PrimeFactor(iterPf.cursor.pFactor.prime, iterPf.cursor.pFactor.multiplicity);
					result.add(temp.prime, temp.multiplicity);
					iterPf.next();
				}
				else {
					PrimeFactor temp1=new PrimeFactor(iterThis.cursor.pFactor.prime, iterThis.cursor.pFactor.multiplicity);
					result.add(temp1.prime, temp1.multiplicity);
					iterThis.next();
				}
			}
			else if(iterPf.cursor.pFactor.prime==iterThis.cursor.pFactor.prime) {
				PrimeFactor temp2=new PrimeFactor(iterPf.cursor.pFactor.prime, Math.max(iterPf.cursor.pFactor.multiplicity, iterThis.cursor.pFactor.multiplicity));
				Node max=new Node(temp2);
				result.add(max.pFactor.prime, max.pFactor.multiplicity);
				iterThis.next();
				iterPf.next();
			}
		}
		while(iterPf.hasNext() && !iterThis.hasNext()) {
			result.add(iterPf.cursor.pFactor.prime, iterPf.cursor.pFactor.multiplicity);
			iterPf.next();
		}
		while(!iterPf.hasNext() && iterThis.hasNext()) {
			result.add(iterThis.cursor.pFactor.prime, iterThis.cursor.pFactor.multiplicity);
			iterThis.next();
		}
		result.updateValue();
		return result; 
	}

	
	/**
	 * Computes the least common multiple of the represented integer v and an integer n. Construct a 
	 * PrimeFactors object using n and then call the lcm() method above.  Calls the first lcm() method. 
	 * 
	 * @param n
	 * @return factored least common multiple 
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization lcm(long n) throws IllegalArgumentException 
	{
		// TODO 
		if(n<1) {
			throw new IllegalArgumentException();
		}
		PrimeFactorization temp=new PrimeFactorization(n);
		PrimeFactorization result=lcm(temp);
		return result; 
	}

	/**
	 * Computes the least common multiple of the integers represented by pf1 and pf2. 
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the lcm of two numbers represented by pf1 and pf2
	 * @throws IllegalArgumentException if pf1 == null or pf2 == null 
	 */
	public static PrimeFactorization lcm(PrimeFactorization pf1, PrimeFactorization pf2)throws IllegalArgumentException
	{
		// TODO 
		if(pf1==null || pf2==null) {
			throw new IllegalArgumentException();
		}
		PrimeFactorization temp=pf1;
		temp.lcm(pf1);
		temp.lcm(pf2);
		return temp; 
	}

	
	// ------------
	// List Methods
	// ------------
	
	/**
	 * Traverses the list to determine if p is a prime factor. 
	 * 
	 * Precondition: p is a prime. 
	 * 
	 * @param p  
	 * @return true  if p is a prime factor of the number v represented by this linked list
	 *         false otherwise 
	 * @throws IllegalArgumentException if p is not a prime //no need
	 */
	public boolean containsPrimeFactor(int p)
	{
		// TODO 
		PrimeFactorizationIterator temp=new PrimeFactorizationIterator();
		while(temp.hasNext()) {
			if(p==temp.next().prime) {
				return true;
			}
		}
		return false;
	}
	
	// The next two methods ought to be private but are made public for testing purpose. Keep
	// them public 
	
	/**
	 * Adds a prime factor p of multiplicity m.  Search for p in the linked list.  If p is found at 
	 * a node N, add m to N.multiplicity.  Otherwise, create a new node to store p and m. 
	 *  
	 * Precondition: p is a prime. 
	 * 
	 * @param p  prime 
	 * @param m  multiplicity
	 * @return   true  if m >= 1
	 *           false if m < 1   
	 */
    public boolean add(int p, int m) 
    {
    	// TODO 
    	if(m<1) {
    		return false;
    	}
    	PrimeFactorizationIterator temp=this.iterator();
    	while(temp.hasNext()) {
    		PrimeFactor pf=temp.next();
    		if(p==pf.prime) {
    			temp.cursor.pFactor.multiplicity+=m;
    			updateValue();
    			return true;
    		}
    	}
    	Node newNode=new Node(p, m);
    	link(temp.cursor, newNode);
    	size++;
    	updateValue();
    	return true;
    }

	    
    /**
     * Removes m from the multiplicity of a prime p on the linked list.  It starts by searching 
     * for p.  Returns false if p is not found, and true if p is found. In the latter case, let 
     * N be the node that stores p. If N.multiplicity > m, subtracts m from N.multiplicity.  
     * If N.multiplicity <= m, removes the node N.  
     * 
     * Precondition: p is a prime. 
     * 
     * @param p
     * @param m
     * @return true  when p is found. 
     *         false when p is not found. 
     * @throws IllegalArgumentException if m < 1
     */
    public boolean remove(int p, int m) throws IllegalArgumentException
    {
    	// TODO 
    	if(m<1) {
    		throw new IllegalArgumentException();
    	}
    	PrimeFactorizationIterator temp=this.iterator();
    	while(temp.hasNext()) {
    		PrimeFactor pf=temp.next();
    		if(pf.prime==p) {
    			if(pf.multiplicity>m) {
    				pf.multiplicity=pf.multiplicity-m;
    				updateValue();
    			}
    			else if(pf.multiplicity<=m) {
    				temp.remove();
    				updateValue();
    			}
    			return true;
    		}
    	}
    	return false; 
    }


    /**
     * 
     * @return size of the list
     */
	public int size() 
	{
		// TODO
		return size; 
	}

	
	/**
	 * Writes out the list as a factorization in the form of a product. Represents exponentiation 
	 * by a caret.  For example, if the number is 5814, the returned string would be printed out 
	 * as "2 * 3^2 * 17 * 19". 
	 */
	@Override 
	public String toString()
	{
		// TODO 
		String s="";
		if(value==1) {
			return s+=1;
		}
		PrimeFactorizationIterator temp=this.iterator();
		s+=temp.next().toString();
		while(temp.hasNext()) {
			s+=" * "+temp.next().toString();
		}
		return s; 
	}

	
	// The next three methods are for testing, but you may use them as you like.  

	/**
	 * @return true if this PrimeFactorization is representing a value that is too large to be within 
	 *              long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if valueOverflow()
	 */
	public long value() {
		return value;
	}

	
	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}


	
	@Override
	public PrimeFactorizationIterator iterator()
	{
	    return new PrimeFactorizationIterator();
	}
	
	/**
	 * Doubly-linked node type for this class.
	 */
    private class Node 
    {
		public PrimeFactor pFactor;			// prime factor 
		public Node next;
		public Node previous;
		
		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node()
		{
			// TODO 
			next=null;
			previous=null;
			pFactor=null;
		}
	    
		/**
		 * Precondition: p is a prime
		 * 
		 * @param p	 prime number 
		 * @param m  multiplicity 
		 * @throws IllegalArgumentException if m < 1 
		 */
		public Node(int p, int m) throws IllegalArgumentException 
		{	
			// TODO 
			if(m<1) {
				throw new IllegalArgumentException();
			}
			this.pFactor=new PrimeFactor(p, m);
		}   

		
		/**
		 * Constructs a node over a provided PrimeFactor object. 
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf)  
		{
			// TODO 
			if(pf==null) {
				throw new IllegalArgumentException();
			}
			pFactor=new PrimeFactor(pf.prime, pf.multiplicity);
		}


		/**
		 * Printed out in the form: prime + "^" + multiplicity.  For instance "2^3". 
		 * Also, deal with the case pFactor == null in which a string "dummy" is 
		 * returned instead.  
		 */
		@Override
		public String toString() 
		{
			// TODO 
			if(pFactor==null) {
				return "dummy";
			}
			return pFactor.toString();
		}
    }

    
    private class PrimeFactorizationIterator implements ListIterator<PrimeFactor>
    {  	
        // Class invariants: 
        // 1) logical cursor position is always between cursor.previous and cursor
        // 2) after a call to next(), cursor.previous refers to the node just returned 
        // 3) after a call to previous() cursor refers to the node just returned 
        // 4) index is always the logical index of node pointed to by cursor

        private Node cursor = head.next;
        private Node pending = null;    // node pending for removal
        private int index = 0;      
  	  
    	// other instance variables ... 
    	  
      
        /**
    	 * Default constructor positions the cursor before the smallest prime factor.
    	 */
    	public PrimeFactorizationIterator()
    	{
    		// TODO 
    		cursor=head.next;
    	}

    	@Override
    	public boolean hasNext()
    	{
    		// TODO 
    		return cursor.next!=null;
    	}

    	
    	@Override
    	public boolean hasPrevious()
    	{
    		// TODO 
    		return cursor.previous!=null; 
    	}

 
    	@Override 
    	public PrimeFactor next() 
    	{
    		// TODO 
    		if(!hasNext()) {
    			throw new NoSuchElementException();
    		}
    		pending=cursor;
    		cursor=cursor.next;
    		cursor.previous=pending;
    		index++;
    		return pending.pFactor; 
    	}

 
    	@Override 
    	public PrimeFactor previous() 
    	{
    		// TODO 
    		if(!hasPrevious()) {
    			throw new NoSuchElementException();
    		}
    		pending=cursor;
    		cursor=cursor.previous;
    		index--;
    		return pending.pFactor; 
    	}

   
    	/**
    	 *  Removes the prime factor returned by next() or previous()
    	 *  
    	 *  @throws IllegalStateException if pending == null 
    	 */
    	@Override
    	public void remove() throws IllegalStateException
    	{
    		// TODO 
    		if(pending==null) {
    			throw new IllegalStateException();
    		}
    		if(pending==cursor) {
    			cursor=cursor.next;
    			unlink(pending);
    			size--;
    			updateValue();
    			pending=null;
    		}
    		unlink(pending);
    		index--;
    		size--;
    		pending=null;
    		updateValue();
    	}
 
 
    	/**
    	 * Adds a prime factor at the cursor position.  The cursor is at a wrong position 
    	 * in either of the two situations below: 
    	 * 
    	 *    a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. 
    	 *    b) pf.prime > cursor.pFactor.prime if cursor != tail. 
    	 * 
    	 * Take into account the possibility that pf.prime == cursor.pFactor.prime. 
    	 * 
    	 * Precondition: pf.prime is a prime. 
    	 * 
    	 * @param pf  
    	 * @throws IllegalArgumentException if the cursor is at a wrong position. 
    	 */
    	@Override
        public void add(PrimeFactor pf) throws IllegalArgumentException 
        {
        	// TODO 
    		if(cursor.previous!=head) {
    			if(pf.prime<cursor.previous.pFactor.prime) {
    				throw new IllegalArgumentException();
    			}
    		}
    		else if(cursor !=tail) {
    			if(pf.prime>cursor.pFactor.prime) {
    				throw new IllegalArgumentException();
    			}
    		}
    		else if(pf.prime==cursor.pFactor.prime) {
    			throw new IllegalArgumentException();
    		}
    		Node temp=new Node(pf);
    		link(cursor, temp);
    		index++;
    		size++;
    		updateValue();
        }


    	@Override
		public int nextIndex() 
		{
			return index;
		}


    	@Override
		public int previousIndex() 
		{
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) 
		{
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}
        
    	// Other methods you may want to add or override that could possibly facilitate 
    	// other operations, for instance, addition, access to the previous element, etc.
    	// 
    	// ...
    	// 
    }

    
    // --------------
    // Helper methods 
    // -------------- 
    
    /**
     * Inserts toAdd into the list after current without updating size.
     * 
     * Precondition: current != null, toAdd != null
     */
    private void link(Node current, Node toAdd)
    {
    	// TODO 
    	toAdd.next=current;
    	current.previous.next=toAdd;
    	toAdd.previous=current.previous;
    	current.previous=toAdd;
    }

	 
    /**
     * Removes toRemove from the list without updating size.
     */
    private void unlink(Node toRemove)
    {
    	// TODO 
    	toRemove.next.previous=toRemove.previous;
    	toRemove.previous.next=toRemove.next;
    }


    /**
	  * Remove all the nodes in the linked list except the two dummy nodes. 
	  * 
	  * Made public for testing purpose.  Ought to be private otherwise. 
	  */
	public void clearList()
	{
		// TODO  
		tail.previous=head;
		head.next=tail;
		value=1;
		size=0;
	}	
	
	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the represented integer.  
	 * Use Math.multiply(). If an exception is throw, assign OVERFLOW to the instance variable value.  
	 * Otherwise, assign the multiplication result to the variable. 
	 * 
	 */
	private void updateValue()
	{
		try {		
			// TODO		
			long temp=1;
			PrimeFactorizationIterator pf=this.iterator();
			while(pf.hasNext()) {
				if(pf.cursor.pFactor.multiplicity<=1) {
					temp=Math.multiplyExact(pf.cursor.pFactor.prime, temp);
				}
				else {
					for(int i=0; i<pf.cursor.pFactor.multiplicity; ++i) {
						temp=Math.multiplyExact(pf.cursor.pFactor.multiplicity, temp);
					}
				}
			}
			this.value=temp;
		} 
			
		catch (ArithmeticException e) 
		{
			value = OVERFLOW;
		}
		
	}
}
