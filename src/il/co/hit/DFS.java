package il.co.hit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * DFS Algorithm Class
 * @author orr_g, or_s, anna_p
 *
 * @param <T>
 */
public class DFS<T> {

    final ThreadLocal<Stack<Node<T>>> threadLocalStack = ThreadLocal.withInitial(() -> new Stack<Node<T>>());
    final ThreadLocal<Set<Node<T>>> threadLocalSet = ThreadLocal.withInitial(LinkedHashSet::new);

    /**
     * Create a ThreadPool with a pool size of 5
     * A maximum pool size of 10
     * and A keep alive time of 1 second
     */
    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * DFSTraverse method finds SCCs.
     * 
     * @param graph represent current Graph (we relate matrix as graph)
     * @param listOfIndexes - list of indexes their value is 1 (connected components are indexes with value 1).
     * @return HashSet<HashSet<T>> - all the SCCs in the current graph.
     */
    public HashSet<HashSet<T>> DFSTraverse(ITraversable<T> graph, List<Index> listOfIndexes){
        HashSet<Future<HashSet<T>>> futureListOfScc = new HashSet<>();
        HashSet<HashSet<T>> listIndexScc = new HashSet<>();
        int listSize = listOfIndexes.size();
        for(int i = 0; i < listSize; i++) {
            int currentIndex = i;
            Callable<HashSet<T>> MyCallable = () -> {
                readWriteLock.writeLock().lock();
                graph.setStartIndex(listOfIndexes.get(currentIndex));
                HashSet<Index> singleSCC = (HashSet<Index>) this.traverse(graph);
                readWriteLock.writeLock().unlock();
                return (HashSet<T>) singleSCC;

            };
            Future<HashSet<T>> futureHashSCCS = threadPoolExecutor.submit(MyCallable);
            futureListOfScc.add(futureHashSCCS);
        }
        for (Future<HashSet<T>> futureScc : futureListOfScc) {
                try {
                    listIndexScc.add(futureScc.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        this.threadPoolExecutor.shutdown();
        return listIndexScc;
    }

    /**
     * traverse - this function executes Depth First Search algorithm by ThreadLocal
     * and handles a matrix as a graph
     * @param graph
     * @return List<T> connected component.
     */
    public Set<T> traverse(ITraversable<T> graph) {
        threadLocalStack.get().push(graph.getOrigin());
        while (!threadLocalStack.get().isEmpty()) {
            Node<T> popped = threadLocalStack.get().pop();
            threadLocalSet.get().add(popped);
            Collection<Node<T>> reachableNodes = graph.getReachableNodes(popped);
            for (Node<T> singleReachableNode : reachableNodes) {
                if (!threadLocalSet.get().contains(singleReachableNode) && !threadLocalStack.get().contains(singleReachableNode)) {
                    threadLocalStack.get().push(singleReachableNode);
                }
            }
        }
        
        Set<T> connectedComponent = new HashSet<>();
        for (Node<T> node : threadLocalSet.get()) {
        	connectedComponent.add(node.getData());
        }
        threadLocalSet.get().clear();
        return connectedComponent;
    }
    
    /**
     * Method to check squared shapes (containing 1) and assuring the fit with the definition
     * of a battleship given in the task.
     * If we break out of the square shape we decrease count of battleships.
     * @param sccs
     * @param matrix
     * @return number of battleships
     */
    public int battleshipCheck(List<HashSet<Index>> sccs, int[][] matrix) {
        int countBS = sccs.size();
        int minRow = Integer.MAX_VALUE, minCol = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE, maxCol = Integer.MIN_VALUE;
        int flag = 0;
        
        for (HashSet<Index> singleSCC : sccs) {
            if (singleSCC.size() == 1) {
                countBS--;
                continue;
            }
            for (Index index : singleSCC) {
                if (index.getRow() <= minRow)
                    minRow = index.getRow();
                if (index.getColumn() <= minCol)
                    minCol = index.getColumn();
                if (index.getRow() > maxRow)
                    maxRow = index.getRow();
                if (index.getColumn() > maxCol)
                    maxCol = index.getColumn();
            }
            for (int i = minRow; i <= maxRow; i++) {
                for (int j = minCol; j <= maxCol; j++) {
                    if (matrix[i][j] == 0) {
                        flag = 1;
                        break;
                    }
                }
                if(flag == 1) {
                    countBS--;
                    break;
                }
            }
            flag = 0;
            minRow = Integer.MAX_VALUE;
            minCol = Integer.MAX_VALUE;
            maxRow = Integer.MIN_VALUE;
            maxCol = Integer.MIN_VALUE;
        }
        return countBS;
    }

    /**
     * Find SCC's - Find all SCC's in a Matrix (Sorted)
     * @param matrix
     * @return list of SCC's
     */
    public List<HashSet<Index>> findSCCs(int[][] matrix) {
        HashSet<HashSet<Index>> sccs;
        List<Index> listOfNodes;
        Matrix sourceMatrix = new Matrix(matrix);

        TMatrix travMatrix = new TMatrix(sourceMatrix);
        listOfNodes = sourceMatrix.findOnes();

        // travMatrix.setStartIndex(travMatrix.getStartIndex());
        DFS<Index> threadLocalDFSVisit = new DFS<>();
        sccs = threadLocalDFSVisit.DFSTraverse(travMatrix, listOfNodes);
        List<HashSet<Index>> listOfAllSCCS = sccs.stream().sorted(Comparator.comparingInt(HashSet::size)).collect(Collectors.toList());
        return listOfAllSCCS;

    }
}