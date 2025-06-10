import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Product {
    private int id;
    private String name;
    private double price;
    private String store;

    public Product(int id, String name, double price, String store) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

public class ShopServer {

    private static final int PORT = 4321;

    private Map<String, List<Product>> shops = new HashMap<>();
    private Map<String, List<Product>> shoppingCart = new HashMap<>();

    private Map<String, List<Product>> getShops() {
        return shops;
    }

    public static void main(String[] args) {
        ShopServer shop = new ShopServer();
        try (ServerSocket server = new ServerSocket(PORT);
            ExecutorService pool = Executors.newFixedThreadPool(10)) {

            while(true) {
                Socket clientSocket = server.accept();
                pool.execute(new Thread(new ShopThread(shop, clientSocket)));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Double>> findProductInShops(String name) {
        List<Map<String, Double>> shopsWithProductAvailable = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : this.shops.entrySet()) {
            String shopName = entry.getKey();
            List<Product> products = entry.getValue();

            for (Product product : products) {
                if (product.getName().equals(name)) {
                    Map<String, Double> storePriceKvp = new HashMap<>();
                    storePriceKvp.put(shopName, product.getPrice());
                    shopsWithProductAvailable.add(storePriceKvp);
                }
            }
        }
        return shopsWithProductAvailable;
    }

    public synchronized boolean addProductToCart(int id) {
        for (Map.Entry<String, List<Product>> entry : this.shoppingCart.entrySet()) {
            String shop = entry.getKey();
            List<Product> products = entry.getValue();

            for (Product product : products) {
                if (product.getId() == id) {
                    if (!products.contains(product)) {
                        products.add(product);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Map<String, List<Product>> getShoppingCart() {
        return this.shoppingCart;
    }
}

class ShopThread implements Runnable {
    private final ShopServer shop;
    private final Socket clientSocket;

    public ShopThread(ShopServer shop, Socket client) {
        this.shop = shop;
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(this.clientSocket.getInputStream());
            PrintWriter writer = new PrintWriter(this.clientSocket.getOutputStream(), true)) {

            while (clientSocket.isConnected()) {
                String command = scanner.nextLine();
                switch(command) {
                    case "1":
                        handleFindProductInShops(scanner, writer);
                        break;
                    case "2":
                        handleAddProductToCart(scanner, writer);
                        break;
                    case "3":
                        handleGetShoppingCart(writer);
                        break;
                    default:
                        System.out.println("Enter a valid command");
                        break;
                }
            }

            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFindProductInShops(Scanner in, PrintWriter out) {
        System.out.print("Enter product name: ");
        String name = in.nextLine();
        System.out.println();
        List<Map<String, Double>> listOfShops = this.shop.findProductInShops(name);

        if (listOfShops.isEmpty()) {
            System.out.println("No such product was found");
            return;
        }

        for (Map<String, Double> kvp : listOfShops) {
            for (String shop : kvp.keySet()) {
                Double price = kvp.get(shop);
                out.printf("Product was found at: %s at the price of %.2f\n", shop, price);
            }
        }
    }

    private void handleAddProductToCart(Scanner in, PrintWriter out) {
        System.out.print("Enter product id: ");
        int id = Integer.parseInt(in.nextLine());
        System.out.println();

        boolean isAdded = this.shop.addProductToCart(id);
        if(isAdded) {
            out.println("Successfully added product to shopping cart");
        }
        else {
            out.println("Grocery already in shopping cart");
        }
    }

    private void handleGetShoppingCart(PrintWriter out) {
        Map<String, List<Product>> shoppingCart = this.shop.getShoppingCart();
        for(Map.Entry<String, List<Product>> entry : shoppingCart.entrySet()) {
            String shopName = entry.getKey();
            List<Product> products = entry.getValue();

            out.printf("Products in shop: %s\n", shopName);
            for (Product product : products) {
                out.printf("\t%s - $%.2f\n", product.getName(), product.getPrice());
            }
        }
    }
}

class ShopClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change if server is on a different machine
    private static final int SERVER_PORT = 4321;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             Scanner serverIn = new Scanner(socket.getInputStream());
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
             Scanner userInput = new Scanner(System.in)) {

            System.out.println("Connected to ShopServer.");

            // Start a separate thread to listen to server messages
            Thread listener = new Thread(() -> {
                while (serverIn.hasNextLine()) {
                    String serverMessage = serverIn.nextLine();
                    System.out.println(serverMessage);
                    // If server is prompting for input, prompt the user
                    if (serverMessage.endsWith("Enter command: ") ||
                            serverMessage.endsWith("Enter product name: ") ||
                            serverMessage.endsWith("Enter product id: ")) {
                        System.out.print(""); // Just to prompt the user input
                    }
                }
            });
            listener.start();

            while (socket.isConnected()) {
                if (serverIn.hasNextLine()) {
                    String serverMessage = serverIn.nextLine();
                    System.out.println(serverMessage);

                    if (serverMessage.endsWith("Enter command: ") ||
                            serverMessage.endsWith("Enter product name: ") ||
                            serverMessage.endsWith("Enter product id: ")) {

                        System.out.print("> "); // Prompt symbol
                        String input = userInput.nextLine();
                        serverOut.println(input);
                    }
                } else {
                    break; // Server closed connection
                }
            }

            System.out.println("Disconnected from ShopServer.");

        } catch (IOException e) {
            System.err.println("Unable to connect to the server. Please ensure the server is running.");
            e.printStackTrace();
        }
    }
}