package jewelGame;

import java.awt.Color;
import java.awt.geom.Point2D;

//This class has all characteristics and methods that all objects in the game share.
public class GameWorldObject 
{
	private Point2D objectLocation;
	private Color objectColor = new Color(0,0,0);
	
	//Method that sets the color of a game object.
	public void setColor(Color newColor)
	{
		objectColor = newColor;
	}
	//Method that gets the color object of a game object.
	public Color getColor()
	{
		return objectColor;
	}
	//Method that set the location of a game object.
	public void setLocation(Point2D newObjectLocation)
	{
		objectLocation = newObjectLocation;
	}
	//Method that gets the location object of a game object.
	public Point2D getLocation()
	{
		return objectLocation;
	}
}

