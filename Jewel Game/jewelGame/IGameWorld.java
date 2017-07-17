package jewelGame;

import java.awt.geom.Point2D;
public interface IGameWorld
{
	//Method that is used to determine if the game is over and if the
    //user is progressing in the game.
	public void checkGameConditions();
	//Method that sets the game score.
	public void setGamePoints(int gamePoints);
	//Method that returns the current int value of the game score.
	public int getGamePoints();
	//Method that setss the game score goal.
	public void setGamePointsGoal(int gamePoints);
	//Method that returns the current int value of the game score goal.
	public int getGamePointsGoal();
	//Method that sets the game clock.
	public void setGameClock(int time);
	//Method that returns the current int value of the game clock.
	public int getGameClock();
	//Method that sets the game level.
	public void setGameLevel(int gameLevel);
	//Method that returns the game level.
	public int getGameLevel();
	//Methods that returns a string in the format of "Minutes:Seconds" of game clock.
	public String getGameClockText();
	public void setGameSound(boolean newGameSound);
	public boolean getGameSound();
	//Method that sets the game's game mode.
	public void setGameMode(boolean newGameMode);
	//Method that returns the game's current game mode.
	public boolean getGameMode();
	//Method sets the initial layout of objects in Game World.
	public void initLayout();
	//Method that has the car to update its heading based on steering direction, decrement the fuel level of the car, have all moveable objects in the
	//collection to move and increment the game clock. 
	public void tick(int timerCycle); 
	//Method that returns the gameWorldCollection
	public GameWorldObject[][] getGameWorldCollection();
	//Method that sets an object to be selected
	public void setGameObjectSelect(Point2D p);
 }
