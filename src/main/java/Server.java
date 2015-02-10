import exception.InvalidDepositID;
import exception.LowBalanceException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 *
 * @author samira rezaei
 *         Server class receive request from clients, proccess and return result..
 */
public class Server implements Runnable {

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

    public Socket getSocket() {
        return socket;
    }


    @Override
    public void run() {
        try {

            DataInputStream dataInputStream = new DataInputStream(this.getSocket().getInputStream());
            String numberOfTransactionStr = dataInputStream.readUTF();
            System.out.println("#of Transaction: " + numberOfTransactionStr);
            int numberOfTransactions = Integer.parseInt(numberOfTransactionStr);
            for (int counter = 0; counter < numberOfTransactions; counter++) {
                ObjectInputStream objectInputStream = new ObjectInputStream(this.getSocket().getInputStream());
                Transaction transaction = (Transaction) objectInputStream.readObject();
                this.getLogger().info("receive new transaction. transaction ID:" + transaction.getId());
                System.out.println("id: " + transaction.getId());
               try {
                   if (validateDepositID(transaction, this.getDepositArrayList())) {

                       logger.info("valid user id");
                   }

               }
               catch (InvalidDepositID invalidDepositID) {
                   invalidDepositID.printStackTrace();
               }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static boolean validateDepositID(Transaction transaction, ArrayList<Deposit> depositArrayList) throws  InvalidDepositID {
        boolean result = false;
        for (int i = 0; i < depositArrayList.size(); i++) {

            if (transaction.getDeposit() == (depositArrayList.get(i).getId())) {
                result = true;
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        ///read core.JSON and config server...///
        Server server = new Server();
        ReadJsonFile readJsonFile = new ReadJsonFile();
        readJsonFile.readJSON();
        server.setDepositArrayList(readJsonFile.getDepositArrayList());
        int port = readJsonFile.getPort();

        ///create log file for server...///
        String logName = readJsonFile.getLogName();

        FileHandler fileHandler = new FileHandler(logName + ".log", true);
        server.getLogger().addHandler(fileHandler);

        ///Socket....///
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server in listening mode... ");
        try {
            Socket clientsSocket = serverSocket.accept();

            server.getLogger().info("server connected to client... " + clientsSocket.getInetAddress() + " from port#:" + clientsSocket.getPort());
            System.out.println("successful connection to client.." + clientsSocket.getInetAddress());
            server.setSocket(clientsSocket);
            Thread thread = new Thread(server);
            server.getLogger().info("make new thread .." + thread.getName());
            thread.start();
            server.getLogger().info("server make a new Thread for " + clientsSocket.getInetAddress() + "with TreadName" + thread.getName());
            System.out.println("thread name" + thread.getName());

        } catch (IOException e) {
            System.out.println("Accept failed: " + port);
            System.exit(-1);
        }
        // serverSocket.close();
    }
}
