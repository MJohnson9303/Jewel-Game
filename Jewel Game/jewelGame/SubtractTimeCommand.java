package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

//This class defines a command that implements a time increment operation.
public class SubtractTimeCommand extends AbstractAction
{
	private static SubtractTimeCommand subtractTimeCommand;
	private GameWorld gameWorld;
  
	private SubtractTimeCommand()
	{
		super("Subtract 30 Seconds");
	}
	//Method that creates a new instance of SubtractTimeCommand and if it is already created, 
	//return the same SubtractTimeCommand to any object that calls for it.
	public static SubtractTimeCommand getAddTimeCommand()
	{
  		if(subtractTimeCommand == null)
  		{
		    subtractTimeCommand = new SubtractTimeCommand();
	    }
	    return subtractTimeCommand;
	}
	//Setting target for command that will allow it to access a specific method when the command is invoked.
	public void setTarget(GameWorld gameWorld)
	{
		this.gameWorld = gameWorld;
	}
	public void actionPerformed(ActionEvent e)
	{
		gameWorld.setGameClock(-30);
	}
}
