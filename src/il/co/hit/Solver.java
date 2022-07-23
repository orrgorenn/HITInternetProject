package il.co.hit;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Solver implements IHandler {
	private Matrix matrix;
    
    private void finishTask() {
        this.matrix = null;
    }

	@Override
	public void handle(InputStream _input, OutputStream _output) throws IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(_input);
        ObjectOutputStream output = new ObjectOutputStream(_output);
        
        this.finishTask();
        
        boolean isRunning = true;
        while(isRunning) {
        	switch(input.readObject().toString()) {
	        	case "1": {
	        		System.out.println("Task 1 - " + Client.getTasks()[0]);
	                int[][] m = (int[][]) input.readObject();
	                // TODO: add message
	                this.matrix = new Matrix(m);
	                List<Index> ones = matrix.findOnes();
	                output.writeObject(ones);
	                System.out.println("[Server] Task 1 finished.");
	                break;
	            }
	        	case "2": {
	        		Index src, dest;
	        		int[][] m = (int[][]) input.readObject();
                    System.out.println("Task 2 - " + Client.getTasks()[1]);
                    this.matrix = new Matrix(m);
                    src = (Index) input.readObject();
                    System.out.println("[Server] Received Source.");
                    dest = (Index) input.readObject();
                    System.out.println("[Server] Received Destination.");
                    TMatrix travMatrix = new TMatrix(this.matrix);
                    travMatrix.setStartIndex(src);
                    travMatrix.setEndIndex(dest);
                    BFS<Index> bfs = new BFS<Index>();
                    List<List<Node<Index>>> minPaths;
                    minPaths = bfs.findShortestPathsParallelBFS(travMatrix, travMatrix.getOrigin(), travMatrix.getDestination());
                    output.writeObject(minPaths);
                    System.out.println("[Server] Task 2 finished.");
	        	}
	        	case "3": {
	        		int[][] matrix = (int[][]) input.readObject();
	        		System.out.println("Task 3 - " + Client.getTasks()[2]);
                    List<HashSet<Index>> listOfHashsets;
                    DFS<Index> dfs = new DFS<>();
                    listOfHashsets = dfs.findSCCs(matrix);
                    int size = dfs.battleshipCheck(listOfHashsets, matrix);
                    output.writeObject(size);
                    System.out.println("[Server] Task 3 finished.");
	        	}
	        	case "4": {
	        		Index src, dest;
	        		int[][] matrix = (int[][]) input.readObject();
	        		System.out.println("Task 4 - " + Client.getTasks()[3]);
                    this.matrix = new Matrix(matrix);
                    src = (Index) input.readObject();
                    System.out.println("[Server] Received Source.");
                    dest = (Index) input.readObject();
                    System.out.println("[Server] Received Destination.");
                    TMatrix travMatrix = new TMatrix(this.matrix);
                    travMatrix.setStartIndex(src);
                    travMatrix.setEndIndex(dest);
                    BellmanFord<Index> bellmanFord = new BellmanFord<Index>();
                    LinkedList<List<Node<Index>>> minWeightList;
                    minWeightList = bellmanFord.findLightestPaths(travMatrix, travMatrix.getOrigin(), travMatrix.getDestination());
                    output.writeObject(minWeightList);
                    System.out.println("[Server] Task 4 finished.");
	        	}
	        	default: {
	        		output.writeObject("Wrong option.");
	        		break;
	        	}
        	}
        }
	}

}
