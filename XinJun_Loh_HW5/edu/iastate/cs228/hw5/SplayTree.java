package edu.iastate.cs228.hw5;


import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * 
 * @author Loh Xin Jun
 *
 */


/**
 * 
 * This class implements a splay tree.  Add any helper methods or implementation details 
 * you'd like to include.
 *
 */
public class SplayTree<E extends Comparable<? super E>> extends AbstractSet<E>
{
	protected Node root; 
	protected int size; 
	private boolean dir=true; //true for left and false for right
	private boolean rotate=false; //true if its for leftRotate or rightRotate
	private boolean hasGP=false;//true if has grandparents, false otherwise
	private StringBuilder list;

	public class Node  // made public for grading purpose
	{
		public E data;
		public Node left;
		public Node parent;
		public Node right;

		public Node(E data) {
			this.data = data;
		}

		@Override
		public Node clone() {
			return new Node(data);
		}	
	}

	
	/**
	 * Default constructor constructs an empty tree. 
	 */
	public SplayTree() 
	{
		size = 0;
	}
	
	
	/**
	 * Needs to call addBST() later on to complete tree construction. 
	 */
	public SplayTree(E data) 
	{
		// TODO 
		root=new Node(data);
		size++;
		addBST(root.data);
	}

	
	/**
	 * Copies over an existing splay tree. The entire tree structure must be copied.  
	 * No splaying. Calls cloneTreeRec(). 
	 * 
	 * @param tree
	 */
	public SplayTree(SplayTree<E> tree)
	{
		// TODO 
		Iterator<E> it=tree.iterator();
		if(tree.root.right==null && tree.root.left==null) {//copying a root
			this.root=tree.root.clone();
			size=1;
		}
		else {
			this.root=tree.root.clone();//copy root
			this.size=tree.size;//copy size
			if(tree.root.left!=null) {//there is left subtree
				this.root.left=cloneTreeRec(tree.root.left);
			}
			if(tree.root.right!=null) {//there is right subtree
				this.root.right=cloneTreeRec(tree.root.right);
			}
		}
	}

	
	/**
	 * Recursive method called by the constructor above. 
	 * 
	 * @param subTree
	 * @return
	 */
	private Node cloneTreeRec(Node subTree) 
	{
		// TODO
		Node temp=subTree.clone();
		if(subTree.left==null && subTree.right==null) {//if it is a leaf
			return temp;
		}
		if(subTree.left!=null) {//for left subtree
			temp.left=cloneTreeRec(subTree.left);
		}
		if(subTree.right!=null) {//for right subtree
			temp.right=cloneTreeRec(subTree.right);
		}
		return temp; 
	}
	
	
	/**
	 * This function is here for grading purpose. It is not a good programming practice.
	 * 
	 * @return element stored at the tree root 
	 */
	public E getRoot()
	{
		// TODO 
		if(this.size==0) {
			return null;
		}
		return root.data; 
	}
	
	
	@Override 
	public int size()
	{
		// TODO
		return size; 
	}
	
	
	/**
	 * Clear the splay tree. 
	 */
	@Override
	public void clear() 
	{
		// TODO 
		root=null;
		size=0;
	}
	
	
	// ----------
	// BST method
	// ----------
	
	/**
	 * Adds an element to the tree without splaying.  The method carries out a binary search tree
	 * addition.  It is used for initializing a splay tree. 
	 * 
	 * Calls link(). 
	 * 
	 * @param data
	 * @return true  if addition takes place  
	 *         false otherwise (i.e., data is in the tree already)
	 */
	public boolean addBST(E data)
	{
		// TODO 
		if(data==null) {
			return false;
		}
		if(null==root) { //for empty root
			root=new Node(data);
			size++;
			return true;
		}
		Node cur=root;
		while(true) {
			int comp=cur.data.compareTo(data);
			if(comp==0) { //key is in tree, do not add it
				return false;
			}
			else if(comp>0) { //data precedes cur.data, go left
				if(cur.left!=null) {
					cur=cur.left;
				}
				else {
					Node temp=new Node(data);
					dir=true;//linking cur.left with temp
					rotate=false;
					link(cur, temp);//link
					size++;
					return true;
				}
			}
			else { //key succeeds cur.data, go right
				if(cur.right!=null) {
					cur=cur.right;
				}
				else {
					Node temp=new Node(data);
					dir=false;//linking cur.right with temp
					rotate=false;
					link(cur, temp);//link
					size++;
					return true;
				}
			}
		}
	}
	
	
	// ------------------
	// Splay tree methods 
	// ------------------
	
	/**
	 * Inserts an element into the splay tree. In case the element was not contained, this  
	 * creates a new node and splays the tree at the new node. If the element exists in the 
	 * tree already, it splays at the node containing the element. 
	 * 
	 * Calls link(). 
	 * 
	 * @param  data  element to be inserted
	 * @return true  if addition takes place 
	 *         false otherwise (i.e., data is in the tree already)
	 */
	@Override 
	public boolean add(E data)
	{
		// TODO 
		if(data==null) {
			return false;
		}
		if(root==null) {
			addBST(data);
			return true;
		}
		Node obj=findEntry(data);
		int comp=obj.data.compareTo(data);
		if(comp==0) { //If the element exists in the tree already
			splay(data);
			return false;
		}
		else {
			Node temp=new Node(data);
			int comp2=temp.data.compareTo(obj.data);
			if(comp2>0) {
				dir=false;//linking obj.right to temp and temp.parent to obj
				rotate=false;
				link(obj, temp);
			}
			else if(comp2<0) {
				dir=true;//linking obj.left to temp and temp.parent to obj
				rotate=false;
				link(obj, temp);
			}
			splay(temp);
			size++;
			return true;
		}
	}
	
	
	/**
	 * Determines whether the tree contains an element.  Splays at the node that stores the 
	 * element.  If the element is not found, splays at the last node on the search path.
	 * 
	 * @param  data  element to be determined whether to exist in the tree
	 * @return true  if the element is contained in the tree 
	 *         false otherwise
	 */
	public boolean contains(E data)
	{
		// TODO 
		if(data==null || root==null) {
			return false;
		}
		Node temp=findEntry(data);
		splay(temp);
		root=temp;
		int comp=temp.data.compareTo(data);
		if(comp==0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Finds the node that stores the data and splays at it.
	 *
	 * @param data
	 */
	public void splay(E data) 
	{
		contains(data);
	}

	
	/**
	 * Removes the node that stores an element.  Splays at its parent node after removal
	 * (No splay if the removed node was the root.) If the node was not found, the last node 
	 * encountered on the search path is splayed to the root.
	 * 
	 * Calls unlink(). 
	 * 
	 * @param  data  element to be removed from the tree
	 * @return true  if the object is removed 
	 *         false if it was not contained in the tree 
	 */
	public boolean remove(E data)
	{
		// TODO 
		if(data==null) {
			return false;
		}
		Node temp=findEntry(data);
		if(temp==null) {
			return false;
		}
		else if(data!=temp.data) {//If the node was not found, the last node encountered on the search path is splayed to the root.
			splay(temp);
			return false;
		}
		else {
			if(temp.data==root.data) {//if temp is the root
				if(root.left!=null && root.right!=null) {//if root has left and right subtree
					this.root=join(root.left, root.right);
					size--;
					return true;
				}
				else if(root.left!=null && root.right==null) {
					root=root.left;
					size--;
					return true;
				}
				else if(root.left==null && root.right!=null) {
					root=root.right;
					size--;
					return true;
				}
				else if(size==1) {//if its the only node
					root=null;
					size--;
					return true;
				}
			}
			//if temp is not root but is in the splaytree
			unlink(temp);
			splay(temp.parent);
			root=temp.parent;
			return true;
		}
	}


	/**
	 * This method finds an element stored in the splay tree that is equal to data as decided 
	 * by the compareTo() method of the class E.  This is useful for retrieving the value of 
	 * a pair <key, value> stored at some node knowing the key, via a call with a pair 
	 * <key, ?> where ? can be any object of E.   
	 * 
	 * Calls findEntry(). Splays at the node containing the element or the last node on the 
	 * search path. 
	 * 
	 * @param  data
	 * @return element such that element.compareTo(data) == 0
	 */
	public E findElement(E data) 
	{
		// TODO 
		if(data==null) {//if data is null
			return null;
		}
		Node temp=findEntry(data);
		if(temp!=null) {
			E obj=temp.data;
			splay(data);
			int comp=obj.compareTo(data);
			if(comp==0) {
				return obj;//return element when found
			}
			else {
				return null;
			}
		}
		return null; 
	}

	
	/**
	 * Finds the node that stores an element. It is called by methods such as contains(), add(), remove(), 
	 * and findElement(). 
	 * 
	 * No splay at the found node. 
	 *
	 * @param  data  element to be searched for 
	 * @return node  if found or the last node on the search path otherwise
	 *         null  if size == 0. 
	 */
	protected Node findEntry(E data)
	{
		// TODO 
		if(size==0) {
			return null;
		}
		Node cur=root;
		while(cur!=null) {
			int comp=cur.data.compareTo(data);
			if(comp==0) {
				return cur;
			}
			else if(comp>0) {
				if(cur.left==null) {
					return cur;
				}
				cur=cur.left;
			}
			else {
				if(cur.right==null) {
					return cur;
				}
				cur=cur.right;
			}
		}
		return cur;
	}
	
	
	/** 
	 * Join the two subtrees T1 and T2 rooted at root1 and root2 into one.  It is 
	 * called by remove(). 
	 * 
	 * Precondition: All elements in T1 are less than those in T2. 
	 * 
	 * Access the largest element in T1, and splay at the node to make it the root of T1.  
	 * Make T2 the right subtree of T1.  The method is called by remove(). 
	 * 
	 * @param root1  root of the subtree T1 
	 * @param root2  root of the subtree T2 
	 * @return the root of the joined subtree
	 */
	protected Node join(Node root1, Node root2)
	{
		// TODO
		Node newRoot=root1;
		while(newRoot.right!=null) {//access to the largest element to root1
			newRoot=newRoot.right;
		}
		splay(newRoot);//splay at the node to make it the root of T1
		newRoot.right=root2;//Make T2 the right subtree of T1
		root2.parent=newRoot;//link parent to newRoot
		return newRoot;
	}

	
	/**
	 * Splay at the current node.  This consists of a sequence of zig, zigZig, or zigZag 
	 * operations until the current node is moved to the root of the tree.
	 * 
	 * @param current  node to splay
	 */
	protected void splay(Node current)
	{
		// TODO
		while(current.parent!=null) {
			if(current.parent.parent==null) {//no grandparent
				zig(current);
			}
			else if(current.parent.parent.left==current.parent && current.parent.left==current) {//left child of left child
				zigZig(current);
			}
			else if(current.parent.parent.right==current.parent && current.parent.right==current) {//right child of right child
				zigZig(current);
			}
			else if(current.parent.parent.left==current.parent && current.parent.right==current) {//right child of left child
				zigZag(current);
			}
			else if(current.parent.parent.right==current.parent && current.parent.left==current) {//left child of right child
				zigZag(current);
			}
		}
	}
	

	/**
	 * This method performs the zig operation on a node. Calls leftRotate() 
	 * or rightRotate().
	 * 
	 * @param current  node to perform the zig operation on
	 */
	protected void zig(Node current)
    {
		// TODO
		//when parent of current is root
		if(current.parent.left==current) {//for left child
			rightRotate(current);
		}
		else if(current.parent.right==current) {//for right child
			leftRotate(current);
		}
	}

	
	/**
	 * This method performs the zig-zig operation on a node. Calls leftRotate() 
	 * or rightRotate().
	 * 
	 * @param current  node to perform the zig-zig operation on
	 */
	protected void zigZig(Node current)
	{
		// TODO
		//when current has grandparent but is not the root
		//current and current.parent are both left or both right child
		if(current.parent.parent.left==current.parent && current.parent.left==current) {//left child of left child
			rightRotate(current.parent);
			rightRotate(current);
		}
		else if(current.parent.parent.right==current.parent && current.parent.right==current) {//right child of right child
			leftRotate(current.parent);
			leftRotate(current);
		}
	}

	
    /**
	 * This method performs the zig-zag operation on a node. Calls leftRotate() 
	 * and rightRotate().
	 * 
	 * @param current  node to perform the zig-zag operation on
	 */
	protected void zigZag(Node current)
	{
		// TODO
		//when current has grandparent but is not the root
		//current is left child and current.parent is right child or vice versa
		//performs zig-zag
		zig(current);
		zig(current);
	}	
	
	
	/**
	 * Carries out a left rotation at a node such that after the rotation 
	 * its former parent becomes its left child. 
	 * 
	 * Calls link(). 
	 * 
	 * @param current
	 */
	private void leftRotate(Node current)
	{
		dir=true;
		rotate=true;
		if(current.parent!=null && current.parent.parent==null) {//no grandparent
			hasGP=false;
			link(current, current);
		}
		else if(current.parent!=null && current.parent.parent!=null) {//has grandparent
			hasGP=true;
			link(current, current);
		}
	}

	
	/**
	 * Carries out a right rotation at a node such that after the rotation 
	 * its former parent becomes its right child. 
	 * 
	 * Calls link(). 
	 * 
	 * @param current
	 */
	private void rightRotate(Node current)
	{
		dir=false;
		rotate=true;
		if(current.parent!=null && current.parent.parent==null) {//no grandparent
			hasGP=false;
			link(current, current);
		}
		else if(current.parent!=null && current.parent.parent!=null) {//has grandparent
			hasGP=true;
			link(current, current);
		}
	}	
	
	
	/**
	 * Establish the parent-child relationship between two nodes. 
	 * 
	 * Called by addBST(), add(), leftRotate(), and rightRotate(). 
	 * 
	 * @param parent
	 * @param child
	 */
	private void link(Node parent, Node child) 
	{
		// TODO 
		if(dir==true && rotate==false) { //true for left and for normal link
			parent.left=child;
			child.parent=parent;
		}
		else if(dir==false && rotate==false) { //false for right and for normal link
			parent.right=child;
			child.parent=parent;
		}
		//link for left rotate
		else if(dir==true && rotate==true) {
			if(hasGP) {//has grandparents
				if(child.parent.parent.left==child.parent && child==child.parent.right) {//current is right child of left child
					if(child.left!=null) {//has subtree
						Node subT=child.left;
						child.left=child.parent;//parent becomes left child
						child.parent=child.parent.parent;//updates parent 
						child.left.parent=child;//update parent link
						child.left.right=subT;//initialize subtree 
						subT.parent=child.left;//updates subtree parent link
						child.parent.left=child;//update child link
					}
					else {//no subtree
						child.left=child.parent;//parent becomes left child
						child.parent=child.parent.parent;//update parent 
						child.left.parent=child;//update parent link
						child.parent.left=child;//update child link
						child.left.right=null;
					}
				}
				else if(child.parent.parent.right==child.parent && child==child.parent.right) {//current is right child of right child
					if(child.left!=null) {//has subtree
						Node subT=child.left;
						child.left=child.parent;//parent becomes left child
						child.parent=child.parent.parent;//update parent
						child.left.parent=child;//update parent link
						child.left.right=subT;//initialize subtree
						subT.parent=child.left;//update subtree parent link
						child.parent.right=child;//update child link
					}
					else {
						child.left=child.parent;//parent becomes left child
						child.parent=child.parent.parent;//update parent
						child.left.parent=child;//update parent link
						child.parent.right=child;//update child link
						child.left.right=null;
					}
				}
			}
			else {//no grandparents
				if(child.left!=null) {//has subtree
					Node subT=child.left;
					child.left=child.parent;//parent become left child
					child.left.parent=child;//update parent 
					child.left.right= subT;//initialize subtree
					subT.parent=child.left;//update subtree link
					root=child;//update root
					child.parent=null;
				}
				else {//no subtree
					child.left=child.parent;//parent become left child
					child.left.parent=child;//update parent
					child.parent=null;
					root=child;//update child
					child.left.right=null;
				}
			}
		}
		//link for right rotation
		else if(dir==false && rotate==true) {
			if(hasGP) {//has grandparents
				if(child.parent.parent.right==child.parent && child.parent.left==child) {//current is left child of right child
					if(child.right!=null) {//has subtree
						Node subT=child.right;
						child.right=child.parent;//parent become right child
						child.parent=child.parent.parent;//update parent
						child.right.left=subT;//initialize subtree
						subT.parent=child.right;//update subtree parent link
						//parent-child link
						child.right.parent=child;
						child.parent.right=child;
					}
					else {//no subtree
						child.right=child.parent;//parent become right child
						child.parent=child.parent.parent;//update parent
						
						//parent-child link
						child.right.parent=child;
						child.parent.right=child;
						child.right.left=null;
					}
				}
				else if(child.parent.parent.left==child.parent && child.parent.left==child) {//current is left child of left child
					if(child.right!=null) {//has subtree
						Node subT=child.right;
						child.right=child.parent;//parent become right child
						child.parent=child.parent.parent;//update parent
						child.right.left=subT;//initialize subtree
						subT.parent=child.right;//update subtree parent link
						//parent-child link
						child.right.parent=child;
						child.parent.left=child;
					}
					else {
						child.right=child.parent;//parent become right child
						child.parent=child.parent.parent;//update parent
						//parent-child link
						child.right.parent=child;
						child.parent.left=child;
						child.right.left=null;
					}
				}
			}
			else {//no grandparents
				if(child.right!=null) {//has subtree
					Node subT=child.right;
					child.right=child.parent;//parent become right child
					child.right.left=subT;//initialize subtree
					subT.parent=child.right;//update subtree parent link
					child.right.parent=child;//update parent link
					root=child;//update root
					child.parent=null;
				}
				else {
					child.right = child.parent;//parent becomes right child
					child.right.parent = child;//update parent link
					child.parent = null;
					root=child;//update root
					child.right.left = null; 
				}
			}
		}
	}
	
	
	/** 
	 * Removes a node n by replacing the subtree rooted at n with the join of the node's
	 * two subtrees.
	 * 
	 * Called by remove().   
	 * 
	 * @param n
	 */
	private void unlink(Node n) 
	{
		// TODO 
		boolean check=true;//
		//deleting a leaf
		if(n.left==null && n.right==null) {
			check=false;
			if(n.parent.left==n) {//left child
				n.parent.left=null;
				size--;

			}
			else if(n.parent.right==n) {//right child
				n.parent.right=null;
				size--;
			}
		}
		else {
			if(n.left!=null && n.right==null) {
				Node temp=successor(n.left);
				n.data=temp.data;
				n=temp;
			}
			else if(n.left!=null && n.right!=null) {
				Node temp=successor(n);
				n.data=temp.data;
				n=temp; //causes temp to be deleted in code below
			}
		}
		if(check) {
			Node replacement=null;
			if(n.left!=null) {
				replacement=n.left;
			}
			else if(n.right!=null) {
				replacement=n.right;
			}
			
			//link replacement into tree in place of node n (replacement may be null)
			if(n.parent==null) {
				root=replacement;
			}
			else {
				if(n.parent.left==n) {
					n.parent.left=replacement;
				}
				else {
					n.parent.right=replacement;
				}
			}
			if(replacement!=null) {
				replacement.parent=n.parent;
			}
		size--;
		}
	}
	
	
	/**
	 * Perform BST removal of a node. 
	 * 
	 * Called by the iterator method remove(). 
	 * @param n
	 */
	private void unlinkBST(Node n)
	{
		// TODO 
		// first deal with the two-child case; copy
	    // data from successor up to n, and then delete successor 
	    // node instead of given node n
	    if (n.left != null && n.right != null){
	      Node s = successor(n);
	      n.data = s.data;
	      n = s; // causes s to be deleted in code below
	    }
	    
	    // n has at most one child, not necessarily the right one.
	    Node replacement = null;    
	    if (n.left != null)   {
	      replacement = n.left;
	    }
	    else if (n.right != null){
	      replacement = n.right;
	    }
	    
	    // link replacement into tree in place of node n 
	    // (replacement may be null)
	    if (n.parent == null){
	      root = replacement;
	    }
	    else{
	      if (n == n.parent.left)
	      {
	        n.parent.left = replacement;
	      }
	      else
	      {
	        n.parent.right = replacement;
	      }
	    }
	    
	    if (replacement != null){
	      replacement.parent = n.parent;
	    }
	    
	    --size;
	}
	
	
	/**
	 * Called by unlink() and the iterator method next(). 
	 * 
	 * @param n
	 * @return successor of n 
	 */
	private Node successor(Node n) 
	{
		if (n == null){
	      return null;
	    }
	    else if (n.right != null){
	      // leftmost entry in right subtree
	      Node current = n.right;
	      while (current.left != null){
	        current = current.left;
	      }
	      return current;
	    }
	    else {
	      // we need to go up the tree to the closest ancestor that is
	      // a left child; its parent must be the successor
	      Node current = n.parent;
	      Node child = n;
	      while (current != null && current.right == child){
	        child = current;
	        current = current.parent;
	      }
	      // either current is null, or child is left child of current
	      return current;
	    }
	}

	
	@Override
	public Iterator<E> iterator()
	{
	    return new SplayTreeIterator();
	}

	
	/**
	 * Write the splay tree according to the format specified in Section 2.2 of the project 
	 * description.
	 * 	
	 * Calls toStringRec(). 
	 *
	 */
	@Override 
	public String toString()
	{
		// TODO 
		list=new StringBuilder();
		String splay=toStringRec(root, 0);
		return splay.trim(); 
	}

	
	private String toStringRec(Node n, int depth)
	{
		// TODO 
		for(int i=0; i<depth*4; ++i) {
			list.append(" ");
		}
		if(n==null) {//for null node
			list.append("null\n");
			return list.toString();
		}
		else if(n.left==null && n.right==null) {//if node is a leaf, no recursion
			list.append(n.data.toString());
			list.append("\n");
			return list.toString();
		}
		else {//left or right child with no child
			list.append(n.data.toString());
			list.append("\n");
		}
		if(n.left!=null || n.right!=null) {//recursively print all nodes in left or right subtree (if any)
			toStringRec(n.left, depth+1);
			toStringRec(n.right, depth+1);
		}
		return list.toString(); 
	}
	
	
	/**
	   *
	   * Iterator implementation for this splay tree.  The elements are returned in 
	   * ascending order according to their natural ordering.  The methods hasNext()
	   * and next() are exactly the same as those for a binary search tree --- no 
	   * splaying at any node as the cursor moves.  The method remove() behaves like 
	   * the class method remove(E data) --- after the node storing data is found.  
	   *  
	   */
	private class SplayTreeIterator implements Iterator<E>
	{
		Node cursor;
		Node pending; 

	    public SplayTreeIterator()
	    {
	    	// TODO
	    	cursor=root;
	    	if(cursor!=null) {
	    		while(cursor.left!=null) {
	    			cursor=cursor.left;//start from the smallest because of ascending ordering
	    		}
	    	}
	    }
	    
	    @Override
	    public boolean hasNext()
	    {
	    	// TODO
	    	return cursor!=null; 
	    }

	    @Override
	    public E next()
	    {
	    	// TODO
	    	if(!hasNext()) {
	    		throw new NoSuchElementException();
	    	}
	    	pending=cursor;
	    	cursor=successor(cursor);
	    	return pending.data; 
	    }

	    /**
	     * This method will join the left and right subtrees of the node being removed, 
	     * and then splay at its parent node.  It behaves like the class method 
	     * remove(E data) after the node storing data is found.  Place the cursor at the 
	     * parent (or the new root if removed node was the root).
	     * 
	     * Calls unlinkBST(). 
	     * 
	     */
	    @Override
	    public void remove()
	    {
	      // TODO
	    	if(pending==null) {
	    		throw new IllegalStateException();
	    	}
	    	if(pending.right!=null && pending.left!=null) {
	    		cursor=pending;
	    	}
	    	unlinkBST(pending);//unlink pending
	    	pending=null;//reset pending 
	    }
	}
}
