package jewelGame;

import java.awt.Graphics;

//This interface defines the methods provided by an object which is "Deletable" on the screen.
public interface IDeletable 
{
	//This method to set deletion status of an object.
	public void setDeleteStatus(boolean status);
	//This method is to get the deletion status of an object.
	public boolean getDeleteStatus();
	//This method provides a way to draw a deletion animation for an object.
	public void deleteDraw(Graphics g);
	//This method is to return boolean status of whether or not an object finished its "deleteDraw"
	public boolean finishedDeletionAnimation();
}
