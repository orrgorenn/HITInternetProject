package il.co.hit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Bellman Ford Algorithm
 * Slower than Dijkstra's algorithm but more flexible
 * @author orr_g, or_s, anna_p
 *
 * @param <T>
 */
public class BellmanFord<T> {
    final ThreadLocal<Queue<List<Node<T>>>> threadLocalQueue = ThreadLocal.withInitial(() -> new LinkedList<>());

    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * findPaths is a method to find paths from srcNode to destNode
     * @param graph represent a graph
     * @param srcNode represent start index
     * @param destNode
     * @return LinkedList<List < Node < T>>> - all paths between source to destination
     */

    public LinkedList<List<Node<T>>> findPaths(ITraversable<T> graph, Node<T> srcNode, Node<T> destNode) {

        List<Node<T>> path = new ArrayList<>();
        LinkedList<List<Node<T>>> listPaths = new LinkedList<>();
        path.add(srcNode);
        threadLocalQueue.get().offer(path);
        while (!threadLocalQueue.get().isEmpty()) {
            path = threadLocalQueue.get().poll();
            Node<T> last = path.get(path.size() - 1);
            if (last.equals(destNode)) {
                listPaths.add(path);
            }
            // Deal with the last node
            Collection<Node<T>> neighborsIndices = graph.getNeighbors(last);
            for (Node<T> neighbor : neighborsIndices) {
                if (!path.contains(neighbor)) {
                    List<Node<T>> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    threadLocalQueue.get().offer(newPath);
                }

            }
        }
        threadLocalQueue.get().clear();
        return listPaths;
    }

    /**
     * sumPathWeight: This function calculate a weight of a specific path by its nodes.
     *
     * @param graph
     * @param path
     * @return weight of the path inside the graph
     */
    public int sumPathWeight(ITraversable<T> graph, List<Node<T>> path) {
        int weight = 0;
        for (Node<T> node : path) {
            weight = weight + graph.getValueByType(node.getData());
        }
        return weight;
    }

    /**
     * findLightestPaths is a method to find lightest paths between srcNode and destNode
     *
     * @param graph
     * @param srcNode
     * @param destNode
     * @return LinkedList<List<Node<T>>> - lightest paths between srcNode to destNode
     */

    public LinkedList<List<Node<T>>> findLightestPaths(ITraversable<T> graph, Node<T> srcNode, Node<T> destNode) {
        AtomicInteger weightOfPath = new AtomicInteger();
        AtomicInteger currMinWeight = new AtomicInteger();
        AtomicInteger totalMinWeight = new AtomicInteger();

        currMinWeight.set(Integer.MAX_VALUE);

        LinkedList<List<Node<T>>> pathsList = findPaths(graph, srcNode, destNode);
        LinkedList<List<Node<T>>> listMinTotalWeight = new LinkedList<>();

        LinkedList<Future<List<Node<T>>>> futureList = new LinkedList<>();
        LinkedList<List<Node<T>>> listMinTotalWeightFuture = new LinkedList<>();

        for (List<Node<T>> list : pathsList) {
            Callable<List<Node<T>>> callable = () -> {
                readWriteLock.writeLock().lock();
                weightOfPath.set(sumPathWeight(graph, list));
                if (weightOfPath.get() <= currMinWeight.get()) {
                	// Found a lighter path
                    currMinWeight.set(weightOfPath.get());
                    totalMinWeight.set(currMinWeight.get());
                    readWriteLock.writeLock().unlock();
                    if (totalMinWeight.get() < currMinWeight.get())
                        totalMinWeight.set(currMinWeight.get());
                    return list;
                } else {
                    weightOfPath.set(0);
                    readWriteLock.writeLock().unlock();
                    return null;
                }

            };
            Future<List<Node<T>>> futurePath = threadPoolExecutor.submit(callable);
            futureList.add(futurePath);
        }

        for (Future<List<Node<T>>> nodeList : futureList) {
            try {
                if (nodeList.get() != null)
                    listMinTotalWeightFuture.add(nodeList.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        int currentWeight = 0;
        for (List<Node<T>> currentList : listMinTotalWeightFuture) {
            for (Node<T> node : currentList) {
                currentWeight = currentWeight + graph.getValueByType(node.getData());
            }
            if (currentWeight == totalMinWeight.get()) {
                listMinTotalWeight.add(currentList);
            }
            currentWeight = 0;
        }
        
        this.threadPoolExecutor.shutdown();
        return listMinTotalWeight;
    }
}


