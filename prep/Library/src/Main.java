import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5678)){
            ExecutorService threadPool = Executors.newFixedThreadPool(10);

            while(true) {
                Socket clientSocket = server.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            int command;
            while ((command = reader.read()) != -1) {
                if(command == 1) {
                    ArrayList<Library> libraries = findAvailableBookInLibraries();
                }
                else if(command == 2) {
                    ArrayList<Book> reserved;
                    reserved.add()
                }
                else if(command == 3) {

                }
                else {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Library> findAvailableBookInLibraries(ArrayList<Book> books, String title) {
        for (Book book : books) {
            if (book.title.equals(title)) {
                if (book.availability) {
                    return book.libraries;
                }
            }
        }
        System.out.println("Error. Invalid book title");
    }

    private static Book rentBook(ArrayList<Book> books, int id) {
        for (Book book : books) {
            if (book.id == id) {
                if(!book.availability) {
                    System.out.println("Error. Book is already reserved");
                }
                else {
                    book.availability = false;
                    return book;
                }
            }
        }
    }
}

class Book {
    protected int id;
    protected String ISBN;
    protected String title;
    protected String author;
    protected boolean availability;
    protected ArrayList<Library> libraries;
}

class Library {
    protected String name;

    protected ArrayList<Book> books;
}