package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

//This class defines a command that implements a tick operation.
public class TickCommand extends AbstractAction
{
    private static TickCommand tickCommand;
    private GameWorld gameWorld;
    private int elapsedTime;
    
    private TickCommand()
    {
       super("Tick");
    }
    //Method that creates a new instance of TickCommand and if it is already created, 
    //return the same TickCommand to any object that calls for it.
    public static TickCommand getTick()
    {
    	if(tickCommand == null)
	    {
		    tickCommand = new TickCommand();
	    }
	    return tickCommand;
    }
    //Setting target for command that will allow it to access a specific method when the command is invoked.
    public void setTarget(GameWorld gameWorld)
    {
    	this.gameWorld = gameWorld;
    }
    //Setting the time cycle for the command.
    public void setTimerCycle(int elapsedTime)
    {
    	this.elapsedTime = elapsedTime;
    }
    //Calls the Game World "tick" method for every Timer event.
    public void actionPerformed(ActionEvent e)
    {
       gameWorld.tick(elapsedTime);
    }
}
