import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

class Contact {
    private String name;
    private String email;

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Contact() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("Name saved");
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
        System.out.println("Email saved");
    }
}

class Engine {
    private ArrayList<Contact> contacts;
    public Engine(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        printCommands();
        System.out.print("Choose command: ");
        int command;
        while((command = scanner.nextInt()) != 4) {
            System.out.println();
            scanner.nextLine();

            if(command == 1) {
                listContacts(this.contacts);
            }
            else if(command == 2) {
                addNewContact(this.contacts);
            }
            else if(command == 3) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.println();
                findContactByName(this.contacts, username);
            }
            else {
                System.out.println("Invalid input");
            }
            printCommands();
            System.out.print("Choose command: ");
        }
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("contacts.txt"));
            for (Contact contact : this.contacts) {
                wr.write(contact.getName() + ", " + contact.getEmail());
                wr.newLine();
            }
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCommands() {
        System.out.println("1) List all contacts");
        System.out.println("2) Add a new contact");
        System.out.println("3) Find contact by name");
        System.out.println("4) Exit");
    }

    private static void listContacts(ArrayList<Contact> contacts) {
        for(Contact contact : contacts) {
            System.out.printf("-----\nName: %s\nEmail: %s\n-----\n", contact.getName(), contact.getEmail());
        }
    }

    private static void addNewContact(ArrayList<Contact> contacts) {
        Contact newContact = new Contact();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter contact name: ");
        String name = scanner.nextLine();
        System.out.print("\nEnter contact email: ");
        String email = scanner.nextLine();
        System.out.println();

        newContact.setName(name);
        newContact.setEmail(email);

        if(contacts.add(newContact)) {
            System.out.printf("Successfully added %s to contacts\n", name);
        }
    }

    private static void findContactByName(ArrayList<Contact> contacts, String targetName) {
        boolean wasFound = false;
        for(Contact contact : contacts) {
            if(contact.getName().equals(targetName)) {
                System.out.printf("Email: %s\n", contact.getEmail());
                wasFound = true;
                break;
            }
        }
        if(!wasFound) {
            System.out.println("No user was found");
        }
    }
}

public class ContactManager {
    public static void main(String[] args) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("contacts.txt"));
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                Contact newContact = new Contact(data[0], data[1]);
                contacts.add(newContact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Engine engine = new Engine(contacts);
        engine.run();
//        try {
//            File file = new File("contacts.txt");
//            if(file.createNewFile()) {
//                System.out.println("Success");
//            }
//            BufferedWriter wr = new BufferedWriter(new FileWriter("contacts.txt"));
//
//            String[] contacts = {
//                    "John Doe, john.doe@example.com",
//                    "Jane Smith, jane.smith@example.com",
//                    "Bob Brown, bob.brown@example.net",
//                    "Alice Johnson, alice.j@example.org",
//                    "Charlie Black, charlie.black@example.com"
//            };
//
//            for (String contact : contacts) {
//                wr.write(contact);
//                wr.newLine(); // Add a new line after each contact
//            }
//
//            wr.flush();
//            wr.close();
//            System.out.println("Contacts file created successfully!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}