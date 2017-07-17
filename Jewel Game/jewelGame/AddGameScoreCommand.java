package jewelGame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

//This class defines a command that implements a game score increment operation.
public class AddGameScoreCommand extends AbstractAction
{
  private static AddGameScoreCommand addGameScoreCommand;
  private GameWorld gameWorld;
  
  private AddGameScoreCommand()
  {
     super("Add 100 Points");
  }
  //Method that creates a new instance of AddGameScoreCommand and if it is already created, 
  //return the same AddGameScoreCommand to any object that calls for it.
  public static AddGameScoreCommand getAddGameScoreCommand()
  {
  		if(addGameScoreCommand == null)
	    {
		    addGameScoreCommand = new AddGameScoreCommand();
	    }
	    return addGameScoreCommand;
  }
  //Setting target for command that will allow it to access a specific method when the command is invoked.
  public void setTarget(GameWorld gameWorld)
  {
	  this.gameWorld = gameWorld;
  }
  public void actionPerformed(ActionEvent e)
  {
	  gameWorld.setGamePoints(100);
  }
}


