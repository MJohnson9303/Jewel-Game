package jewelGame;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

//This class defines a command that implements a change sound status operation. 
public class SoundCommand extends AbstractAction
{
	 private static SoundCommand soundCommand;
     private GameWorld gameWorld;
     private Sound backGroundSound;
	 private SoundCommand()
	 {
	    super("Sound");
	 }
	 //Method that creates a new instance of SoundCommand and if it is already created, 
     //return the same SoundCommand to any object that calls for it
     public static SoundCommand getSound()
     {
    	 if(soundCommand == null)
	     {
		     soundCommand = new SoundCommand();
	     }
	     return soundCommand;
     }
     //Setting target for command that will allow it to access a specific method when the command is invoked
     public void setTarget(GameWorld gameWorld)
     {
     	this.gameWorld = gameWorld;
     	backGroundSound = gameWorld.getBackGroundSound();
     }
     //If the check box is selected, it will change the game's state value "sound" to on and play the game's background sound 
     //if the game is in play mode and sound is on. If the check box is unselected, it will change the game's state value
     //"sound" to off and stop the game's background sound.
	 public void actionPerformed(ActionEvent e)
	 {
	    AbstractButton abstractButton = (AbstractButton) e.getSource();
	    boolean selected = abstractButton.getModel().isSelected();
	    gameWorld.setGameSound(selected);
	    if(gameWorld.getGameMode() == true && gameWorld.getGameSound() == true)
	    {
	    	backGroundSound.loop();
	    }
	    if(gameWorld.getGameSound() == false)
	    {
	    	backGroundSound.stop();
	    }
	 }
}
