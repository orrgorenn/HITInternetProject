package il.co.hit;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class wraps a concrete object and supplies getters and setters
 * @param <T>
 */

/**
 * The interface is serializable. It doesn't force us to implement any specific method.
 */
public class Node<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private T data;
    private Node<T> prev;

    /**
     * Class constructor to create a node with a link to a previous node
     * @param nodeData
     * @param nodePrev
     */
    public Node(T nodeData, final Node<T> nodePrev){
        this.data = nodeData;
        this.prev = nodePrev;
    }
    
    /**
     * Class constructor for a node without a link to previous node
     * This constructor uses a previously made constructor
     * @param nodeData
     */
    public Node(T nodeData){
        this(nodeData, null);
    }
    
    /**
     * Class constructor for an empty node
     */
    public Node() {
        this(null);
    }
    
    
    /**
     * Get current node data
     * @return node data
     */
    public T getData() {
        return this.data;
    }

    /**
     * set data to a node
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Return previous node from current node
     * @return
     */
    public Node<T> getPrevNode() {
        return this.prev;
    }

    /**
     * Set previous node for a current node
     * @param prevNode
     */
    public void setParent(Node<T> prevNode) {
        this.prev = prevNode;
    }

    /**
     * This is used when accessing objects multiple times with comparisons.
     * Equals objects have the same hashcode.
     * This method returns the hashcode of the current object, which is equal to the primitive int value.
     * @return
     */
    @Override
    public int hashCode() {
        return data != null ? data.hashCode():0;
    }

    /**
     * The function compares between objects (between all their data members) and returns boolean value
     * @param o Object represent the object we compare to
     * @return boolean answer
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> state1 = (Node<?>) o;
        return Objects.equals(data, state1.data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
