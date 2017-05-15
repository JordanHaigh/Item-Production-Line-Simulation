/**
 * Created by Jordan on 15-May-17.
 */
public interface IObservable
{
    void attach(IObserver observer);
    void notifyAllObservers();
}
