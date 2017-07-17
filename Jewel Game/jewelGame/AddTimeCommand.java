package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

//This class defines a command that implements a time increment operation.
public class AddTimeCommand extends AbstractAction
{
	private static AddTimeCommand addTimeCommand;
	private GameWorld gameWorld;
  
	private AddTimeCommand()
	{
		super("Add 30 Seconds");
	}
	//Method that creates a new instance of AddTimeCommand and if it is already created, 
	//return the same AddTimeCommand to any object that calls for it.
	public static AddTimeCommand getAddTimeCommand()
	{
  		if(addTimeCommand == null)
	    {
		    addTimeCommand = new AddTimeCommand();
	    }
	    return addTimeCommand;
	}
	//Setting target for command that will allow it to access a specific method when the command is invoked.
	public void setTarget(GameWorld gameWorld)
	{
		this.gameWorld = gameWorld;
	}
	public void actionPerformed(ActionEvent e)
	{
		gameWorld.setGameClock(30);
	}
}
