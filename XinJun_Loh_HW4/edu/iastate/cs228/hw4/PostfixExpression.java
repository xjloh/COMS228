package edu.iastate.cs228.hw4;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner; 

public class PostfixExpression extends Expression 
{
	private int leftOperand;            // left operand for the current evaluation step             
	private int rightOperand;           // right operand (or the only operand in the case of 
	                                    // a unary minus) for the current evaluation step	

	private PureStack<Integer> operandStack;  // stack of operands
	

	/**
	 * Constructor stores the input postfix string and initializes the operand stack.
	 * 
	 * @param st      input postfix string. 
	 * @param varTbl  hash map that stores variables from the postfix string and their values.
	 */
	public PostfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		// TODO
		super(st, varTbl);
		operandStack=new ArrayBasedStack<Integer>();//initialize operand stack
		postfixExpression=removeExtraSpaces(st);
		
	}
	
	
	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public PostfixExpression (String s)
	{
		// TODO
		varTable=new HashMap<Character, Integer>();//default hash map
		postfixExpression=removeExtraSpaces(s);
		operandStack=new ArrayBasedStack<Integer>();
	}

	
	/**
	 * Outputs the postfix expression according to the format in the project description.
	 */
	@Override 
	public String toString()
	{
		// TODO 
		return postfixExpression; 
	}
	

	/**
	 * Resets the postfix expression. 
	 * @param st
	 */
	public void resetPostfix (String st)
	{
		postfixExpression = st; 
	}


	/**
     * Scan the postfixExpression and carry out the following:  
     * 
     *    1. Whenever an integer is encountered, push it onto operandStack.
     *    2. Whenever a binary (unary) operator is encountered, invoke it on the two (one) elements popped from  
     *       operandStack,  and push the result back onto the stack.  
     *    3. On encountering a character that is not a digit, an operator, or a blank space, stop 
     *       the evaluation. 
     *       
     * @return value of the postfix expression 
     * @throws ExpressionFormatException with one of the messages below: 
     *  
     *           -- "Invalid character" if encountering a character that is not a digit, an operator
     *              or a whitespace (blank, tab); 
     *           --	"Too many operands" if operandStack is non-empty at the end of evaluation; 
     *           -- "Too many operators" if getOperands() throws NoSuchElementException; 
     *           -- "Divide by zero" if division or modulo is the current operation and rightOperand == 0;
     *           -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0;
     *           -- self-defined message if the error is not one of the above.
     *           
     *         UnassignedVariableException if the operand as a variable does not have a value stored
     *            in the hash map.  In this case, the exception is thrown with the message
     *            
     *           -- "Variable <name> was not assigned a value", where <name> is the name of the variable.  
     *           
     */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException 
    {
    	// TODO 
		Scanner scan= new Scanner(postfixExpression);
		while(scan.hasNext()) {
			String obj=scan.next();
			if(isInt(obj)) { //check for integer
				int temp=Integer.parseInt(obj);
				operandStack.push(temp);
			}
			else if(isOperator(obj.charAt(0))) { //check for operators
				try {
					getOperands(obj.charAt(0));
				}
				catch(NoSuchElementException x){
					scan.close();
					throw new ExpressionFormatException("Too many operators");
				}
				if(obj.charAt(0)=='%' || obj.charAt(0)=='/' && rightOperand==0) {
					scan.close();
					throw new ExpressionFormatException("Divide by zero");
				}
				else if(obj.charAt(0)=='^' && leftOperand==0 && rightOperand==0) {
					scan.close();
					throw new ExpressionFormatException("0^0");
				}
				operandStack.push(compute(obj.charAt(0)));
			}
			else if(isVariable(obj.charAt(0))) { //check for variables
				if(varTable.containsKey(obj.charAt(0))) {
					operandStack.push(varTable.get(obj.charAt(0)));
				}
				else {
					scan.close();
					throw new UnassignedVariableException("Variable" + obj.charAt(0) + "was not assigned a value");
				}
			}
			else if(!isVariable(obj.charAt(0)) && !isInt(obj) && !isOperator(obj.charAt(0))) {
				scan.close();
				throw new ExpressionFormatException("Invalid character");
			}
		}
		scan.close();
		int temp=operandStack.pop();
		if(!operandStack.isEmpty()) { //if operandStack is non-empty at the end of evaluation
			throw new ExpressionFormatException("Too many operands");
		}
		return temp;  
    }
	

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it to rightOperand. The stack must have at least
	 * one entry. Otherwise, throws NoSuchElementException.
	 * For binary operator, pops the right and left operands from operandStack, and assign them to rightOperand and leftOperand, respectively. The stack must have at least
	 * two entries. Otherwise, throws NoSuchElementException.
	 * @param op
	 * 			char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException 
	{
		// TODO 
		if(op=='~') {
			if(operandStack.size()<1) {
				throw new NoSuchElementException();
			}
			rightOperand=operandStack.pop();
		}
		else if(isOperator(op) && op!='~') {
			if(operandStack.size()<2) {
				throw new NoSuchElementException();
			}
			rightOperand=operandStack.pop();
			leftOperand=operandStack.pop();
		}
	}


	/**
	  * Computes "leftOperand op rightOperand" or "op rightOperand" if a unary operator.
	  * 
	  * @param op operator that acts on leftOperand and rightOperand.
	  * @return
	  *     returns the value obtained by computation.
	  * @throws ExpressionFormatException
	  *             with one of the messages below: <br>
	  *             -- "Divide by zero" if division is the current operation and rightOperand == 0; <br>
	  *             -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0.
	  */
	private int compute(char op) throws ExpressionFormatException  
	{
		// TODO 
		if(op=='/' && rightOperand==0) {
			throw new ExpressionFormatException("Divide by zero");
		}
		else if(op=='^' && leftOperand==0 && rightOperand==0) {
			throw new ExpressionFormatException("0^0");
		}
		if(op=='~') {
			return -rightOperand;
		}
		else if(op=='+') {
			return leftOperand+rightOperand;
		}
		else if(op=='-') {
			return leftOperand-rightOperand;
		}
		else if(op=='/') {
			return leftOperand/rightOperand;
		}
		else if(op=='*') {
			return leftOperand*rightOperand;
		}
		else if(op=='%') {
			return leftOperand%rightOperand;
		}
		else {
			return (int)Math.pow(leftOperand, rightOperand);
		}  
		// TO MODIFY 
	}
}
