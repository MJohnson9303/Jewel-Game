package jewelGame;

import java.awt.Graphics;
import java.awt.geom.Point2D;

//This interface defines the methods provided by an object which is "Selectable" on the screen.
public interface ISelectable 
{
	//This method provides a way to mark an object a "selected" or not.
	public void setSelectedStatus(boolean yesNo);
	//This method provides a way to test whether an object is selected.
	public boolean getSelectedStatus();
	//This method provides a way to determine if a mouse point is "in" an object.
	public boolean contains(Point2D p);
	//This method provides a way to draw an unselected and selected object.
	public void selectDraw(Graphics g);
}
