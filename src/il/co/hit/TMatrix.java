package il.co.hit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is a traversable matrix and implements the ITraversable interface
 * @author orr_g, or_s, anna_p
 *
 */
public class TMatrix implements ITraversable<Index> {
    protected final Matrix matrix;
    protected Index startIndex, endIndex;

    /**
     * Class constructor for a matrix
     * @param matrix
     */
    public TMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Get starting index of matrix
     * @return Index
     */
    public Index getStartIndex() {
        return startIndex;
    }

    /**
     * Set starting index of matrix
     */
    public void setStartIndex(Index startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * Get ending index of matrix
     * @return Index
     */
    public Index getEndIndex() {
        return endIndex;
    }

    /**
     * Set ending index of matrix
     */
    public void setEndIndex(Index endIndex) {
        this.endIndex = endIndex;
    }
    
    /**
     * Overriding a method from the ITraversable interface
     * @return new index
     */
    @Override
    public int getValue(Node<Index> node) {
        return matrix.getValue(new Index(node.getData().getRow(), node.getData().getColumn()));
    }

    /**
     * Get a specific value by type T
     */
    @Override
    public int getValueByType(Index idx) {
        return this.matrix.getValue(idx);
    }

    /**
     * Get the start index of a matrix
     */
    @Override
    public Node<Index> getOrigin() throws NullPointerException {
        if (this.startIndex == null) throw new NullPointerException("Start index is not initialized");
        return new Node<>(this.startIndex);

    }

    /**
     * Get the ending index of a matrix
     */
    @Override
    public Node<Index> getDestination() throws NullPointerException{
        if (this.endIndex == null) throw new NullPointerException("End index is not initialized");
        return new Node<>(this.endIndex);
    }

    /**
     * Overriding a method from ITraversable
     * Get a collection of reachable nodes from a specific node
     * A Reachable node is a node that connected horizontally or vertically and containing 1
     */
    @Override
    public Collection<Node<Index>> getReachableNodes(Node<Index> node) {
        List<Node<Index>> reachableIndices = new ArrayList<>();
        for (Index index : this.matrix.getNeighbors(node.getData())) {
            if (matrix.getValue(index) == 1) {
                Node<Index> indexNode = new Node<>(index, node);
                reachableIndices.add(indexNode);
            }
        }
        return reachableIndices;
    }

    /**
     * Overriding a method from ITraversable
     * Get a collection of neighbor nodes from a specific node
     * A Reachable node is a node that connected horizontally or vertically and containing 1
     */
    @Override
    public Collection<Node<Index>> getNeighbors(Node<Index> node) {
        List<Node<Index>> NeighborIndex = new ArrayList<>();
        for (Index index : this.matrix.getNeighbors(node.getData())) {
            Node<Index> indexNode = new Node<>(index, node);
            NeighborIndex.add(indexNode);
        }
        return NeighborIndex;
    }

    @Override
    public String toString() {
        return matrix.toString();
    }

   public int getSize(){
        return this.getSize();
    }

}
