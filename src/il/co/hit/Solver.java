package il.co.hit;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
	        		Index source, destination;
	        		int[][] m = (int[][]) input.readObject();
                    System.out.println("Task 2 - " + Client.getTasks()[1]);
                    this.matrix = new Matrix(m);
                    source = (Index) input.readObject();
                    System.out.println("[Server] Received Source.");
                    destination = (Index) input.readObject();
                    System.out.println("[Server] Received Destination.");
                    TraversableMatrix traversable22 = new TraversableMatrix(this.matrix);
                    traversable22.setStartIndex(src);
                    traversable22.setEndIndex(dest);
                    ParallelBFS parallelBFS = new ParallelBFS();
                    List<List<Index>> minPaths;
                    minPaths = parallelBFS.findShortestPathsParallelBFS(traversable22,traversable22.getOrigin(),traversable22.getDestination());
                    objectOutputStream.writeObject(minPaths);
                    System.out.println("Task 2.2 finished\n");
	        	}
	        	default: {
	        		output.writeObject("Wrong option.");
	        		break;
	        	}
        	}
        }
	}

}
