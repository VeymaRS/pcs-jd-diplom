import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8989);
             Scanner in = new Scanner(System.in);
             BufferedReader ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String input;
            System.out.println("Client connected to socket");
            System.out.println("What are you searching for?");
            input = in.nextLine();
            String[] inputArray = input.split("\\P{IsAlphabetic}+");
            if (inputArray.length == 1) {
                out.println(input);
            } else {
                System.out.println("Incorrect data entry");
            }
            ois.lines()
                    .forEach(System.out::println);
        } catch (Exception err) {
            err.printStackTrace();
        }

    }
}