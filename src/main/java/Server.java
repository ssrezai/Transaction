import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 * @author samira rezaei
 *         Server class receive request from clients, proccess and return result..
 */
public class Server {

    private Socket socket;
    public ArrayList<Deposit> depositArrayList = new ArrayList<Deposit>();
    private Logger logger = Logger.getLogger("server");

    public Logger getLogger() {
        return logger;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ArrayList<Deposit> getDepositArrayList() {
        return depositArrayList;
    }

    public void setDepositArrayList(ArrayList<Deposit> depositArrayList) {
        this.depositArrayList = depositArrayList;
    }

    public Server(Socket socket) {
        this.socket = socket;
    }

    public Server() {
    }

//    public Socket getSocket() {
//        return socket;
//    }


    public static void main(String[] args) throws Exception {

        ///read core.JSON and config server...///
        Server server = new Server();
        ReadJsonFile readJsonFile = new ReadJsonFile();
        readJsonFile.readJSON();
        ArrayList<Deposit> depositArrayList = readJsonFile.getDepositArrayList();
        server.setDepositArrayList(depositArrayList);
        int port = readJsonFile.getPort();

        ///create log file for server...///
        String logName = readJsonFile.getLogName();

        FileHandler fileHandler = new FileHandler(logName + ".log", true);
        server.getLogger().addHandler(fileHandler);

        ///Socket....///
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server in listening mode... ");
        RandomAccessFile randomAccessFile= new RandomAccessFile("../Transaction/src/main/Resource/output.txt","rw");
        while (true) {
            try {
                Socket clientsSocket = serverSocket.accept();

                server.getLogger().info("server connected to client... " + clientsSocket.getInetAddress() + " from port#:" + clientsSocket.getPort());
                System.out.println("successful connection to client.." + clientsSocket.getInetAddress());
                server.setSocket(clientsSocket);
                ServerThread serverThread = new ServerThread(clientsSocket, server);
                Thread thread = new Thread(serverThread);
                server.getLogger().info("server make a new Thread for " + clientsSocket.getInetAddress() + "with TreadName" + thread.getName());
                server.getLogger().info("make new thread .." + thread.getName());
                thread.start();
                System.out.println("thread name" + thread.getName());
                randomAccessFile.writeUTF("run for thread: "+thread.getName()+"\n\r");
                for(int count=0;count<server.getDepositArrayList().size();count++)
                {
                    randomAccessFile.writeUTF(server.getDepositArrayList().get(count).getName()+":\t"+
                            String.valueOf(server.getDepositArrayList().get(count).getInitialBalance())+"\r" );
                    //randomAccessFile
                    System.out.println(server.getDepositArrayList().get(count).getInitialBalance());
                }

            } catch (IOException e) {
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            }
        }
        // serverSocket.close();
    }
}
