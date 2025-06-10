import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.PrintWriter;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            ExecutorService threadPool = Executors.newFixedThreadPool(3);

            while(true) {
                Socket clientSocket = server.accept();
                threadPool.execute(new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Account {
    private double balance;
    protected String pin;
    protected String accountNumber;

    public Account(double balance, String pin, String accountNumber) {
        this.balance = balance;
        this.pin = pin;
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
        System.out.println("Successfully deposited to balance. New balance: " + this.balance);
    }

    public void withdraw(double amount) {
        if(amount > this.balance) {
            System.out.println("Error. Insufficient balance");
        }
        else {
            this.balance -= amount;
            System.out.println("Successfully withdrew from balance. New balance: " + this.balance);
        }
    }

    public boolean checkPin(String pin) {
        if(pin.equals(this.pin)) {
            return true;
        }
        return false;
    }
}

class ServerThread implements Runnable {
    private ArrayList<Account> accounts;
    private HashMap<Data, Object> sessionData;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ServerThread(Socket socket) {
        accounts = new ArrayList<Account>() {{
            add(new Account(100, "1234", "123456789"));
            add(new Account(1000, "4321", "987654321"));
            add(new Account(15347.31, "1111", "111111111"));
        }};

        sessionData = new HashMap<Data, Object>();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendMessage(Commands command, String message) {
        System.out.printf("[%s]: [%s]", command, message);
        this.writer.printf("%s: %s\n", command, message);
    }

    public void SendStatus(Commands command, Status status) {
        this.writer.printf("%s: %s\n", command, status);
    }

    public void GetMessage() {
        try {
            String wholeMessage = this.reader.readLine();
            String[] data = wholeMessage.split(": ");
            Commands command = Commands.valueOf(data[0]);
            String message = data[1];

            ProcessMessage(command, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ProcessMessage(Commands command, String args) {
        switch (command) {
            case WELCOME:
                SendStatus(Commands.WELCOME, Status.OK);
                break;
            case ACCOUNT_NUMBER:
                // Checking if the account number exists
                Status message = accounts
                        .stream()
                        .anyMatch(account -> account.accountNumber.equals(args)) ? Status.OK : Status.ERROR;
                // Saving the account number in the session data
                if (message == Status.OK) sessionData.put(Data.ACCOUNT_NUMBER, args);
                // Sending the status to the client
                SendStatus(Commands.ACCOUNT_NUMBER, message);
                break;
            case PIN:
                // Getting the account number from the session data
                var accountNumber = sessionData.get(Data.ACCOUNT_NUMBER);

                // Getting the account from the list of accounts with the account number
                var account = accounts.stream().filter(acc -> acc.accountNumber.equals(accountNumber)).findFirst().get();
                // Checking if the pin is correct
                message = account.checkPin(args) ? Status.OK : Status.ERROR;
                // Saving the account in the session data
                if (message == Status.OK) sessionData.put(Data.ACCOUNT, account);
                // Sending the status to the client
                SendStatus(Commands.PIN, message);
                break;
            case WITHDRAW:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Withdrawing the amount
                    account.withdraw(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, Status.OK);
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, Status.ERROR);
                    break;
                }
                break;
            case DEPOSIT:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Depositing the amount
                    account.deposit(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, Status.OK);
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, Status.ERROR);
                    break;
                }
                break;
            case GET_BALANCE:
                // SendStatus(Commands.GET_BALANCE, Status.OK);
                break;
            default:
                SendStatus(Commands.ERROR, Status.ERROR);
                break;
        }
    }

    public void ServerLogic() {
        this.SendMessage(Commands.WELCOME, "Оценяваме вашето доверие");
        this.GetMessage();
    }
}

enum Commands {
    WELCOME("Добре дошли в нашето приложение!"),
    ASK_ACCOUNT_NUMBER("Моля, въведете номера на вашата сметка:"),
    ACCOUNT_NUMBER("Номер на сметка"),
    ASK_PIN("Моля, въведете вашия PIN код:"),
    PIN("PIN код"),
    WITHDRAW("Изберете сума за теглене:"),
    DEPOSIT("Изберете сума за депозиране:"),
    GET_BALANCE("Проверка на текущия баланс..."),
    ERROR("Възникна грешка! Опитайте отново."),
    EXIT("Изход от приложението. Довиждане!");

    private final String message;

    Commands(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

enum Data {
    ACCOUNT_NUMBER("Моля въведете номера на Вашата сметка"),
    ACCOUNT("Сметка на потребителя");

    private final String message;

    Data(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

enum Status {
    OK("Успешна транзакция"),
    ERROR("Неуспешна транзакция");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getStatus() {
        return this.message;
    }
}