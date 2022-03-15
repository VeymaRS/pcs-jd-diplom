import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TodoServer {
    BooleanSearchEngine engine;
    private int port;

    public TodoServer(int port, BooleanSearchEngine engine) {
        this.port = port;
        this.engine = engine;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)
        ) {
            System.out.println("Starting server at " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String input = in.readLine();

                List<PageEntry> pageEntries = engine.search(input);
                if (pageEntries != null) {
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                    Type listType = new TypeToken<List<PageEntry>>() {
                    }.getType();
                    out.println(gson.toJson(pageEntries, listType));
                    in.close();
                    out.close();
                } else {
                    String notWord = "The word is not found!";
                    out.println(notWord);
                    in.close();
                    out.close();
                }
            }
        } catch (Error err) {
            err.printStackTrace();
        }
    }
}

