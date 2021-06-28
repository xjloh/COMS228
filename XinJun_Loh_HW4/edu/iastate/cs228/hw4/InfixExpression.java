package edu.iastate.cs228.hw4;

/**
 *  
 * @author Loh Xin Jun
 *
 */

import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix conversion using 
 * one stack, and evaluates the converted postfix expression.    
 *
 */

public class InfixExpression extends Expression 
{
	private String infixExpression;   	// the infix expression to convert		
	private boolean postfixReady = false;   // postfix already generated if true
	private int rankTotal = 0;		// Keeps track of the cumulative rank of the infix expression.
	
	private PureStack<Operator> operatorStack; 	  // stack of operators 
	
	
	/**
	 * Constructor stores the input infix string, and initializes the operand stack and 
	 * the hash map.
	 * 
	 * @param st  input infix string. 
	 * @param varTbl  hash map storing all variables in the infix expression and their values. 
	 */
	public InfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		// TODO
		super(st, varTbl);
		operatorStack=new ArrayBasedStack<Operator>(); //initialize the operand stack
		infixExpression=removeExtraSpaces(st);
	}
	

	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public InfixExpression (String s)
	{
		// TODO  
		operatorStack=new ArrayBasedStack<Operator>();
		infixExpression=removeExtraSpaces(s);
	}
	

	/**
	 * Outputs the infix expression according to the format in the project description.
	 */
	@Override
	public String toString()
	{
		// TODO  
		infixExpression=infixExpression.replace("( ", "(").replace(" )", ")");
		return infixExpression;
	}
	
	/** 
	 * @return equivalent postfix expression, or  
	 * 
	 *         a null string if a call to postfix() inside the body (when postfixReady 
	 * 		   == false) throws an exception.
	 */
	public String postfixString()throws Exception 
	{
		// TODO
		if(postfixReady==false) {
			throw new Exception();
		}
		return postfixExpression; 
	}


	/**
	 * Resets the infix expression. 
	 * 
	 * @param st
	 */
	public void resetInfix (String st)
	{
		infixExpression = st; 
	}


	/**
	 * Converts infix expression to an equivalent postfix string stored at postfixExpression.
	 * If postfixReady == false, the method scans the infixExpression, and does the following
	 * (for algorithm details refer to the relevant PowerPoint slides): 
	 * 
	 *     1. Skips a whitespace character.
	 *     2. Writes a scanned operand to postfixExpression. 
	 *     3. When an operator is scanned, generates an operator object.  In case the operator is 
	 *        determined to be a unary minus, store the char '~' in the generated operator object.
	 *     4. If the scanned operator has a higher input precedence than the stack precedence of 
	 *        the top operator on the operatorStack, push it onto the stack.   
	 *     5. Otherwise, first calls outputHigherOrEqual() before pushing the scanned operator 
	 *        onto the stack. No push if the scanned operator is ). 
     *     6. Keeps track of the cumulative rank of the infix expression. 
     *     
     *  During the conversion, catches errors in the infixExpression by throwing 
     *  ExpressionFormatException with one of the following messages:
     *  
     *      -- "Operator expected" if the cumulative rank goes above 1;
     *      -- "Operand expected" if the rank goes below 0; 
     *      -- "Missing '('" if scanning a ‘)’ results in popping the stack empty with no '(';
     *      -- "Missing ')'" if a '(' is left unmatched on the stack at the end of the scan; 
     *      -- "Invalid character" if a scanned char is neither a digit nor an operator; 
     *   
     *  If an error is not one of the above types, throw the exception with a message you define.
     *      
     *  Sets postfixReady to true.  
	 */
	public void postfix() throws ExpressionFormatException
	{
		 // TODO 
		infixExpression=infixExpression.replace("(", "( ").replace(")", " )");
		Scanner scan=new Scanner(infixExpression);
		boolean check=true;
		int index=0;
		String cur="";
		String before="";
		postfixExpression="";
		if(postfixReady==false) {
			while(scan.hasNext()) {
				if(check) {
					cur=scan.next();
					check=false;
				}
				else {
					before=cur;
					cur=scan.next();
				}
			if(isVariable(cur.charAt(0)) || isInt(cur)) { //operands
				postfixExpression+=cur+" ";
			}
			else if(isOperator(cur.charAt(0))) {
				Operator temp=new Operator(cur.charAt(0));
				//check for unary minus
				if(index==0 && infixExpression.charAt(0)=='-') { //first in an expression
					temp=new Operator('~');
					operatorStack.push(temp);
				}
				else if(index>0 && cur.charAt(0)=='-' && before.charAt(0)==')') { // binary minus for )-
					outputHigherOrEqual(temp);
					operatorStack.push(temp);
				}
				else if(index>0 && cur.charAt(0)=='-' && isOperator(before.charAt(0))) { //after another operator
					temp=new Operator('~');
					operatorStack.push(temp);
				}
				else if(index>0 && before.charAt(0)=='(' && cur.charAt(0)=='-') { //after a left parenthesis (-
					temp=new Operator('~');
					operatorStack.push(temp);
				}
				else if(operatorStack.isEmpty()) {
					operatorStack.push(new Operator(cur.charAt(0)));
				}
				else if(temp.getOp()=='(') {
					operatorStack.push(temp);
				}
				else if(temp.getOp()==')') {
					outputHigherOrEqual(temp);
					if(operatorStack.isEmpty() && cur.isEmpty()) { //change
						scan.close();
						throw new ExpressionFormatException("Missing '('");
					}
				}
				else if(operatorStack.peek().compareTo(temp)==-1) {
					operatorStack.push(temp);
				}
				else {
					outputHigherOrEqual(temp);
					operatorStack.push(temp);
				}
			}
			if(!isVariable(cur.charAt(0)) && !isInt(cur) && !isOperator(cur.charAt(0))) {
				scan.close();
				throw new ExpressionFormatException("Invalid character");
			}
			//for rank=1
			if(isVariable(cur.charAt(0)) || isInt(cur)) {
				rankTotal+=1;
			}
			if(isOperator(cur.charAt(0))) { 
				// for rank=-1
				if(cur.charAt(0)=='*' || cur.charAt(0)=='/' || cur.charAt(0)=='%' || cur.charAt(0)=='+' || cur.charAt(0)=='^') {
					rankTotal-=1;
				}
				//for rank=0
				else if(cur.charAt(0)=='(' || cur.charAt(0)==')') {
					rankTotal+=0;
				}
				//for binary minus
				else if(index>0 && cur.charAt(0)=='-' && before.charAt(0)==')') {
					rankTotal-=1;
				}
				else if(index>0 && cur.charAt(0)=='-' && !isOperator(before.charAt(0))) {
					rankTotal-=1;
				}
				//for unary minus
				else if(index>0 && before.charAt(0)=='(' && cur.charAt(0)=='-'){ 
					rankTotal+=0;
				}
				else if(index>0 && before.charAt(0)=='-' && cur.charAt(0)=='-') { //change 2
					rankTotal+=0;
				}
				else if(index>0 && isOperator(before.charAt(0)) && cur.charAt(0)=='-') { //change 3
					rankTotal+=0;
				}
				else if(index==0 && cur.charAt(0)=='-') {
					rankTotal+=0;
				}
			}
			if(rankTotal>1) {
				scan.close();
				throw new ExpressionFormatException("Operator expected");
			}
			else if(rankTotal<0) {
				scan.close();
				throw new ExpressionFormatException("Operand expected");
			}
			index++;
			} //end of while loop
			while(!operatorStack.isEmpty()) { //for the leftovers
				if(operatorStack.peek().getOp()=='(') {
					scan.close();
					throw new ExpressionFormatException("Missing ')'");
				}
				postfixExpression+=operatorStack.pop().getOp()+" ";
			}
		scan.close();
		postfixExpression=postfixExpression.trim();
		postfixReady=true;
		}
	}

	
	
	/**
	 * This function first calls postfix() to convert infixExpression into postfixExpression. Then 
	 * it creates a PostfixExpression object and calls its evaluate() method (which may throw  
	 * an exception).  It also passes any exception thrown by the evaluate() method of the 
	 * PostfixExpression object upward the chain. 
	 * 
	 * @return value of the infix expression 
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException  
    {
    	// TODO 
		postfixReady=false;
		rankTotal=0;
		this.postfix();
		PostfixExpression obj=new PostfixExpression(this.postfixExpression, this.varTable);
		return obj.evaluate();  
    }


	/**
	  * Pops the operator stack and output as long as the operator on the top of the stack has a 
	  * stack precedence greater than or equal to the input precedence of the current operator op.  
	  * Writes the popped operators to the string postfixExpression.  
	  * 
	  * If op is a ')', and the top of the stack is a '(', also pops '(' from the stack but does
	  * not write it to postfixExpression. 
	  * 
	  * @param op  current operator
	  * @throws ExpressionFormatException
	  *    with the following message
	  *    -- "Missing '('" if op is a ')' and matching '(' is not found on stack. 
	  */
	 private void outputHigherOrEqual(Operator op) throws ExpressionFormatException
	{
		// TODO 
		 while(!operatorStack.isEmpty()) {
			 if(operatorStack.peek().compareTo(op)==1 || operatorStack.peek().compareTo(op)==0) {
				 postfixExpression += operatorStack.pop().getOp() + " ";
			 }
			 if(operatorStack.size()==0 && op.getOp()==')') {
				 throw new ExpressionFormatException("Missing '('");
			 }
			 else if(op.getOp()==')' && operatorStack.peek().getOp()=='(') {
				 operatorStack.pop();
				 break;
			 }
			 else if(operatorStack.size()==0 || operatorStack.peek().compareTo(op)==-1) {
				 break;
			 }
		 }
	}
	
	// other helper methods if needed
}
