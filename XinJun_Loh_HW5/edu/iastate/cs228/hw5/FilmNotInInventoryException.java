package edu.iastate.cs228.hw5;

/**
 * 
 * @author Loh Xin Jun
 * 
 */

/**
 * Expression thrown for a video not in the inventory.
 */
public class FilmNotInInventoryException extends Exception
{
  public FilmNotInInventoryException()
  {
    super();
  }
  
  public FilmNotInInventoryException(String msg)
  {
    super(msg);
  }
}

