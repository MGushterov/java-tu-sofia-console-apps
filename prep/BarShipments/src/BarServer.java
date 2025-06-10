/*
* Mihael Gushterov
* 381223064
* 92
*/

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Beverage {
    protected String id;
    private String name;
    private String type;
    private int quantity;

    //Logic for getters/setters + constructor
    //Ne prepisvai nadolu

    public Beverage(String id, String name, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

enum Status {
    SUBMITTED("Shipment request submitted"),
    PROCESSING("Request is processing"),
    PROCESSED("Request processed"),
    FINISHED("Request has finished successfully");

    private String message;

    Status(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}

class Order {
    private String orderNumber;
    private String beverageName;
    private int beverageAmount;
    private Status status;

    // Logic for getters/setters + constructor
    // Ne prepisvai nadolu


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBeverageName() {
        return beverageName;
    }

    public void setBeverageName(String beverageName) {
        this.beverageName = beverageName;
    }

    public int getBeverageAmount() {
        return beverageAmount;
    }

    public void setBeverageAmount(int beverageAmount) {
        this.beverageAmount = beverageAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

public class BarServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5678);
            ExecutorService pool = Executors.newFixedThreadPool(10)) {

            while (true) {
                Socket client = server.accept();
                pool.execute(new ClientHandler(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static List<Beverage> beverages = new ArrayList<>();
    protected static List<Order> orders = new ArrayList<>();
}

class ClientHandler implements Runnable {
    private final Socket client;

    public ClientHandler(Socket clientSocket) {
        this.client = clientSocket;
    }

    // zadacha 1
    public List<Beverage> getAllAvailable() {
        return BarServer.beverages;
    }

    public void printGetAllAvailable(PrintWriter out) {
        List<Beverage> bevs = getAllAvailable();
        for (Beverage bev : bevs) {
            if (bev.getQuantity() == 0) {
                out.printf("%s %s %s not available", bev.getId(), bev.getName(), bev.getType());
            }
            else {
                out.printf("%s %s %s %d", bev.getId(), bev.getName(), bev.getType(), bev.getQuantity());
            }
        }
    }

    // zadacha 2
    public boolean placeOrder(Scanner in) {
        String id = in.nextLine();
        int amount = in.nextInt(); //tova stava da go napishesh na izpita, nikoi da ti napravi problem (veroqtno)
        //int amount = Integer.parseInt(in.nextLine());  tuk e po-pravilno
        String bevName;

        boolean bevWithIdFound = false;
        for(Beverage bev : BarServer.beverages) {
            if(bev.getId().equals(id)) {
                bevWithIdFound = true;
                bevName = bev.getName();
                break;
            }
        }

        //ne zapulvam constructora
        Order newOrder = new Order();
        BarServer.orders.add(newOrder);

        return bevWithIdFound;
    }

    public void printPlaceOrder(Scanner in, PrintWriter out) {
        boolean result = placeOrder(in);
        if(result) {
            out.println("Successfully placed order");
        }
        else {
            out.println("Beverage was not found by ID");
        }
    }

    // zadacha 3
    public List<Order> getAllOrders() {
        return BarServer.orders;
    }

    public void printGetAllOrders(PrintWriter out) {
        List<Order> orders = this.getAllOrders();
        for (Order order : orders) {
            out.printf("%s %s %d %s", order.getOrderNumber(), order.getBeverageName(), order.getBeverageAmount(), order.getStatus());
        }
    }

    // zadacha 4
    public void fillBar() {
        for (Order order : BarServer.orders) {
            if (order.getStatus().equals(Status.PROCESSED)) {
                for (Beverage bev : BarServer.beverages) {
                    if(bev.getName().equals(order.getBeverageName())) {
                        bev.setQuantity(order.getBeverageAmount());
                        order.setStatus(Status.FINISHED);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(this.client.getInputStream());
             PrintWriter out = new PrintWriter(this.client.getOutputStream())) {

            while (this.client.isConnected()) {
                String command = in.nextLine();

                switch(command) {
                    case "1":
                        printGetAllAvailable(out);
                        break;
                    case "2":
                        printPlaceOrder(in, out);
                        break;
                    case "3":
                        printGetAllOrders(out);
                        break;
                    case "4":
                        fillBar();
                        break;
                    case "5":
                        this.client.close();
                        break;
                    default:
                        out.println("Enter valid command");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}