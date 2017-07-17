package jewelGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

//This class will execute commands based on user input and will check for unknown or illegal commands.
public class Game extends JFrame implements ActionListener
{
	//Private objects
	private GameWorld gw;
	private MapView mv;
	private ScoreView sv;
	//Private JMenuBar, JMenu, JMenuItem, JCheckbox,JPanel, JButton, InputMap, ActionMap and KeyStroke objects
	//needed for building a GUI interface and key binding for user interaction with the progam.
	//AbstractAction command objects.
	private SoundCommand soundCommand;
    private TickCommand tickCommand;
    private GameModeCommand gameModeCommand;
    private AddTimeCommand addTimeCommand;
    private SubtractTimeCommand subtractTimeCommand;
    private AddGameScoreCommand addGameScoreCommand;
    private SubtractGameScoreCommand subtractGameScoreCommand;
    private QuitCommand quitCommand;
	//Timer object to be used for object animation.
	private Timer timer; 
	//This integer variable will be the time cycle for the timer. Set to 1/10 second.
	//Timer is set to 1/10 a second with this to make the score update more frequent.
	//GameWorld "tick" method will take this into account for count-down clock.
	private int elapsedTime = 100;
	//Construct game world and initialize the layout of the game.
	public Game()
	{
		gw = new GameWorld();
		mv = new MapView();
		sv = new ScoreView();
		gw.addObserver(mv);
		gw.addObserver(sv);
		gw.initLayout();
		gw.notifyObservers();
		//Creating new instance of timer object with it cycling every 1000 milliseconds and Game
		//as the ActionListener.
		timer = new Timer(elapsedTime, this);
		//Setting title on GUI frame.
		this.setTitle("Jewel Game");
		//Setting size of GUI frame.
		this.setSize(890, 689);
		//Setting the GUI frame to not be resizable.
		this.setResizable(false);
		//Setting "x" button operation to close program on default.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Creating singleton.
		soundCommand = SoundCommand.getSound();
		tickCommand = TickCommand.getTick();
		gameModeCommand = GameModeCommand.getGameMode();
		addTimeCommand = AddTimeCommand.getAddTimeCommand();
		subtractTimeCommand = SubtractTimeCommand.getAddTimeCommand();
		addGameScoreCommand = AddGameScoreCommand.getAddGameScoreCommand();
		subtractGameScoreCommand = SubtractGameScoreCommand.getSubtractGameScoreCommand();
		quitCommand = QuitCommand.getQuit();
		//Setting targets for command.
		soundCommand.setTarget(gw);
	    tickCommand.setTarget(gw);
	    gameModeCommand.setTarget(gw, timer);
	    addTimeCommand.setTarget(gw);
	    subtractTimeCommand.setTarget(gw);
	    addGameScoreCommand.setTarget(gw);
	    subtractGameScoreCommand.setTarget(gw);
	    quitCommand.setTarget(timer);
		//Constructing command objects to be used by GUI buttons and menu items indirectly using the Simpleton Design Pattern.
		//This is to insure that no more than 1 instance exists at a time.
		//Creating and attaching menu bar to GUI.
	    JMenuBar bar = createMenuBar();
	    this.setJMenuBar(bar);
	    //Adding the ScoreView panel to the top of the GUI frame.
	    this.add(sv, BorderLayout.NORTH);
	    //Adding the MapView panel to the center of the GUI frame.
	    this.add(mv, BorderLayout.CENTER);
        //Creating and attaching button panel to the GUI frame.
        this.add(buttonPanelSetUp(), BorderLayout.WEST);
        //Requesting focus for the MapView panel.
        mv.requestFocus();
	    //Starting the timer.
	    timer.start();
	    //Starting the background music.
	    Sound gameSound = gw.getBackGroundSound();
	    gameSound.loop();
		//Making GUI visible.
	    this.setVisible(true);
	}
	//Private method that creates a menu bar with two menus and multiple menu items and returns the menu bar.
	private JMenuBar createMenuBar()
	{
		JMenuBar bar;
		JMenu fileMenu;
		JCheckBox soundCheckBox;
		JMenuItem fileAbout;
	    bar = new JMenuBar();
	    fileMenu = new JMenu("Options");
        soundCheckBox = new JCheckBox("Sound", true);
        fileMenu.add(soundCheckBox);
        fileAbout = new JMenuItem("About");
        fileMenu.add(fileAbout);
        bar.add(fileMenu);
        //Attaching AbstractAction commands to menu items
        soundCheckBox.setAction(soundCommand);
	    return bar;
	}
	//Private method that adds three panels to the GUI frame as well as adding buttons to one of those panels.
    //It also adds action listeners for those buttons.
    private JPanel buttonPanelSetUp()
    {
	    //Creating a button panel that will contain the buttons of the GUI
	    JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new TitledBorder("Commands"));
        buttonPanel.setLayout(new GridLayout(20,1));
        //Creating and adding buttons to the panel.
        JButton gameModeButton = new JButton("Pause");
        buttonPanel.add(gameModeButton);
        JButton addTimeButton = new JButton("Add 30 Seconds");
        buttonPanel.add(addTimeButton);
        JButton subtractTimeButton = new JButton("Subtract 30 Seconds");
        buttonPanel.add(subtractTimeButton);
        JButton addPointsButton = new JButton("Add 100 Points");
        buttonPanel.add(addPointsButton);
        JButton subtractPointsButton = new JButton("Subtract 100 Points");
        buttonPanel.add(subtractPointsButton);
        JButton quitButton = new JButton("Quit");
        buttonPanel.add(quitButton);
	    //Attaching AbstractAction commands to buttons
	    gameModeButton.setAction(gameModeCommand);
	    addTimeButton.setAction(addTimeCommand);
	    subtractTimeButton.setAction(subtractTimeCommand);
	    addPointsButton.setAction(addGameScoreCommand);
	    subtractPointsButton.setAction(subtractGameScoreCommand);
	    quitButton.setAction(quitCommand);
        return buttonPanel;
    }
    //This method is called every time the Timer object "ticks".
	public void actionPerformed(ActionEvent e) 
	{
		//The following methods sets the elapsed time for relevant objects and
		//calls the Game World's "tick" function for every Timer event.
		tickCommand.setTimerCycle(elapsedTime);
    	tickCommand.actionPerformed(e);
	}
}

