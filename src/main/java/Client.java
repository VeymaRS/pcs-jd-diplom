import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8989);
             Scanner in = new Scanner(System.in);
             BufferedReader ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String input;

            while (!socket.isOutputShutdown()) {
                System.out.println("Client connected to socket");
                System.out.println("What are you searching for? (input one word or E for exit)");
                input = in.nextLine();
                String[] inputArray = input.split("\\P{IsAlphabetic}+");
                if (inputArray.length == 1 & !input.equals("E")) {
                    out.println(input);
                    Gson gson = new GsonBuilder()
                            .create();
                    Type listType = new TypeToken<List<PageEntry>>() {
                    }.getType();
                    JsonReader jsonReader = new JsonReader(ois);
                    List<PageEntry> pageEntryList = gson.fromJson(jsonReader, listType);
                    for (PageEntry entry : pageEntryList) {
                        System.out.println(entry);
                    }
                } else if (inputArray.length == 1 & input.equals("E")) {
                    out.println("Exit");
                    break;
                } else {
                    System.out.println("Incorrect data entry");
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}