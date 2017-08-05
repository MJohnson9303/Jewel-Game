package jewelGame;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

//Observer class that observes GameWorld for game state value changes and 
//displays information such as level, score and time remaining to the user.
public class ScoreView extends JPanel implements IObserver
{
   private GameWorldProxy gameWorldProxy;
   private JLabel timeRemainingLabel = new JLabel();
   private JProgressBar progressBar = new JProgressBar();
   private JLabel gameLevelLabel = new JLabel();
   private JLabel gameScoreLabel = new JLabel();
   private JLabel gameScoreGoalLabel = new JLabel();
   private JLabel soundLabel = new JLabel();
   //Constructor that creates adds labels into the ScoreView panel and sets a black border around it.
   //It also sets up the progress bar for the user's time limit.
   public ScoreView()
   {
	   timeRemainingLabel.setText("Time Remaining");
	   add(timeRemainingLabel);
	   progressBar.setMinimum(0);
	   progressBar.setMaximum(60);
	   progressBar.setStringPainted(true);
	   add(progressBar);
	   add(gameLevelLabel);
	   add(gameScoreLabel);
	   add(gameScoreGoalLabel);
	   add(soundLabel);
	   setBorder(new LineBorder(Color.black, 2));
   }
   //Update method that sets the text inside the scorePanel to current game state information.
   public void update(IObservable o, Object arg)
   {
      gameWorldProxy = (GameWorldProxy) o;
      progressBar.setValue(gameWorldProxy.getGameClock());
      progressBar.setString(gameWorldProxy.getGameClockText());
      gameLevelLabel.setText("Level: "+gameWorldProxy.getGameLevel());
      gameScoreLabel.setText("Score: "+gameWorldProxy.getGamePoints());
      gameScoreGoalLabel.setText("Goal: "+gameWorldProxy.getGamePointsGoal());
      soundLabel.setText("Sound: "+gameWorldProxy.getGameSound());
      if(gameWorldProxy.getGameSound() == true)
      {
         soundLabel.setText("Sound: ON");
      }
      else
      {
         soundLabel.setText("Sound: OFF");
      }
   }
}
