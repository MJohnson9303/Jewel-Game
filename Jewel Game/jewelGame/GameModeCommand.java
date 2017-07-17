package jewelGame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Timer;

//This class defines a command that implements a tick operation.
public class GameModeCommand extends AbstractAction
{
  private static GameModeCommand gameModeCommand;
  private GameWorld gameWorld;
  private Timer timer;
  private Sound gameSound;
  
  private GameModeCommand()
  {
     super("Pause");
  }
  //Method that creates a new instance of GameModeCommand and if it is already created, 
  //return the same GameModeCommand to any object that calls for it.
  public static GameModeCommand getGameMode()
  {
  	if(gameModeCommand == null)
	    {
		    gameModeCommand = new GameModeCommand();
	    }
	    return gameModeCommand;
  }
  //Setting target for command that will allow it to access a specific method when the command is invoked.
  public void setTarget(GameWorld gameWorld, Timer timer)
  {
	  this.gameWorld = gameWorld;
	  this.timer = timer;
	  this.gameSound = gameWorld.getBackGroundSound();
  }
  public void actionPerformed(ActionEvent e)
  {
	  if(gameWorld.getGameMode() == true)
	  {
		  super.putValue(NAME, "Play");
		  timer.stop();
		  gameSound.stop();
		  gameWorld.setGameMode(false);
	  }
	  else
	  {
		  super.putValue(NAME, "Pause");
		  timer.start();
		  if(gameWorld.getGameSound() == true)
		  {
			  gameSound.loop();
		  }
		  gameWorld.setGameMode(true);
	  }
  }
}

