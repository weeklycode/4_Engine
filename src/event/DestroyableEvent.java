package event;

public interface DestroyableEvent extends CancellableEvent{

	public void destroyEvent();
	public boolean isDestroyed();

}
