import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 */
public class Server {

    public static void main(String[] args) throws Exception {

        //read core.JSON and config server...
        ReadJsonFile readJsonFile = new ReadJsonFile();
        readJsonFile.readJSON();
        int port = readJsonFile.getPort();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server in listening mode... ");
        ServerThread serverThread;
        try {
            while (true) {
                System.out.println("... connection to client..");
                Socket socket = serverSocket.accept();
                System.out.println("successful connection to client.."+socket.getInetAddress());
                serverThread = new ServerThread(socket);
               Thread thread = new Thread(serverThread);
                System.out.println("thred name" + thread.getName());
             thread.start();

            }
        } catch (IOException e) {
            System.out.println("Accept failed: " + port);
            System.exit(-1);
        }
       // serverSocket.close();
    }
}
