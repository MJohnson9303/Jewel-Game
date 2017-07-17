package jewelGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

//Observer class that observes GameWorld for game state value changes and displays changes.
public class MapView extends JPanel implements IObserver, MouseListener
{
	private Graphics g;
    private IGameWorld gameWorldProxy;
    private GameWorldObject[][] gameWorldCollection;
    private GameWorldObject holdObject;
    //This stores the location of the mouse click and passes it into GameWorld's setGameObjectSelect()
  	private Point mouseLocation = new Point(0,0);
    //Constructor that creates a new mapPanel, sets a black border around it and sets the panel's size
    public MapView()
    {
    	g = this.getGraphics();
        setBorder(new LineBorder(Color.black, 2));
        //Attaching mouse listener to the MapView panel
        addMouseListener(this);
    }
    //This method will handle the drawing of all the objects inside the MapView panel
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	for(int row = 0; row <= gameWorldCollection.length - 1; row++)
    	{
    		for(int col = 0; col <= gameWorldCollection[0].length - 1; col++)
    		{
		       holdObject = (GameWorldObject) gameWorldCollection[row][col];
		       if(holdObject instanceof IDrawable)
		       {
		          IDrawable idobj = (IDrawable)holdObject;
		          if(holdObject instanceof IDeletable)
			       {
			    	   IDeletable ideobj = (IDeletable)holdObject;
			    	   if(ideobj.getDeleteStatus() == true)
			    	   {
			    		   ideobj.deleteDraw(g);
			    		   continue;
			    	   }
			       }
			       if(holdObject instanceof ISelectable)
			       {
			    	   ISelectable isobj = (ISelectable)holdObject;
			    	   if(isobj.getSelectedStatus() == true)
			    	   {
			    		   isobj.selectDraw(g);
			    		   continue;
			    	   } 
			       }
			       idobj.draw(g);
		       }
    		}
	    }
    }
    //Update method that displays current positions of game objects when a change occurs.
    public void update(IObservable o, Object arg)
    {
        gameWorldProxy = (GameWorldProxy)o;
        gameWorldCollection = gameWorldProxy.getGameWorldCollection();
        //Calling the overridden "paintComponent" subroutine.
        this.repaint();
    }
    //This method is called for the clicked mouse event 
  	//which obtains where the mouse was clicked.
  	public void mouseClicked(MouseEvent e) 
  	{
  		mouseLocation = e.getPoint();
  		gameWorldProxy.setGameObjectSelect(mouseLocation);
  		this.repaint();
  		
  	}
  	public void mouseEntered(MouseEvent e) 
  	{
  		// TODO Auto-generated method stub
  		
  	}
  	public void mouseExited(MouseEvent e) 
  	{
  		// TODO Auto-generated method stub
  		
  	}
  	public void mousePressed(MouseEvent e) 
  	{
  		// TODO Auto-generated method stub
  		
  	}
  	public void mouseReleased(MouseEvent e) 
  	{
  		// TODO Auto-generated method stub
  		
  	}
}

