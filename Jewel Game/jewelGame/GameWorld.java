package jewelGame;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class GameWorld implements IGameWorld
{
	//Game state values 
	private int gamePoints = 0;
	//gameClock is in seconds.
	private int gameClock = 180;
	private int gamePointsGoal = 1000;
	private int gameLevel = 1;
	private boolean gameSound = true;
    //true for "play" and false for "pause"
    private boolean gameMode = true;
	//Creating GameWorldCollection object for collection and indirect accessing of game objects
    //private GameWorldCollection gameWorldCollection = new GameWorldCollection();
    //GameWorldObjectFactoryObject that will be used to create game world objects as needed
    private GameWorldObjectFactory gwof = new GameWorldObjectFactory();
    //Creating Observer collection object
    private Vector<IObserver> observerCollection = new Vector<IObserver>();
    //An object to hold an object from the collection as needed.
    private GameWorldObject holdObject;
    //GameWorldProxy reference for the observers can use to get information from GameWorld, but
    //can't actually alter it except by allowing the selection of Orbs.
    private GameWorldProxy proxy;
	//These hold the selected items in the collection for moving them when a 
	//second item is selected.
	private GameWorldObject holdMovableObject1;
	private GameWorldObject holdMovableObject2;
	//This is used to keep track of the elapsed time from the timer for the gameClock in tick(int elapsedTime).
	private int elapsedTime = 0;
	//This is used to keep track of selected objects in the game.
	int selectedItems = 0;
    //Creating new Sound object set to the path of the game's background music.
    private Sound backGroundSound = new Sound("." + File.separator + "Sounds" + File.separator + "03-a-new-journey.wav");
	//This array will contain the game map with the first 5/6 rows/columns belong to the panels to form
    //the "board" and the other half for the Orbs that will be displayed on the panels.
    private GameWorldObject[][] gameMap = new GameWorldObject[10][12];
	//This will hold GameWorldObjects to be deleted from the game.
	private Vector<GameWorldObject> toBeDeletedCollection = new Vector<GameWorldObject>();
	//Rate for various orb matches.
	private int match3PointsRate = 50;
	private int match4PointsRate = 150;
	private int match5PointsRate = 300;
	private int match6PointsRate = 500;
	//Flat rate for all star orb matches. 60 seconds is the highest possible reward for a type of match.
	private int matchStarClockRate = 10;
	
	//This method is used to determine game conditions such as if the User reached the next level
	//or ran out of time.
	public void checkGameConditions()
	{
		//If the user has reached the game score goal, increase the goal and the game level.
		if(gamePoints >= gamePointsGoal)
		{
			setGamePointsGoal((int)(getGamePointsGoal()+ (getGamePointsGoal() * 0.5)));
			setGameLevel(getGameLevel() + 1);
			setGamePoints(0);
		}
		//If the user has run out of time, end the game.
		if(gameClock <= 0)
		{
			System.exit(0);
		}
	}
	//Method that sets the game score.
	public void setGamePoints(int gamePoints)
	{
		this.gamePoints = gamePoints;
	}
	//Method that returns the game score.
	public int getGamePoints()
	{
		return gamePoints;
	}
	//Method that increments the game score goal.
	public void setGamePointsGoal(int gamePoints)
	{
		this.gamePointsGoal = gamePoints;
	}
	//Method that returns the game score goal.
	public int getGamePointsGoal()
	{
		return gamePointsGoal;
	}
	//Method that sets the game clock.
	public void setGameClock(int time)
	{
		gameClock = time;
	}
	public int getGameClock()
	{
		return gameClock;
	}
	public String getGameClockText()
	{
		int gameClock = getGameClock();
		String minutes = String.valueOf(gameClock/60);
		String seconds = String.valueOf(gameClock % 60);
		if(Integer.valueOf(seconds) < 10)
		{
			seconds = "0"+seconds;
		}
		String gameClockText = minutes + ":" + seconds;
		return gameClockText;
	}
	//Method that sets the game level.
	public void setGameLevel(int gameLevel) 
	{
		this.gameLevel = gameLevel;
	}
	//Method that returns the game level.
	public int getGameLevel() 
	{
		return gameLevel;
	}
	public void setGameSound(boolean newGameSound)
	{
		gameSound = newGameSound;
	}
	public boolean getGameSound()
	{
		return gameSound;
	}
	//Method that returns the Sound object backGroundSound
	public Sound getBackGroundSound()
	{
		return backGroundSound;
	}
	//Method that sets the game's game mode
	public void setGameMode(boolean newGameMode)
	{
		gameMode = newGameMode;
	}
	//Method that returns the game's current game mode
	public boolean getGameMode()
	{
		return gameMode;
	}
	public GameWorldObject[][] getGameWorldCollection() 
	{
		return gameMap;
	}
	//Method sets the initial layout of objects in Game World and initializes gameStateValue currentFuelLevel
	public void initLayout()
	{
		createCheckerBoard();
		fillCheckerBoard();
	}
	//Method that creates black and white SquarePanels to create a checker board for the game.
	private void createCheckerBoard()
	{
		int xLocation = 60;
		int yLocation = 60;
		Color squareColor;
		//Rows;  5 Rows
		for(int row = 0; row < 5; row++)
		{
			//Columns; 6 Columns
			for(int col = 0; col < 6; col++)
			{
				if ((row + col) % 2 == 0)
				{
                    squareColor = Color.BLACK;
				}
				else
				{
					squareColor = Color.WHITE;
				}
				gameMap[row][col] = gwof.makeSquarePanel(squareColor, new Point2D.Double(xLocation, yLocation));
				//gameWorldCollection.add(gwof.makeSquarePanel(squareColor, new Point2D.Double(xLocation, yLocation)));
				//Incrementing the x location of each square
				xLocation += 120;	
			}
			//Resetting the xLocation. Otherwise, the squares will be made continuously
			//to the left as more rows are created.
			xLocation = 60;
			yLocation += 120;
		}
	}
	//Method that places random Orbs at the same locations of the SquarePanels.
	//Since the background of the Orb images are transparent, it appears the Orbs
	//are placed on top of a checker board.
	//I also use this method to fill in a 2D GameWorldObject array that will be used
	//for match checking.
	private void fillCheckerBoard()
	{
		int xLocation = 60;
		int yLocation = 60;
		int randomOrbNumber;
		Random randomNumber = new Random();
		GameWorldObject holdObject;
		//Rows;  5 Rows
		for(int row = 5; row <= gameMap.length - 1; row++)
		{
			//Columns; 6 Columns
			for(int col = 6; col <= gameMap[0].length - 1; col++)
			{
				randomOrbNumber = randomNumber.nextInt(10000);
				if(randomOrbNumber % 11 == 0)
				{
					holdObject = gwof.makeFireOrb(new Point2D.Double(xLocation, yLocation));
				}
				else if(randomOrbNumber % 3 == 0)
				{
					holdObject = gwof.makeWaterOrb(new Point2D.Double(xLocation, yLocation));
				}
				else if(randomOrbNumber % 5 == 0)
				{
					holdObject = gwof.makeLeafOrb(new Point2D.Double(xLocation, yLocation));
				}
				else if(randomOrbNumber % 7 == 0)
				{
					holdObject = gwof.makeLightOrb(new Point2D.Double(xLocation, yLocation));
				}
				else if (randomOrbNumber % 2 == 0)
				{
					holdObject = gwof.makeStarOrb(new Point2D.Double(xLocation, yLocation));
				}
				else
				{
					holdObject = gwof.makeDarknessOrb(new Point2D.Double(xLocation, yLocation));
				}
				//Incrementing the x location of each square
				xLocation += 120;	
				//Filling in the 2D array "gameMap" with the randomly generated Orbs for match checking.
				gameMap[row][col] = holdObject;
			}
			//Resetting the xLocation. Otherwise, the squares will be made continuously
			//to the left as more rows are created.
			xLocation = 60;
			yLocation += 120;
		}
	}
	//Method adds object into observer collection
    public void addObserver(IObserver obs)
    {
       observerCollection.add(obs);
    }
    //Method goes through the collection of observers and has them call their update method
    public void notifyObservers()
    {
    	proxy = new GameWorldProxy(this);
        for(IObserver iobj : observerCollection)
        {
           iobj.update((IObservable)proxy, null);
        }
    }
    //Method that selects a GameWorldObject based on the location of a 
    //mouse click, an Orb in this case, moves objects when the second object is 
    //selected and unselects all selected objects.
    //Used in Game inside the mouseClicked(MouseEvent e) method.
	public void setGameObjectSelect(Point2D p) 
	{
		for(int row = 5; row <= gameMap.length - 1; row++)
		{
			//Columns; 6 Columns
			for(int col = 6; col <= gameMap[0].length - 1; col++)
			{
				holdObject = gameMap[row][col];
				if(holdObject instanceof ISelectable)
				{
					ISelectable isobj = (ISelectable)holdObject;
					if(isobj.contains(p))
					{
						isobj.setSelectedStatus(true);
						//Increment "selectedItems" with each click on an Orb.
						selectedItems += 1;
						//On the first click, set the Orb to "holdMovableObject1".
						if(selectedItems == 1)
						{
							holdMovableObject1 = holdObject;
						}
						//On the second click, set the second Orb to "holdMovableObject2".
						//Reset "selectedItems" to 0.
						//Invoke "updatePositions()" which switches the Orbs.
						if(selectedItems == 2)
						{
							holdMovableObject2 = holdObject;
							selectedItems = 0;
							updatePositions();
							updateGameMapForGemSwitch();
							checkForFireOrbMatches();
							checkForWaterOrbMatches();
							checkForLeafOrbMatches();
							checkForDarknessOrbMatches();
							checkForLightOrbMatches();
							checkForStarOrbMatches();
							unselectAll();
							setDeleteAnimationStatus();
						}
				   }
			   }
	        }
    	}
		System.out.println();
		System.out.println("Map Before Change Due to Match Detection:");
		displayGameMapText();
	}
	//Method that has all movable objects to update their positions.
  	//Used in setGameObjectSelect(Point2D p) as I didn't want to rely movement
	//on the clock.
  	private void updatePositions()
  	{
  		Point2D holdLocation1;
  		Point2D holdLocation2;
  		IMovable imobj1 = (IMovable) holdMovableObject1;
  		IMovable imobj2 = (IMovable) holdMovableObject2;
  		holdLocation1 = holdMovableObject1.getLocation();
  		holdLocation2 = holdMovableObject2.getLocation();
  		imobj1.move(holdLocation2);
  		imobj2.move(holdLocation1);
  	}
  	//This method switches the locations of selected Orbs in the array.
  	private void updateGameMapForGemSwitch()
  	{
  		for(int row = 5; row <= gameMap.length - 1; row++)
  		{
  			for(int col = 6; col <= gameMap[0].length - 1; col++)
  			{
  				if(gameMap[row][col] == holdMovableObject1)
  				{
  					gameMap[row][col] = holdMovableObject2;
  				}
  				else if(gameMap[row][col] == holdMovableObject2)
  				{
  					gameMap[row][col] = holdMovableObject1;
  				}
  			}
  		}
  	}
	//This method is used to unselect all selectable objects.
	////Used in setGameObjectSelect(Point2D p) as I didn't want to rely unselecting
	//on the clock.
    private void unselectAll()
    {
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int col = 6; col <= gameMap[0].length - 1; col++)
			{
	    		holdObject = gameMap[row][col];
	    		if(holdObject instanceof ISelectable)
	    		{
	    			ISelectable isobj = (ISelectable)holdObject;
	    			isobj.setSelectedStatus(false);
	    		}
			}
    	}
    }
    //This method searches through the array for the deleted objects which left a "null" value
    //and replace the null locations with new Orbs.
    private void refillGameMapCollection()
    {
    	int xLocation = 60;
		int yLocation = 60;
		int randomOrbNumber;
		Random randomNumber = new Random();
		GameWorldObject holdObject;
    	for(int row = 5; row <= gameMap.length - 1; row++)
    	{
    		for(int col = 6; col <= gameMap[0].length - 1; col++)
    		{
    			if(gameMap[row][col] == null)
    			{
    				try 
    				{
						Thread.sleep(50,0);
					} 
    				catch (InterruptedException e) 
    				{
						e.printStackTrace();
					}
    				randomOrbNumber = randomNumber.nextInt(10000);
    				if(randomOrbNumber % 11 == 0)
    				{
    					holdObject = gwof.makeFireOrb(new Point2D.Double(xLocation, yLocation));
    				}
    				else if(randomOrbNumber % 3 == 0)
    				{
    					holdObject = gwof.makeWaterOrb(new Point2D.Double(xLocation, yLocation));
    				}
    				else if(randomOrbNumber % 5 == 0)
    				{
    					holdObject = gwof.makeLeafOrb(new Point2D.Double(xLocation, yLocation));
    				}
    				else if(randomOrbNumber % 7 == 0)
    				{
    					holdObject = gwof.makeLightOrb(new Point2D.Double(xLocation, yLocation));
    				}
    				else if (randomOrbNumber % 2 == 0)
    				{
    					holdObject = gwof.makeStarOrb(new Point2D.Double(xLocation, yLocation));
    				}
    				else
    				{
    					holdObject = gwof.makeDarknessOrb(new Point2D.Double(xLocation, yLocation));
    				}	
    				//Filling in the 2D array "gameMap" with the randomly generated Orbs for match checking.
    				gameMap[row][col] = holdObject;
    			}
    			//Incrementing the x location of each square
				xLocation += 120;
    		}
			//Resetting the xLocation. Otherwise, the squares will be made continuously
			//to the left as more rows are created.
			xLocation = 60;
			yLocation += 120;
    	}
    }
    //This subroutine goes through the GameWorldObjects to be deleted and set deletedStatus to true
    //if they are a deletable object with a deletion animation.
    private void setDeleteAnimationStatus()
    {
    	Iterator<GameWorldObject> theDeletedElements = toBeDeletedCollection.listIterator();
	    while(theDeletedElements.hasNext())
	    {
	    	GameWorldObject toBeDeletedObject = (GameWorldObject) theDeletedElements.next();
			if(toBeDeletedObject instanceof IDeletable)
			{
				IDeletable deletableObject = (IDeletable) toBeDeletedObject;
				deletableObject.setDeleteStatus(true);
			}
	    }
    }
    //This method will go through all soon to be deleted Orbs to check if they have finished 
    //their deletion animations. This is to prevent a "cut-off" of the animation when the
    //Orbs are deleted from the game map.
    private boolean checkIfDeleteAnimationsFinished()
    {
    	boolean finished = false;
    	Iterator<GameWorldObject> theDeletedElements = toBeDeletedCollection.listIterator();
	    while(theDeletedElements.hasNext())
	    {
	    	GameWorldObject toBeDeletedObject = (GameWorldObject) theDeletedElements.next();
	    	if(toBeDeletedObject instanceof IDeletable)
	    	{
	    		IDeletable idobj = (IDeletable) toBeDeletedObject;
	    		if(idobj.finishedDeletionAnimation() == true)
	    		{
	    			finished = true;
	    		}
	    		else
	    		{
	    			finished = false;
	    		}
	    	}
	    }
	    return finished;
    }
    //This method goes through the array with the collection of matched Orbs and replaces their
    //location with a "null" value.
    private void deleteFoundMatchesFromGameMap()
    {
    	Iterator<GameWorldObject> theDeletedElements = toBeDeletedCollection.listIterator();
	    while(theDeletedElements.hasNext())
	    {
	    	GameWorldObject toBeDeletedObject = (GameWorldObject) theDeletedElements.next();
	    	for(int row = 5; row <= gameMap.length - 1; row++)
	    	{
	    		for(int col = 6; col <= gameMap[0].length - 1; col++)
	    		{
	    			if(gameMap[row][col] == toBeDeletedObject)
	    			{
	    				gameMap[row][col] = null;
	    			}
	    		}
	    	}
	    }
    }
    //Method empties the to-be-deleted-collection.
    private void emptyToBeDeletedCollection()
    {
    	toBeDeletedCollection.removeAll(toBeDeletedCollection);
    }
    //Method checks for FireOrb matches in the gameMap.
    private void checkForFireOrbMatches()
    {
    	//Setting the counter variable.
    	int fireOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
				if(gameMap[row][i] instanceof FireOrb)
    	    	{
    	    		fireOrbMatches++;
    	    	}
				//If the chain was broken, check for up to 5 matches of Orbs.
				else
				{
					//If a Fire Orb match is found, print a message with the row number.
        	    	if(fireOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found fire 3 match in row "+ (row-4));
        	    	}
        	    	if(fireOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found fire 4 match in row "+ (row-4));
        	    	}
        	    	if(fireOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	        	toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found fire 5 match in row "+ (row-4));
        	    	}
					//Reset the match count as chain was broken.
		        	fireOrbMatches = 0;
				}
				//If the chain was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Water Orb match is found, print a message with the row number.
        	    	if(fireOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	    		toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found fire 3 match in row "+ (row-4));
        	    	}
        	    	if(fireOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found fire 4 match in row "+ (row-4));
        	    	}
        	    	if(fireOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found fire 5 match in row "+ (row-4));
        	    	}
        	    	if(fireOrbMatches == 6)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match6PointsRate);
        	    		System.out.println("Found fire 6 match in row "+ (row-4));
        	    	}
				}	
			}
			//Reset the match count for the next row.
        	fireOrbMatches = 0;
		}
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	//Looking for Fire Orbs.
    	    	if(gameMap[i][col] instanceof FireOrb)
    	    	{
    	    		fireOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	        else
				{
    	    		//If a Fire Orb match is found, print a message with the row number.
    	    		if(fireOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found fire 3 match in column "+ (col-5));
        	    	}
        	    	if(fireOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	        	System.out.println("Found fire 4 match in column "+ (col-5));
        	    	}
					//Reset the match count as the chain was broken.
		        	fireOrbMatches = 0;
				}
    	    	//If the chain was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
					//If a Fire Orb match is found, print a message with the row number.
    	    		if(fireOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	toBeDeletedCollection.add(gameMap[i][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found fire 3 match in column "+ (col-5));
        	    	}
        	    	if(fireOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		toBeDeletedCollection.add(gameMap[i][col]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	        	System.out.println("Found fire 4 match in column "+ (col-5));
        	    	}
        	    	if(fireOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		toBeDeletedCollection.add(gameMap[i][col]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	        	System.out.println("Found fire 5 match in column "+ (col-5));
        	    	}
				}
    	    }
    	    //Reset the match count for the next row.
        	fireOrbMatches = 0;
    	}
    }
    //Method checks for WaterOrb matches in the gameMap.
    private void checkForWaterOrbMatches()
    {
    	//Setting the counter variable.
    	int waterOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
    	    	//Looking for Water Orbs.
    	    	if(gameMap[row][i] instanceof WaterOrb)
    	    	{
    	    		waterOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 5 matches of Orbs.
    	    	else
				{
    	    		//If a Water Orb match is found, print a message with the row number.
        	    	if(waterOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found water 3 match in row "+ (row-4));
        	    	}
        	    	if(waterOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found water 4 match in row "+ (row-4));
        	    	}
        	    	if(waterOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	        	toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found water 5 match in row "+ (row-4));
        	    	}
					//Reset the match count as the chain was broken.
		        	waterOrbMatches = 0;
				}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Water Orb match is found, print a message with the row number.
        	    	if(waterOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	    		toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found water 3 match in row "+ (row-4));
        	    	}
        	    	if(waterOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found water 4 match in row "+ (row-4));
        	    	}
        	    	if(waterOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found water 5 match in row "+ (row-4));
        	    	}
        	    	if(waterOrbMatches == 6)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	        	toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match6PointsRate);
        	    		System.out.println("Found water 6 match in row "+ (row-4));
        	    	}
				}
			}
			//Reset the match count for the next row.
    	    waterOrbMatches = 0;
		}
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	//Looking for Water Orbs.
    	    	if(gameMap[i][col] instanceof WaterOrb)
    	    	{
    	    		waterOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	    	else
    	    	{
    	    		//If a Water Orb match is found, print a message with the col number.
        	    	if(waterOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found water 3 match in column "+ (col-5));
        	    	}
        	    	if(waterOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found water 4 match in column "+ (col-5));
        	    	}
    	    		//Reset the match count as the chain was broken.
    	    		waterOrbMatches = 0;
    	    	}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
					//If a Water Orb match is found, print a message with the col number.
        	    	if(waterOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	toBeDeletedCollection.add(gameMap[i][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found water 3 match in column "+ (col-5));
        	    	}
        	    	if(waterOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		toBeDeletedCollection.add(gameMap[i][col]);
        	    		setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found water 4 match in column "+ (col-5));
        	    	}
        	    	if(waterOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		toBeDeletedCollection.add(gameMap[i][col]);
        	    		setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found water 5 match in column "+ (col-5));
        	    	}
				}
    	    }
    	    //Reset the match count for the next row.
    	    waterOrbMatches = 0;
    	}
    }
    //Method checks for LeafOrb matches in the gameMap.
    private void checkForLeafOrbMatches()
    {
    	//Setting the counter variable.
    	int leafOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
				if(gameMap[row][i] instanceof LeafOrb)
    	    	{
    	    		leafOrbMatches++;
    	    	}
				//If the chain was broken, check for up to 5 matches of Orbs.
				else
				{
					//If a Leaf Orb match is found, print a message with the row number.
        	    	if(leafOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found leaf 3 match in row "+ (row-4));
        	    	}
        	    	if(leafOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found leaf 4 match in row "+ (row-4));
        	    	}
        	    	if(leafOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	        	toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found leaf 5 match in row "+ (row-4));
        	    	}
					//Reset the match count as the chain was broken.
		        	leafOrbMatches = 0;
				}
				//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Water Orb match is found, print a message with the row number.
        	    	if(leafOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	    		toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	    		System.out.println("Found leaf 3 match in row "+ (row-4));
        	    	}
        	    	if(leafOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	    		toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match4PointsRate);
        	    		System.out.println("Found leaf 4 match in row "+ (row-4));
        	    	}
        	    	if(leafOrbMatches == 5)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	    		toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match5PointsRate);
        	    		System.out.println("Found leaf 5 match in row "+ (row-4));
        	    	}
        	    	if(leafOrbMatches == 6)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[row][i-5]);
        	    		toBeDeletedCollection.add(gameMap[row][i-4]);
        	        	toBeDeletedCollection.add(gameMap[row][i-3]);
        	        	toBeDeletedCollection.add(gameMap[row][i-2]);
        	        	toBeDeletedCollection.add(gameMap[row][i-1]);
        	        	toBeDeletedCollection.add(gameMap[row][i]);
        	        	setGamePoints(getGamePoints()+match6PointsRate);
        	    		System.out.println("Found leaf 6 match in row "+ (row-4));
        	    	}
				}
			}
			//Reset the match count for the next row.
        	leafOrbMatches = 0;
		}
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	if(gameMap[i][col] instanceof LeafOrb)
    	    	{
    	    		leafOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	    	else
    	    	{
    	    		//If a Leaf Orb match is found, print a message with the col number.
        	    	if(leafOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found leaf 3 match in column "+ (col-5));
        	    	}
        	    	if(leafOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		setGamePoints(getGamePoints()+match4PointsRate);
           	    		System.out.println("Found leaf 4 match in column "+ (col-5));
        	    	}
    	    		//Reset the match count as the chain was broken.
    	    		leafOrbMatches = 0;
    	    	}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
	    	    	//If a Leaf Orb match is found, print a message with the col number.
	    	    	if(leafOrbMatches == 3)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	        	toBeDeletedCollection.add(gameMap[i][col]);
	    	        	setGamePoints(getGamePoints()+match3PointsRate);
	    	        	System.out.println("Found leaf 3 match in column "+ (col-5));
	    	    	}
	    	    	if(leafOrbMatches == 4)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match4PointsRate);
	       	    		System.out.println("Found leaf 4 match in column "+ (col-5));
	    	    	}
	    	    	if(leafOrbMatches == 5)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-4][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match5PointsRate);
	    	    		System.out.println("Found leaf 5 match in column "+ (col-5));
	    	    	}
				}
    	    }
    	    //Reset the match count for the next row.
        	leafOrbMatches = 0;
    	}
    }
    //Method checks for DarknessOrb matches in the gameMap.
    private void checkForDarknessOrbMatches()
    {
    	//Setting the counter variable.
    	int darknessOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
		    	if(gameMap[row][i] instanceof DarknessOrb)
		    	{
		    		darknessOrbMatches++;
		    	}
		    	//If the chain was broken, check for up to 5 matches of Orbs.
		    	else
				{
		    		//If a Darkness Orb match is found, print a message with the row number.
			    	if(darknessOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match3PointsRate);
			    		System.out.println("Found dark 3 match in row "+ (row-4));
			    	}
			    	if(darknessOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match4PointsRate);
			    		System.out.println("Found dark 4 match in row "+ (row-4));
			    	}
			    	if(darknessOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			        	toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match5PointsRate);
			    		System.out.println("Found dark 5 match in row "+ (row-4));
			    	}
					//Reset the match count as the chain was broken.
		        	darknessOrbMatches = 0;
				}
		    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Darkness Orb match is found, print a message with the row number.
			    	if(darknessOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			    		toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match3PointsRate);
			    		System.out.println("Found dark 3 match in row "+ (row-4));
			    	}
			    	if(darknessOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match4PointsRate);
			    		System.out.println("Found dark 4 match in row "+ (row-4));
			    	}
			    	if(darknessOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match5PointsRate);
			    		System.out.println("Found dark 5 match in row "+ (row-4));
			    	}
			    	if(darknessOrbMatches == 6)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match6PointsRate);
			    		System.out.println("Found dark 6 match in row "+ (row-4));
			    	}
				}
			}
			//Reset the match count for the next row.
        	darknessOrbMatches = 0;
		}
    	
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	if(gameMap[i][col] instanceof DarknessOrb)
    	    	{
    	    		darknessOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	    	else
    	    	{
    	    		//If a Darkness Orb match is found, print a message with the col number.
        	    	if(darknessOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found dark 3 match in column "+ (col-5));
        	    	}
        	    	if(darknessOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		setGamePoints(getGamePoints()+match4PointsRate);
           	    		System.out.println("Found dark 4 match in column "+ (col-5));
        	    	}
    	    		//Reset the match count as the chain was broken.
    	    		darknessOrbMatches = 0;
    	    	}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
	    	    	//If a Darkness Orb match is found, print a message with the col number.
	    	    	if(darknessOrbMatches == 3)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	        	toBeDeletedCollection.add(gameMap[i][col]);
	    	        	setGamePoints(getGamePoints()+match3PointsRate);
	    	        	System.out.println("Found dark 3 match in column "+ (col-5));
	    	    	}
	    	    	if(darknessOrbMatches == 4)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match4PointsRate);
	       	    		System.out.println("Found dark 4 match in column "+ (col-5));
	    	    	}
	    	    	if(darknessOrbMatches == 5)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-4][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match5PointsRate);
	    	    		System.out.println("Found dark 5 match in column "+ (col-5));
	    	    	}
				}
    	    }
    	    //Reset the match count for the next row.
        	darknessOrbMatches = 0;
    	}
    }
    //Method checks for LightOrb matches in the gameMap.
    private void checkForLightOrbMatches()
    {
    	//Setting the counter variable.
    	int lightOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
		    	if(gameMap[row][i] instanceof LightOrb)
		    	{
		    		lightOrbMatches++;
		    	}
		    	//If the chain was broken, check for up to 5 matches of Orbs.
		    	else
				{
		    		//If a Light Orb match is found, print a message with the row number.
			    	if(lightOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match3PointsRate);
			    		System.out.println("Found light 3 match in row "+ (row-4));
			    	}
			    	if(lightOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match4PointsRate);
			    		System.out.println("Found light 4 match in row "+ (row-4));
			    	}
			    	if(lightOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			        	toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGamePoints(getGamePoints()+match5PointsRate);
			    		System.out.println("Found light 5 match in row "+ (row-4));
			    	}
					//Reset the match count as the chain was broken.
		        	lightOrbMatches = 0;
				}
		    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Light Orb match is found, print a message with the row number.
			    	if(lightOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			    		toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match3PointsRate);
			    		System.out.println("Found light 3 match in row "+ (row-4));
			    	}
			    	if(lightOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match4PointsRate);
			    		System.out.println("Found light 4 match in row "+ (row-4));
			    	}
			    	if(lightOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match5PointsRate);
			    		System.out.println("Found light 5 match in row "+ (row-4));
			    	}
			    	if(lightOrbMatches == 6)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGamePoints(getGamePoints()+match6PointsRate);
			    		System.out.println("Found light 6 match in row "+ (row-4));
			    	}
				}
			}
			//Reset the match count for the next row.
        	lightOrbMatches = 0;
		}
    	
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	if(gameMap[i][col] instanceof LightOrb)
    	    	{
    	    		lightOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	    	else
    	    	{
    	    		//If a Light Orb match is found, print a message with the col number.
        	    	if(lightOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGamePoints(getGamePoints()+match3PointsRate);
        	        	System.out.println("Found light 3 match in column "+ (col-5));
        	    	}
        	    	if(lightOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		setGamePoints(getGamePoints()+match4PointsRate);
           	    		System.out.println("Found light 4 match in column "+ (col-5));
        	    	}
    	    		//Reset the match count as the chain was broken.
    	    		lightOrbMatches = 0;
    	    	}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
	    	    	//If a Light Orb match is found, print a message with the col number.
	    	    	if(lightOrbMatches == 3)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	        	toBeDeletedCollection.add(gameMap[i][col]);
	    	        	setGamePoints(getGamePoints()+match3PointsRate);
	    	        	System.out.println("Found light 3 match in column "+ (col-5));
	    	    	}
	    	    	if(lightOrbMatches == 4)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match4PointsRate);
	       	    		System.out.println("Found light 4 match in column "+ (col-5));
	    	    	}
	    	    	if(lightOrbMatches == 5)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-4][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGamePoints(getGamePoints()+match5PointsRate);
	    	    		System.out.println("Found light 5 match in column "+ (col-5));
	    	    	}
				}
    	    }
    	    //Reset the match count for the next row.
        	lightOrbMatches = 0;
    	}
    }
    //Method checks for StarOrb matches in the gameMap.
    private void checkForStarOrbMatches()
    {
    	//Setting the counter variable.
    	int starOrbMatches = 0;
    	//Checking for Orb matches horizontally.
    	for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int i = 6; i <= gameMap[0].length - 1; i++)
			{
		    	if(gameMap[row][i] instanceof StarOrb)
		    	{
		    		starOrbMatches++;
		    	}
		    	//If the chain was broken, check for up to 5 matches of Orbs.
		    	else
				{
		    		//If a Light Orb match is found, print a message with the row number.
			    	if(starOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 3 match in row "+ (row-4));
			    	}
			    	if(starOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 4 match in row "+ (row-4));
			    	}
			    	if(starOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			        	toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 5 match in row "+ (row-4));
			    	}
					//Reset the match count as the chain was broken.
		        	starOrbMatches = 0;
				}
		    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap[0].length - 1)
				{
					//If a Star Orb match is found, print a message with the row number.
			    	if(starOrbMatches == 3)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			    		toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 3 match in row "+ (row-4));
			    	}
			    	if(starOrbMatches == 4)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			    		toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 4 match in row "+ (row-4));
			    	}
			    	if(starOrbMatches == 5)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 5 match in row "+ (row-4));
			    	}
			    	if(starOrbMatches == 6)
			    	{
			    		toBeDeletedCollection.add(gameMap[row][i-5]);
			    		toBeDeletedCollection.add(gameMap[row][i-4]);
			    		toBeDeletedCollection.add(gameMap[row][i-3]);
			        	toBeDeletedCollection.add(gameMap[row][i-2]);
			        	toBeDeletedCollection.add(gameMap[row][i-1]);
			        	toBeDeletedCollection.add(gameMap[row][i]);
			        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
			    		System.out.println("Found star 6 match in row "+ (row-4));
			    	}
				}
			}
			//Reset the match count for the next row.
        	starOrbMatches = 0;
		}
    	
    	//Checking for matches vertically.
    	for(int col = 6; col <= gameMap[0].length - 1; col++)
    	{      
    	    for(int i = 5; i <= gameMap.length - 1; i++)
    	    {
    	    	if(gameMap[i][col] instanceof StarOrb)
    	    	{
    	    		starOrbMatches++;
    	    	}
    	    	//If the chain was broken, check for up to 4 matches of Orbs.
    	    	else
    	    	{
    	    		//If a Star Orb match is found, print a message with the col number.
        	    	if(starOrbMatches == 3)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	    		toBeDeletedCollection.add(gameMap[i-2][col]);
        	        	toBeDeletedCollection.add(gameMap[i-1][col]);
        	        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
        	        	System.out.println("Found star 3 match in column "+ (col-5));
        	    	}
        	    	if(starOrbMatches == 4)
        	    	{
        	    		toBeDeletedCollection.add(gameMap[i-4][col]);
        	    		toBeDeletedCollection.add(gameMap[i-3][col]);
        	        	toBeDeletedCollection.add(gameMap[i-2][col]);
        	    		toBeDeletedCollection.add(gameMap[i-1][col]);
        	    		setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
           	    		System.out.println("Found star 4 match in column "+ (col-5));
        	    	}
    	    		//Reset the match count as the chain was broken.
    	    		starOrbMatches = 0;
    	    	}
    	    	//If the check was unbroken toward the end, check for possible matches.
				if(i == gameMap.length - 1)
				{
	    	    	//If a Light Orb match is found, print a message with the col number.
	    	    	if(starOrbMatches == 3)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	        	toBeDeletedCollection.add(gameMap[i][col]);
	    	        	setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
	    	        	System.out.println("Found star 3 match in column "+ (col-5));
	    	    	}
	    	    	if(starOrbMatches == 4)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-2][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
	       	    		System.out.println("Found star 4 match in column "+ (col-5));
	    	    	}
	    	    	if(starOrbMatches == 5)
	    	    	{
	    	    		toBeDeletedCollection.add(gameMap[i-4][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-3][col]);
	    	        	toBeDeletedCollection.add(gameMap[i-2][col]);
	    	    		toBeDeletedCollection.add(gameMap[i-1][col]);
	    	    		toBeDeletedCollection.add(gameMap[i][col]);
	    	    		setGameClock(getGameClock()+matchStarClockRate*starOrbMatches);
	    	    		System.out.println("Found star 5 match in column "+ (col-5));
	    	    	}
				}
    	    }
    	    //Reset the match count for the next row.
        	starOrbMatches = 0;
    	}
    }
	public void tick(int elapsedTime)
	{
		this.elapsedTime += elapsedTime;
		//Every 100 milliseconds check if soon-to-be-deleted Orbs finished their deletion animations,
		//delete them from the map, empty the to-be-deleted collection and refill the map.
		if(this.elapsedTime % 100 == 0)
		{
			if(checkIfDeleteAnimationsFinished() == true)
			{
				deleteFoundMatchesFromGameMap();
		    	emptyToBeDeletedCollection();
		    	refillGameMapCollection();
			}
		}
		//Every 1000 milliseconds or 1 second, decrement the clock and rest "elapsedTime" to remove
		//possibility of it reaching the limit of "int".
		if(this.elapsedTime % 1000 == 0)
		{
			setGameClock(getGameClock() - 1);
			//Resetting the variable to zero.
			this.elapsedTime = 0;
		}
		checkGameConditions();
		notifyObservers();
	}
	//Method displays the contents of gameMap.
	public void displayGameMapText()
	{
		for(int row = 5; row <= gameMap.length - 1; row++)
		{
			for(int col = 6; col <= gameMap[0].length - 1; col++)
			{
				System.out.print(gameMap[row][col] + " | ");
			}
			System.out.println();
	    }
	    System.out.println();
	} 
}
