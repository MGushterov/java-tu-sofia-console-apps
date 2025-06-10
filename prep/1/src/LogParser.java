import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    public static void main(String[] args) throws IOException {
        // Log Parser inputs
        try {
            File file = new File("log.txt");
            if(file.createNewFile()) {
                System.out.println("Successfully created file: " + file.getName());
            }
            else {
                System.out.println("File already exists");
            }
        }
        catch (IOException exception) {
            System.out.println("An error occurred");
            exception.printStackTrace();
        }

        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("log.txt"));

            wr.write("[2025-01-08 13:45:02] INFO    [User: John] - Logged in successfully\n");
            wr.write("[2025-01-08 13:46:10] ERROR   [Admin: Jane] - Invalid password attempt\n");
            wr.write("[2025-01-08 13:47:22] WARNING [User: Anna] - Password about to expire\n");
            wr.write("[2025-01-08 14:02:58] INFO    [Admin: Mike] - Profile updated\n");
            wr.write("[2025-01-08 14:05:00] INFO    [User: John] - Logged out\n");
            wr.write("[2025-01-08 14:09:11] ERROR   [User: Mark] - Payment processing failed\n");

            wr.flush();
            wr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));

            Pattern pattern = Pattern.compile("(\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\])\\s([A-Z]+)\\s+(\\[[A-Z]\\w+:\\s[A-Z]\\w+\\])\\s-\\s(.+)");
            String line;
            while((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()) {
                    String timestamp = matcher.group(1);
                    String logLevel = matcher.group(2);
                    String user = matcher.group(3);
                    String message = matcher.group(4);

                    System.out.printf("Timestamp: %s\n", timestamp);
                    System.out.printf("Log Level: %s\n", logLevel);
                    System.out.printf("User: %s\n", user);
                    System.out.printf("Message: %s\n", message);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}