package il.co.hit;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;

public class Solver implements IHandler {
	private Matrix matrix;
    private Index startIndex, endIndex;
    private volatile boolean work = true;
    
    private void finishTask() {
        this.matrix = null;
        this.startIndex = null;
        this.endIndex = null;
        this.work = true;
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
                    ParallelBFS<Index> parallelBFS = new ParallelBFS<Index>();
                    List<List<Node<Index>>> minPaths;
                    minPaths = parallelBFS.findShortestPathsParallelBFS(travMatrix, travMatrix.getOrigin(), travMatrix.getDestination());
                    output.writeObject(minPaths);
                    System.out.println("[Server] Task 2 finished.");
	        	}
	        	case "3": {
	        		int[][] matrix = (int[][]) input.readObject();
	        		System.out.println("Task 3 - " + Client.getTasks()[2]);
                    List<HashSet<Index>> listOfHashsets;
                    ParallelDFS<Index> parallelDFS = new ParallelDFS<>();
                    listOfHashsets = parallelDFS.findSCCs(matrix);
                    int size = parallelDFS.battleshipCheck(listOfHashsets, matrix);
                    output.writeObject(size);
                    System.out.println("[Server] Task 3 finished.");
	        	}
	        	default: {
	        		output.writeObject("Wrong option.");
	        		break;
	        	}
        	}
        }
	}

}
