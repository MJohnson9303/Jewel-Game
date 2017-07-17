package jewelGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class SquarePanel extends GameWorldObject implements IDrawable
{
    public SquarePanel(Color givenColor, Point2D objectLocation)
    {	
		super.setColor(givenColor);
		super.setLocation(objectLocation);
    }
    //Method that draws a square panel from its center.
  	public void draw(Graphics g)
  	{
		g.setColor(super.getColor());
		g.fillRect((int)super.getLocation().getX() - 60, (int)super.getLocation().getY() - 60, 120, 120);
  	}
    //A String method that returns a string detailing information of the square panel.
  	public String toString()
  	{
  		return "Square Panel: loc="+getLocation().getX()+","+getLocation().getY();
  	}
}
