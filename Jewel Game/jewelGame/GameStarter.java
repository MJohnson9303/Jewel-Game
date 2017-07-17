package jewelGame;

import javax.swing.JOptionPane;

//This class starts the game.
public class GameStarter 
{
	public static void main(String[] args) 
	{
		int result = JOptionPane.showConfirmDialog
                (null, 
                "In this game I use music from Puzzle and Dragons and edited images\n"
                + "from Puzzle and Dragons and\n"
                + "Puzzle and Dragons Super Mario Bros. Edition. I do not own them and\n"
                + "they belong to GungHo and Nintendo. I merely use them because I like them\n"
                + "and this game was to try my hand at making a simple jewel matching game.\n"
                + "If you have no issue with this, then please continue. If you do, then you may choose\n"
                + "to click on 'No'.",
                "Important Message Before You Begin", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
		{
			Game g = new Game();
		}
		else
		{
			System.exit(0);
		}
	}
}
