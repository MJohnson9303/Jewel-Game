package jewelGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LightOrb extends GameWorldObject implements IDrawable, ISelectable, IMovable, IDeletable
{
	private File lightOrbFile;
	//This variable will be used for selection conditions.
	private boolean selected;
	//Radius of the orb.
	private double radius;
	//This variable will be used for deletion animation conditions.
	private boolean deleteStatus;
	//Used as rate of deletion for "deleteDraw".
	int deletionImageSize = 120;
	//This variable will be used to provide an answer on whether or not "deleteDraw" finished.
	private boolean finishedDeleteStatus;
	//Used as rate of size increase for initial "draw" to make newly drawn orb appear to grow
	//into existence.
	int creationImageSize = 0;
    public LightOrb(Point2D objectLocation)
    {
    	lightOrbFile = new File("." + File.separator + "Images" + File.separator + "Light Orb.png");
    	//Setting the radius to 60. Not quite accurate, but close enough for now.
    	radius = 110;
    	super.setLocation(objectLocation);
    }
    //Drawing from the center of the orb. The picture is 120x120.
  	public void draw(Graphics g)
  	{
  		
		try 
		{
			BufferedImage bufferedImage = ImageIO.read(lightOrbFile);
			if(creationImageSize >= 120)
			{
				g.drawImage(bufferedImage, (int)super.getLocation().getX() - 60, (int)super.getLocation().getY() - 60, null);
			}
			else
			{
				g.drawImage(bufferedImage, (int)super.getLocation().getX() - (creationImageSize/2), (int)super.getLocation().getY() - (creationImageSize/2), creationImageSize, creationImageSize, null);
				creationImageSize = creationImageSize + 30; 
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
  	}
  	//This method sets the select status of the orb.
	public void setSelectedStatus(boolean yesNo) 
	{
		selected = yesNo;
	}
	//This method returns the select status of the orb.
	public boolean getSelectedStatus() 
	{
		return selected;
	}
	//This method determines if the orb was selected and returns the boolean result.
	public boolean contains(Point2D p) 
	{
		double px = p.getX();
		double py = p.getY();
		double xLoc = super.getLocation().getX();
		double yLoc = super.getLocation().getY();
		if((px + radius/2.0 >= xLoc) && (px <= xLoc + radius/2.0) && (py + radius/2.0 >= yLoc) && (py <= yLoc + radius/2.0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//This method draws the selected or unselected orb.
	public void selectDraw(Graphics g) 
	{
		try 
		{
			BufferedImage bufferedImage = ImageIO.read(lightOrbFile);
			g.drawImage(bufferedImage, (int)super.getLocation().getX() - 60, (int)super.getLocation().getY() - 60, null);
			//Drawing a green square around the orb if it is selected.
			g.setColor(Color.GREEN);
			g.drawRect((int)super.getLocation().getX() - 55, (int)super.getLocation().getY() - 55, 110, 110);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void move(Point2D objectLocation) 
	{
		super.setLocation(objectLocation);
	}
	//A String method that returns a string detailing information of the orb.
	public String toString()
	{
		return "Light Orb: loc="+getLocation().getX()+","+getLocation().getY();
	}
	//This method will set delete status.
	public void setDeleteStatus(boolean status) 
	{
		this.deleteStatus = status;
		
	}
	//This method will return the boolean value of delete status.
	public boolean getDeleteStatus() 
	{
		return deleteStatus;
	}
	//This method will shrink the orb image to make it appear as though it is disappearing.
	public void deleteDraw(Graphics g) 
	{
		try 
		{
			BufferedImage bufferedImage = ImageIO.read(lightOrbFile);
			if(deletionImageSize >= 0)
			{
				g.drawImage(bufferedImage, (int)super.getLocation().getX() - (deletionImageSize/2), (int)super.getLocation().getY() - (deletionImageSize/2), deletionImageSize, deletionImageSize, null);
				deletionImageSize = deletionImageSize - 30;
			}
			else
			{
				finishedDeleteStatus = true;
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	//This method will return the boolean value of whether or not "deleteDraw" finished.
	public boolean finishedDeletionAnimation() 
	{
		return finishedDeleteStatus;
	}
}