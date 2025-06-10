import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Movie {
    private int id;
    private String title;
    private String director;
    private int availableSeats;

    // Logic for constructor + getters / setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}

enum Status {
    RESERVATION_SUCCESSFUL("Successfully made reservation"),
    RESERVATION_UNSUCCESSFUL("Movie not fount or was not able to reserve seats");

    private String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

public class CinemaServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(6789);
             ExecutorService pool = Executors.newFixedThreadPool(10)) {

            while(true) {
                Socket client = server.accept();
                pool.execute(new ClientHandler(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static Map<String, List<Movie>> cinemas = new HashMap<>();
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    // Zadacha 1

    private Map<String, Movie> findMovieByName(String title) {
        Map<String, Movie> moviesFound = new HashMap<>();

        for (Map.Entry<String, List<Movie>> cinemas : CinemaServer.cinemas.entrySet()) {
            String cinema = cinemas.getKey();
            List<Movie> moviesInCinema = cinemas.getValue();

            for (Movie movie : moviesInCinema) {
                if(movie.getTitle().equals(title)) {
                    moviesFound.put(cinema, movie);
                }
            }
        }

        return moviesFound;
    }

    public void printFindMovieByName(Scanner in,  PrintWriter out) {
        String title = in.nextLine();
        Map<String, Movie> moviesFound = findMovieByName(title);

        if(moviesFound.isEmpty()) {
            out.printf("No movies matching the title '%s' were found", title);
        }
        else {
            for (Movie movie : moviesFound.values()) {
                out.printf("Id: %d, Title: %s, Seats Available: %d", movie.getId(), movie.getTitle(), movie.getAvailableSeats());
            }
        }
    }

    // Zadacha 2

    private boolean reserveMovieById(int id) {
        boolean resWasMade = false;
//        for (Map.Entry<String, List<Movie>> cinemaMoviesListPair : CinemaServer.cinemas.entrySet()) {
//            String cinema = cinemaMoviesListPair.getKey();
//            List<Movie> movies = cinemaMoviesListPair.getValue();
//
//            for (Movie movie : movies) {
//                if(movie.getId() == id) {
//                    if(movie.getAvailableSeats() > 0) {
//                        resWasMade = true;
//                        movie.setAvailableSeats(movie.getAvailableSeats() - 1);
//                    }
//                }
//            }
//        }

        for (List<Movie> movies : CinemaServer.cinemas.values()) {
            for(Movie movie : movies) {
                if(movie.getId() == id) {
                    if(movie.getAvailableSeats() > 0) {
                        resWasMade = true;
                        movie.setAvailableSeats(movie.getAvailableSeats() - 1);
                    }
                }
            }
        }

        return resWasMade;
    }

    public void printReserveMovieById(Scanner in, PrintWriter out) {
        int id = Integer.parseInt(in.nextLine());
        // int id = in.nextInt();
        // in.nextLine();
        boolean result = reserveMovieById(id);
        if (result) {
            out.printf("Status:  %s", Status.RESERVATION_SUCCESSFUL.getMessage());
        }
        else {
            out.printf("Status, %s", Status.RESERVATION_UNSUCCESSFUL.getMessage());
        }
    }

    // Zadacha 3

    public void printAllAvailable(PrintWriter out) {
        for(Map.Entry<String, List<Movie>> cinemaMoviesPair : CinemaServer.cinemas.entrySet()) {
            String cinema = cinemaMoviesPair.getKey();
            List<Movie> moviesInCinema = cinemaMoviesPair.getValue();

            out.printf("Cinema: %s; Movies available: /n", cinema);

            for(Movie movie : moviesInCinema) {
                out.printf("Title: '%s' /nDirector: %s/nAvailable seats: %d", movie.getTitle(), movie.getDirector(), movie.getAvailableSeats());
            }
        }
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(this.clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream())) {

            while(clientSocket.isConnected()) {
                String command = in.nextLine();
                switch (command) {
                    case "1":
                        this.printFindMovieByName(in, out);
                        break;
                    case "2":
                        this.printReserveMovieById(in, out);
                        break;
                    case "3":
                        this.printAllAvailable(out);
                        break;
                    case "4":
                        clientSocket.isClosed();
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}