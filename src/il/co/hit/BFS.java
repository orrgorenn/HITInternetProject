package il.co.hit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * BFS Algorithm
 * @author orr_g, or_s, anna_p
 *
 * @param <T>
 */
public class BFS<T> {
    final ThreadLocal<LinkedList<List<Node<T>>>> threadLocalQueue = ThreadLocal.withInitial(() -> new LinkedList<List<Node<T>>>());

    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * findAllPaths method finds paths from srcNode to destNode by ThreadLocal
     * @param graph is a graph
     * @param srcNode is a starting node
     * @param destNode is a destination node
     * @return List<List<Node<T>>> - All paths from srcNode to destNode
     */
    public List<List<Node<T>>> findAllPaths (ITraversable<T> graph, Node<T> srcNode, Node<T> destNode) {
        ArrayList<Node<T>> path = new ArrayList<>();
        List<List<Node<T>>> allPaths = new ArrayList<>();
        path.add(srcNode);
        threadLocalQueue.get().add(path);
        while(!threadLocalQueue.get().isEmpty()) {
            path = (ArrayList<Node<T>>) threadLocalQueue.get().poll();
            Node<T> polled = path.get(path.size() - 1);
            if(polled.equals(destNode))
                allPaths.add(path);
            Collection<Node<T>> reachableNodes = graph.getReachableNodes(polled);
            for (Node<T> singleReachableNode : reachableNodes) {
                if (!path.contains(singleReachableNode)) {
                    ArrayList<Node<T>> newPath = new ArrayList<>(path);
                    newPath.add(singleReachableNode);
                    threadLocalQueue.get().add(newPath);
                }
            }
        }
        threadLocalQueue.get().clear();
        return allPaths;
    }
    
    /**
     * The method finds the shortest paths in a parallel way
     *
     * @param graph represent a graph
     * @param srcNode represent start index
     * @param destNode represent final/ destination index
     * @return List<List<Node<T>>> - All of the shortest paths from srcNode to destNode
     */
    public List<List<Node<T>>> findShortestPathsParallelBFS(ITraversable<T> graph, Node<T> srcNode, Node<T> destNode) {
        AtomicInteger sizeOfMinPath = new AtomicInteger();
        AtomicInteger sizeOfPath = new AtomicInteger();
        sizeOfMinPath.set(Integer.MAX_VALUE);
        List<Future<List<Node<T>>>> futureList = new ArrayList<>();
        List<List<Node<T>>> allPaths = findAllPaths(graph, srcNode, destNode);
        List<List<Node<T>>> minPaths = new ArrayList<>();
        for (List<Node<T>> list: allPaths) {
            Callable<List<Node<T>>> callable = () -> {
                readWriteLock.writeLock().lock();
                sizeOfPath.set(list.size());
                if(sizeOfPath.get() <= sizeOfMinPath.get()) {
                    sizeOfMinPath.set(sizeOfPath.get());
                    readWriteLock.writeLock().unlock();
                    return list;
                }
                else {
                    readWriteLock.writeLock().unlock();
                    return null;
                }
            };
            Future<List<Node<T>>> futurePath = threadPoolExecutor.submit(callable);
            futureList.add(futurePath);
        }
        for (Future<List<Node<T>>> futurePath : futureList) {
            try {
                if (futurePath.get() != null)
                    minPaths.add(futurePath.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.threadPoolExecutor.shutdown();
        if (minPaths.isEmpty()) {
            System.err.println("No path exist between the source " + srcNode + " and the destination " + destNode);
            return null;
        }
        return minPaths;
    }
}
