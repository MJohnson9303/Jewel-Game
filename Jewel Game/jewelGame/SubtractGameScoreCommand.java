package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

//This class defines a command that implements a game score decrement operation.
public class SubtractGameScoreCommand extends AbstractAction
{
	private static SubtractGameScoreCommand subtractGameScoreCommand;
	private GameWorld gameWorld;
  
	private SubtractGameScoreCommand()
	{
		super("Subtract 100 Points");
	}
	//Method that creates a new instance of SubtractGameScoreCommand and if it is already created, 
	//return the same SubtractGameScoreCommand to any object that calls for it.
	public static SubtractGameScoreCommand getSubtractGameScoreCommand()
	{
  		if(subtractGameScoreCommand == null)
	    {
		    subtractGameScoreCommand = new SubtractGameScoreCommand();
	    }
	    return subtractGameScoreCommand;
	}
	//Setting target for command that will allow it to access a specific method when the command is invoked.
	public void setTarget(GameWorld gameWorld)
	{
		this.gameWorld = gameWorld;
	}
	public void actionPerformed(ActionEvent e)
	{
		gameWorld.setGamePoints(-100);
	}
}