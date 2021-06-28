package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
/**
 * 
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * This class evaluates input infix and postfix expressions. 
 *
 */
public class InfixPostfix 
{

	/**
	 * Repeatedly evaluates input infix and postfix expressions.  See the project description
	 * for the input description. It constructs a HashMap object for each expression and passes it 
	 * to the created InfixExpression or PostfixExpression object. 
	 *  
	 * @param args
	 **/
	public static void main(String[] args) 
	{
		// TODO  
		System.out.println("Evaluation of Infix and Postfix Expressions");
		System.out.println("keys: 1 (standard input)  2 (file input)  3 (exit)");
		System.out.println("(Enter “I” before an infix expression, “P” before a postfix expression”)");
		Scanner in=new Scanner(System.in);
		int count=1;
		System.out.print("Trial" + " "+ count+":");
		int decision=in.nextInt();
		while(decision==1 || decision==2 || decision==3) {
			//exit
			if(decision==3) {
				in.close();
				break;
			}
			//standard input
			else if(decision==1) {
				System.out.print("Expression:");
				String theIorP="" + in.next();
				
				if(theIorP.charAt(0)=='I') { //for infix
					String infix=in.nextLine();
					HashMap<Character, Integer> hMap=new HashMap<Character, Integer>();
					if(hasVariables(infix)) { // If the expression contains some variables, then prompts the user to enter their values one by one	
						Expression temp=new InfixExpression(infix, hMap);
						System.out.println("Infix form: " + temp.toString());
						try {
							((InfixExpression)temp).postfix();
							System.out.println("Postfix form: " + temp.postfixExpression);
							System.out.println("where");
							for(int i=0; i<infix.length(); ++i) {
								if(!temp.varTable.containsKey((infix.charAt(i))) && Character.isLowerCase(infix.charAt(i))) {
									System.out.print(infix.charAt(i) + "=");
									temp.varTable.put(infix.charAt(i), in.nextInt());
								}
							}
							System.out.println("Expression Value: " + temp.evaluate());
						}
						catch(Exception x) {
							System.out.println(x.getMessage());
						}
					}
					//if expression contains no variables
					else { 
						InfixExpression temp=new InfixExpression(infix);
						System.out.println("Infix form: " + temp.toString());
						try {
							temp.postfix();
							System.out.println("Postfix  form: " + temp.postfixString());
							System.out.println("Expression value: " + temp.evaluate());
						}
						catch(Exception x) {
							System.out.println(x.getMessage());
						}
					}
				}
				else if(theIorP.charAt(0)=='P') { //for postfix
					String post=in.nextLine();
					HashMap<Character, Integer> hMap=new HashMap<Character, Integer>();
					PostfixExpression temp;
					if(hasVariables(post)) { //contains variables
						temp=new PostfixExpression(post, hMap);
						System.out.println("Postfix form: " + temp.postfixExpression);
						System.out.println("where");
						for(int i=0; i<post.length(); i++) {
							if(Character.isLowerCase(post.charAt(i))){
								System.out.print(post.charAt(i) + "=");
								int val=in.nextInt();
								temp.varTable.put(post.charAt(i), val);
							}
						}
						try {
							System.out.println("Expression value: " + temp.evaluate());
						}
						catch(Exception x) {
							System.out.println(x.getMessage());
						}
					}
					else { //does not contain variables
						temp=new PostfixExpression(post);
						System.out.println("Postfix form: " + temp.toString());
						try {
							System.out.println("Expression value: " + temp.evaluate());
						}
						catch(Exception x){
							System.out.println(x.getMessage());
						}
					}
				}
			}
			
			//file input
			else if(decision==2) {
				System.out.println("Input from a file");
				System.out.print("Enter file name: " );
				String name=in.next();
				File file=new File(name);
				Scanner scanFile=null;
				try {
					scanFile=new Scanner(file);
					char iorP=scanFile.next().charAt(0);
					while(scanFile.hasNext()) {
						System.out.println();
						//infix expression
						if(iorP=='I') {
							String obj=scanFile.nextLine();
							//if expression has variables
							if(hasVariables(obj)) {
								HashMap<Character, Integer> hMap=new HashMap<Character, Integer>();
								Expression temp=new InfixExpression(obj, hMap);
								System.out.println("Infix form: " + temp.toString());
								try {
									((InfixExpression)temp).postfix();
									System.out.println("Postfix form: " + temp.postfixExpression);
									System.out.println("where");
									int total=countVar(obj);
									for(int i=0; i<total; ++i) {
										char var=scanFile.next().charAt(0);
										scanFile.next();
										int value=scanFile.nextInt();
										hMap.put(var, value);
										System.out.println(var + " = " + hMap.get(var));
										
										if(scanFile.hasNextLine()) {
											scanFile.nextLine();
										}
									}
									System.out.println("Expression value: " + temp.evaluate());
								}
								catch (Exception e) {
									System.out.println(e.getMessage());
								}
							}
							//if expression has no variables
							else {
								Expression temp=new InfixExpression(obj);
								System.out.println("Infix form: " + temp.toString());
								try {
									((InfixExpression)temp).postfix();
									System.out.println("Postfix form: " + temp.postfixExpression);
									System.out.println("Expression value: " + temp.evaluate());
									
								}
								catch(Exception e){
									System.out.println(e.getMessage());
								}
							}
						}//end of infix
						else if(iorP=='P') {
							String post=scanFile.nextLine();
							if(hasVariables(post)) {
								HashMap<Character, Integer> hMap=new HashMap<Character, Integer>();
								PostfixExpression obj=new PostfixExpression(post, hMap);
								System.out.println("Postfix form: " + obj.toString());
								System.out.println("where");
								int total=countVar(post);
								for(int i=0; i<total; ++i) {
									char var=scanFile.next().charAt(0);
									scanFile.next();
									int value=scanFile.nextInt();
									hMap.put(var, value);
									System.out.println(var + " = " + hMap.get(var));
									
									if(scanFile.hasNextLine()) {
										scanFile.nextLine();
									}
								}
								try {
									System.out.println("Expression value:" + obj.evaluate());
								}
								catch(Exception e) {
									System.out.println(e.getMessage());
								}
							}
							//does not contains variables
							else {
								PostfixExpression obj=new PostfixExpression(post);
								System.out.println("Postfix form: " + obj.toString());
								try {
									System.out.println("Expression value: " + obj.evaluate());
								}
								catch(Exception e) {
									System.out.println(e.getMessage());
								}
							}
						}//end of postfix
						if(scanFile.hasNextLine()) {
							char check=scanFile.next().charAt(0);
							while(check!='I' && check!='P') {
								scanFile.nextLine();
							}
							iorP=check;
						}
					}
				}
				catch (FileNotFoundException e) {
					System.out.println("Invalid file");
				}
			}//end of file input
			count++;
			System.out.println();
			System.out.print("Trial" + " "+ count+":");
			decision=in.nextInt();
		}
		in.close();
	}
	
	// helper methods if needed
	
	//count the numbers of variables in an expression from file input
	private static int countVar(String st) {
		int count=0;
		char temp=' ';
		for(int i=0; i<st.length(); ++i) {
			if(st.charAt(i)!=temp && Character.isLowerCase(st.charAt(i))) {
				count+=1;
				temp=st.charAt(i);
			}
		}
		return count;
	}
	//if expression contains variables 
	private static boolean hasVariables(String s) {
		for(int i=0; i<s.length(); i++) {
			if(Character.isLowerCase(s.charAt(i))){
				return true;
			}
		}
		return false;
	}
}
