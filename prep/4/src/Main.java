import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args){
        try {
            ServerSocket server = new ServerSocket(5173);
            while(true) {
                Socket clientSocket = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String input;
                while(!(input = reader.readLine()).equals("Stop")) {
                    writer.println("Echo: " + input);
                }
                writer.println("bye");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try(ServerSocket server = new ServerSocket(8080);
            ExecutorService pool = Executors.newFixedThreadPool(10);
        ){
            while (true){
                Socket socket = server.accept();
                pool.execute(new ServerThread(socket));
                System.out.println("Client connected!");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    }
}