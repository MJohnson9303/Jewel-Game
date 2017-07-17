package jewelGame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
//This class will allow a User to use "wav" audio files in the game.
public class Sound 
{
	AudioClip soundClip;
	public Sound(String fileName)
	{
		try
		{
			File file = new File(fileName);
			if(file.exists())
			{
				soundClip = Applet.newAudioClip(file.toURI().toURL());
			}
			else
			{
				throw new RuntimeException("File not found: " + fileName);
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException("Problem with " +fileName + ": " + e);
		}
	}
	public void play()
	{
		soundClip.play();
	}
	public void loop()
	{
		soundClip.loop();
	}
	public void stop()
	{
		soundClip.stop();
	}
}

