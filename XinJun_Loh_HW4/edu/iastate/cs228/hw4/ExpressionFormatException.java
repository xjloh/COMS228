package edu.iastate.cs228.hw4;

/**
 *  
 * @author Loh Xin Jun
 *
 */

/**
 * Expression thrown in PostfixExample for an invalid expression.
 */
public class ExpressionFormatException extends Exception
{
  public ExpressionFormatException()
  {
    super();
  }
  
  public ExpressionFormatException(String msg)
  {
    super(msg);
  }
}

