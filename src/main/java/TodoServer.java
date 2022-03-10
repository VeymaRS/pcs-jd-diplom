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
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Starting server at " + port + "...");

            while (!clientSocket.isClosed()) {
                String input = in.readLine();
                if (!input.equals("Exit")) {
                    List<PageEntry> pageEntries = engine.search(input);
                    if (pageEntries != null) {
                        Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .create();
                        Type listType = new TypeToken<List<PageEntry>>() {
                        }.getType();
                        out.println(gson.toJson(pageEntries, listType));
                    } else {
                        out.println("Word not found!");
                    }
                } else {
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
            }
        } catch (Error err) {
            err.printStackTrace();
        }
    }
}

