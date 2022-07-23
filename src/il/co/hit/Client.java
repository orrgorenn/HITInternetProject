package il.co.hit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This is the client class.
 * The client class communicates with the server over TCP.
 * The client can choose total of 4 tasks.
 * @author orr_g, or_s, anna_p
 *
 */

public class Client {
	/**
	 * Task's List
	 */
	private static String[] tasks = {
			"Return all indexes of 1's from the matrix.",
			"Find shortest paths from source index to destination index.",
			"Play the battleship game.",
			"Find lightest path from srouce index to destination index on a weighted matrix."
	};
	
	public static String[] getTasks() {
		return tasks;
	}
	
	/**
	 * Print to the console all available tasks
	 */
	private static void displayTasks() {
		System.out.println("Welcome to Internet Programming final project, made by Orr.G, Anna P. and Or.S.");
		System.out.println("Please choose your desired task:");
		for(int i = 0; i < tasks.length; i++) {
			System.out.println("(" + (i + 1) + ") - " + tasks[i]);
		}
	}
	
	private static int[][] taskOneMatrix = {
    		{1, 0, 0},
    		{1, 0, 1},
    		{0, 1, 1},
    };
    
    private static int[][] taskTwoMatrix = {
    		{1, 0, 0},
    		{1, 1, 0},
    		{1, 1, 0},
    };
    
    /**
     * 
     {1, 0, 0, 1, 1}, - 2 BattleShips
	 {1, 0, 0, 1, 1},
	 {1, 0, 0, 1, 1},
	 * 
     {1, 1, 0, 1, 1}, - 3 BattleShips
     {0, 0, 0, 1, 1},
     {1, 1, 0, 1, 1},
     */
    private static int[][] taskThreeMatrix = {
    		{1, 1, 0, 1, 1},
    	    {0, 0, 0, 1, 1},
    	    {1, 1, 0, 1, 1},
    };
    
    private static int[][] taskFourMatrix = {
    		{100, 100, 100},
    		{500, 900, 300}
    };
    
    private static int[][][] allTasks = {
			taskOneMatrix,
			taskTwoMatrix,
			taskThreeMatrix,
			taskFourMatrix,
	};
    
    private static Index getIndexFromInput(Matrix m) {
    	Scanner in = new Scanner(System.in);
        System.out.println("Choose a row in matrix:");
        int row = in.nextInt();
        while(!(row >= 0 && row < m.getMatrix().length)){
            System.out.println("Invalid, the options are: 0 - " + (m.getMatrix().length - 1) + ", Please enter correct value:");
            row = in.nextInt();
        }
        System.out.println("Choose a column in matrix:");
        int col = in.nextInt();
        while(!(col >= 0 && col < m.getMatrix()[0].length)){
        	System.out.println("Invalid, the options are: 0 - " + (m.getMatrix()[0].length - 1) + ", Please enter correct value:");
            col = in.nextInt();
        }
        return new Index(row , col);
    }
    
    private static void stopServer(ObjectOutputStream output, ObjectInputStream input, Socket socket) throws IOException {
    	System.out.println("Stopping Client...");
		output.writeObject("stop-server");
		input.close();
		output.close();
		socket.close();
		System.out.println("Client succesfully stopped.");
    }
    
    private static void sendAssignment(int task, ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
    	System.out.println("The chosen task is: " + tasks[task - 1]);
    	output.writeObject(String.valueOf(task));
		output.writeObject(allTasks[task - 1]);
		Matrix matrix;
		Index startIndex, endIndex;
		
		switch(task) {
			case 1: {
				List<HashSet<Index>> answer = new ArrayList<>((List<HashSet<Index>>) input.readObject()) {};
				System.out.println("Answer: \n" + answer);
				break;
			}
			case 2: {
				matrix = new Matrix(allTasks[task - 1]);
		        System.out.println("Choose starting index (r, c):");
		        startIndex = getIndexFromInput(matrix);
		        System.out.println("Choose destination index (r, c):");
		        endIndex = getIndexFromInput(matrix); 
		        
		        output.writeObject(startIndex);
		        output.writeObject(endIndex);
		        
		        List<List<Node<Index>>> minPaths = new ArrayList<>((List<List<Node<Index>>>) input.readObject());
		        
		        System.out.println("Answer: \n" + minPaths);
		        break;
			}
			case 3: {
				int size = (int) input.readObject();
		        System.out.println("Answer: \n" + size);
		        break;
			}
			case 4: {
				matrix = new Matrix(allTasks[task - 1]);
				System.out.println("Choose starting index (r, c):");
                startIndex = getIndexFromInput(matrix);
                System.out.println("Choose destination index (r, c):");
                endIndex = getIndexFromInput(matrix);
                output.writeObject(startIndex);
                output.writeObject(endIndex);
                
                LinkedList<List<Index>> minWeightList = new LinkedList<>((LinkedList<List<Index>>) input.readObject());
                System.out.println("Answer: \n" + minWeightList);
			}
			default:
				break;
		}
        
		System.out.println("0 - to stop | 9 - show task options | 1 - 4 choose task.");
    }
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, ClassCastException {
		Scanner in = new Scanner(System.in);
		boolean isRunning = false;
		
		Socket socket = new Socket(Globals.IP, Globals.PORT);
		
		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("[Client] Connected to server on port: " + Globals.PORT);
		
		isRunning = true;
		
		displayTasks();
		String userInput = in.next();
		while(isRunning) {
			switch(userInput) {
				case "1": {
					sendAssignment(Integer.parseInt(userInput), output, input);
					userInput = in.next();
					break;
				}
				case "2": {
					sendAssignment(Integer.parseInt(userInput), output, input);
					userInput = in.next();
					break;
				}
				case "3": {
					sendAssignment(Integer.parseInt(userInput), output, input);
					userInput = in.next();
					break;
				}
				case "4": {
					sendAssignment(Integer.parseInt(userInput), output, input);
					userInput = in.next();
					break;
				}
				case "9": {
					displayTasks();
					userInput = in.next();
					break;
				}
				case "0": {
					stopServer(output, input, socket);
					isRunning = false;
					break;
				}
				default: {
					displayTasks();
					userInput = in.next();
				}
			}
		}
		in.close();
	}
}
