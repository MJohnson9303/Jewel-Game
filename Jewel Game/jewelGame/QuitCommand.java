package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*This class defines a command that implements a quit operation. 
  It prompts for confirmation of the user's intent to quit and exits if the user confirms.*/
public class QuitCommand extends AbstractAction
{
	private static QuitCommand quitCommand;
	private Timer timer;
    private QuitCommand()
    {
       super("Quit");
    }
    //Method that creates a new instance of QuitCommand and if it is already created, return the same QuitCommand
    //to any object that calls for it
    public static QuitCommand getQuit()
    {
    	if(quitCommand == null)
    	{
    		quitCommand = new QuitCommand();
    	}
    	return quitCommand;
    }
    //Setting target for command that will allow it to access a specific method when the command is invoked.
    public void setTarget(Timer timer)
    {
    	this.timer = timer;
    }
    public void actionPerformed(ActionEvent e)
    {
    	//Stopping the timer while the user makes a decision.
    	timer.stop();
    	int result = JOptionPane.showConfirmDialog
	                   (null, 
	                   "Are you sure you want to exit?",
	                   "Confirm Exit", 
	                   JOptionPane.YES_NO_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
	     if(result == JOptionPane.YES_OPTION)
	     {
	        System.exit(0);
	     }
	     //If the user doesn't quit the game, restart the timer.
	     else
	     {
	    	 timer.start();
	     }
    }
}
