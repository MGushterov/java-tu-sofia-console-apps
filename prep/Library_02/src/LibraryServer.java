import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

class Book {
    protected int id;
    protected String isbn;
    protected String title;
    protected String author;
    protected boolean availability;
    protected String library;
}

public class LibraryServer {
    private static final int PORT = 5678;
    protected Map<String, List<Book>> libraries = new HashMap<>();

    public static void main(String[] args) {
        LibraryServer libraryServer = new LibraryServer();
        try (ServerSocket server = new ServerSocket(PORT);
            ExecutorService pool = Executors.newFixedThreadPool(10)) {

            while (true) {
                Socket clientSocket = server.accept();
                pool.execute(new LibraryThread(libraryServer, clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class LibraryThread implements Runnable {
    private final String clientId = String.valueOf(UUID.randomUUID());
    private LibraryServer libraryServer;
    private Socket client;
    private List<Book> rentedBooks = new ArrayList<>();

    public LibraryThread(LibraryServer libraryServer, Socket socket) {
        this.libraryServer = libraryServer;
        this.client = socket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(client.getInputStream());
            PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            while(client.isConnected()) {
                String command = in.nextLine();
                switch (command) {
                    case "1":
                        this.handleFindBookByName(in, out);
                        break;
                    case "2":
                        this.handleRentBook(in, out);
                        break;
                    case "3":
                        this.handleFindAllAvailableInLibrary(in, out);
                        break;
                    case "4":
                        client.close();
                        break;
                    default:
                        out.println("Enter a valid number");
                        break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Zadacha 1

    public List<String> findBookByName(String title) {
        List<String> availableLibraries = new ArrayList<>();
        for (Map.Entry<String, List<Book>> entry : this.libraryServer.libraries.entrySet()) {
            String libraryName = entry.getKey();
            List<Book> books = entry.getValue();

            for (Book book : books) {
                if (book.title.equals(title) && book.availability) {
                    availableLibraries.add(libraryName);
                }
            }
        }

        return availableLibraries;
    }

    // Zadacha 2

    public synchronized boolean rentBook(int bookId) {
        for (List<Book> bookList : this.libraryServer.libraries.values()) {
            for (Book book : bookList) {
                if (book.id == bookId) {
                    rentedBooks.add(book);
                    return true;
                }
            }
        }
        return false;
    }

    // Zadacha 3

    public List<Book> findAllAvailableInLibrary(String libName) {
        List<Book> availableBooks = new ArrayList<>();
        if (libraryServer.libraries.containsKey(libName)) {
            availableBooks = libraryServer.libraries.get(libName);
        }
        return availableBooks;
    }

    // Zadacha 1 Handler

    public void handleFindBookByName(Scanner in, PrintWriter out) {
        out.print("Enter book name: ");
        String name = in.nextLine();
        out.println();

        List<String> libraries = this.findBookByName(name);

        if(libraries.isEmpty()) {
            out.println("Book does not exist");
        }

        for (String lib : libraries) {
            out.printf("%s ", lib);
        }
    }

    // Zadacha 2 Handler

    public void handleRentBook(Scanner in, PrintWriter out) {
        out.print("Enter book id: ");
        int bookId = Integer.parseInt(in.nextLine());
        out.println();

        boolean isAdded = this.rentBook(bookId);
        if(isAdded) {
            out.println("Book was successfully added to rental list");
        }
        else {
            out.println("Book already in rental list");
        }
    }

    // Zadacha 3 Handler

    public void handleFindAllAvailableInLibrary(Scanner in, PrintWriter out) {
        out.print("Enter library name: ");
        String name = in.nextLine();
        out.println();

        List<Book> books = findAllAvailableInLibrary(name);

        if(books.isEmpty()) {
            out.println("Library does not exist or there are no available books");
            return;
        }

        for (Book book : books) {
            out.printf("Book title: %s; Book author %s", book.title, book.author);
        }
    }
}