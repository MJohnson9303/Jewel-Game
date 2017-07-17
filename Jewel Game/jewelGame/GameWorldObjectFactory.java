package jewelGame;

import java.awt.Color;
import java.awt.geom.Point2D;

//Class that implements the Factory Method design pattern, 
//so it handles the creations of all the objects I need.
public class GameWorldObjectFactory
{
	
	public SquarePanel makeSquarePanel(Color color, Point2D objectLocation)
	{
		return new SquarePanel(color, objectLocation);
	}
	public FireOrb makeFireOrb (Point2D objectLocation)
	{
		return new FireOrb (objectLocation);
	}
	public WaterOrb makeWaterOrb (Point2D objectLocation)
	{
		return new WaterOrb (objectLocation);
	}
	public LeafOrb makeLeafOrb (Point2D objectLocation)
	{
		return new LeafOrb (objectLocation);
	}
	public DarknessOrb makeDarknessOrb (Point2D objectLocation)
	{
		return new DarknessOrb (objectLocation);
	}
	public LightOrb makeLightOrb (Point2D objectLocation)
	{
		return new LightOrb (objectLocation);
	}
	public StarOrb makeStarOrb (Point2D objectLocation)
	{
		return new StarOrb (objectLocation);
	}
}