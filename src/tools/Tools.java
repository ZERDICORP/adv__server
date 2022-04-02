package tools;



import java.awt.Point;
import constants.Direction;



public class Tools
{
	public static Point vectorByDirection(int d)
	{
		switch (Direction.values()[d])
    {   
      case UP: return new Point(0, -1);
      case DOWN: return new Point(0, 1);
      case LEFT: return new Point(-1, 0);
      case RIGHT: return new Point(1, 0);
    }
		return null;
	}

	public static boolean in2DArea(int x, int y, int w, int h)
  {   
    return (x >= 0 && x < w) && (y >= 0 && y < h); 
  }	

	public static void log(String msg)
  {   
    System.out.println(
      "[adv:log] " +
      msg.substring(0, 1).toUpperCase() + msg.substring(1) +
      ".."
    );  
  }	
}
