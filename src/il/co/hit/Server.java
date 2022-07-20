package il.co.hit;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
	// Volatile - communicate with runtime and processor to not reorder any instruction
	// Should flush any updates to these variables right away.
	private volatile boolean stopServer;
	
	// ThreadPool managing all of out client - server comms.
    private ThreadPoolExecutor threadPool;
    
    // Interface to handle each client
    private IHandler requestHandler;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // Initialize server
    public Server() {
        this.threadPool = null;
        stopServer = false;
        requestHandler = null;
    }
    
    // Stop server function - shutdown threadpool, announce on server closure.
    public void stop() {
    	System.out.println("[Server] Server is shutting down...");
        if(!stopServer){
            try {
	            readWriteLock.writeLock().lock();
	            if(!stopServer){
	                if(threadPool != null)
	                    threadPool.shutdown();
	            }
            }
            finally {
                stopServer = true;
                readWriteLock.writeLock().unlock();
                System.out.println("[Server] Server is down.");
            }
        }
    }
    
    // Serving clients functionality - handle all tasks and comm. with the client
    public void serveClients(IHandler handler) {
        this.requestHandler = handler;
        new Thread(() -> {
            this.threadPool = new ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

            try {
                ServerSocket serverSocket = new ServerSocket(Globals.PORT);
                System.out.println("[Server]: Server started.");
                while(!stopServer){
                    Socket conn = serverSocket.accept();
                    Runnable clientHandling = ()-> {
                        System.out.println("[Server]: Client connected.");
                        try {
                            requestHandler.handle(conn.getInputStream(), conn.getOutputStream());
                            conn.getInputStream().close();
                            conn.getOutputStream().close();
                            conn.close();
                        } catch (EOFException e) {
                            System.out.print("[Server]: client disconnected.");
                        } catch(IOException e) {
                        	System.err.print("[Server]: error in handle: " + e);
                        } catch (ClassNotFoundException e) {
                        	System.err.print("[Server]: error in handle: " + e);
                        }
                    };
                    threadPool.execute(clientHandling);
                }
                serverSocket.close();
            } catch (IOException e) {
            	System.err.print("[Server]: error in socket: " + e);
            }

        }).start();
    }
    
    // Main function to run server and serve clients
    public static void main(String[] args) {
        Server server = new Server();
        server.serveClients(new Solver());
    }
}
