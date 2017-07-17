package jewelGame;

//Observer interface with an undefined update method to be used by observer classes.
public interface IObserver
{
 public void update(IObservable o, Object arg);
}
