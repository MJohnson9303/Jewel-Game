package jewelGame;

import java.awt.geom.Point2D;

//This class is to act as a proxy for GameWorld to be passed to it's observers and prevent those observers from
//using methods not needed to obtain game state information and nothing else except allow them to select objects.
public class GameWorldProxy implements IObservable, IGameWorld
{
	private GameWorld realGameWorld;
	
    public GameWorldProxy(GameWorld gw)
    {
       realGameWorld = gw;
    }
	public void checkGameConditions() 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public void setGamePoints(int gamePoints) 
	{
		throw new RuntimeException("Observer can not alter the game");	
	}
	public int getGamePoints()
	{
		return realGameWorld.getGamePoints();
	}
	public void setGamePointsGoal(int gamePoints) 
	{
		throw new RuntimeException("Observer can not alter the game");	
	}
	public int getGamePointsGoal() 
	{
		return realGameWorld.getGamePointsGoal();
	}
	public void setGameClock(int time) 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public int getGameClock() 
	{
		return realGameWorld.getGameClock();
	}
	public void setGameLevel(int gameLevel) 
	{
		throw new RuntimeException("Observer can not alter the game");	
	}
	public int getGameLevel() 
	{
		return realGameWorld.getGameLevel();
	}
	public String getGameClockText() 
	{
		return realGameWorld.getGameClockText();
	}
	public void setGameSound(boolean newGameSound) 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public boolean getGameSound() 
	{
		return realGameWorld.getGameSound();
	}
	public void setGameMode(boolean newGameMode) 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public boolean getGameMode() 
	{
		return realGameWorld.getGameMode();
	}
	public void initLayout() 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public void tick(int timerCycle) 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public GameWorldObject[][] getGameWorldCollection() 
	{
		return realGameWorld.getGameWorldCollection();
	}
	public void setGameObjectSelect(Point2D p) 
	{
		realGameWorld.setGameObjectSelect(p);
	}
	public void addObserver(IObserver obs) 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
	public void notifyObservers() 
	{
		throw new RuntimeException("Observer can not alter the game");
	}
}