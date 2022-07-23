package il.co.hit;
import java.util.Collection;

/**
 * This interface defines the functionality required for a traversable graph
 * A graph is traversable if you can draw a path between all the vertices without retracing the same path.
 */
public interface ITraversable<T> {
	public Collection<Node<T>> getReachableNodes(Node<T> node);
	public Collection<Node<T>> getNeighbors(Node<T> node);
	public void setStartIndex(Index idx);
	public void setEndIndex(Index idx);
	public int getSize();
	public int getValue(Node<T> node);
	public int getValueByType(T node);
	public Node<T> getOrigin();
	public Node<T> getDestination();
}
