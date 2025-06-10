import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordPolicyChecker {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("pass.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("pass_policy.txt"));
            Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*\\W).{8,}");
            String newLine;

            while((newLine = reader.readLine()) != null) {
                String[] data = newLine.split("; ");
                String name= data[0];
                String password= data[1];
                Matcher matcher = pattern.matcher(password);

                if(matcher.matches()) {
                    writer.write(name + ": STRONG");
                    writer.newLine();
                }
                else {
                    writer.write(name + ": WEAK");
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("pass.txt"));
//            writer.write("Alice; MyPa$$w0rd\n");
//            writer.write("Bob; password\n");
//            writer.write("Charlie; P@ss123\n");
//
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}