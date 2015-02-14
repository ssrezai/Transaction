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

//    public BigDecimal updateDepositBalance(Transaction transaction, ArrayList<Deposit> depositArrayList, int position) {
//        BigDecimal result;
//        if (transaction.getType().equals("deposit")) {
//            result = depositArrayList.get(position).getInitialBalance().add(transaction.getAmount());
//        } else
//            result = depositArrayList.get(position).getInitialBalance().subtract(transaction.getAmount());
//        return result;
//    }

    //  @Override
//    public void run() {
//        try {
//            String messageFromServer = "";
//            DataInputStream dataInputStream = new DataInputStream(this.getSocket().getInputStream());
//            String numberOfTransactionStr = dataInputStream.readUTF();
//            System.out.println("#of Transaction: " + numberOfTransactionStr);
//            int numberOfTransactions = Integer.parseInt(numberOfTransactionStr);
//            for (int counter = 0; counter < numberOfTransactions; counter++) {
//                ObjectInputStream objectInputStream = new ObjectInputStream(this.getSocket().getInputStream());
//                Transaction transaction = (Transaction) objectInputStream.readObject();
//                this.getLogger().info("receive new transaction. transaction ID:" + transaction.getId());
//                System.out.println("id: " + transaction.getId());
//
//                try {
//                    if (Validator.validateDepositID(transaction, this.getDepositArrayList())) {
//                        logger.info("Valid user id");
//                        ///now we have a Valid deposit ID, so we can find position of it!
//                        int position = Validator.getTransactionID(transaction, this.getDepositArrayList());
//
//                        if (Validator.validateDepositBalance(transaction, this.getDepositArrayList(), position)) {
//                            System.out.println("we can do your request...");
//                            messageFromServer = "server says: validate transaction";
//                            BigDecimal newValue = updateDepositBalance(transaction, this.getDepositArrayList(), position);
//                            System.out.println(newValue);
//                            this.getDepositArrayList().get(position).setInitialBalance(newValue);
//                        }
//                    }
//                } catch (InvalidDepositID invalidDepositID) {
//                    System.out.println("Invalid Deposit ID");
//                    logger.info("Invalid user DepositID");
//                    messageFromServer = "server says:Invalid user DepositID";
//                } catch (LowBalanceException e) {
//                    System.out.println("LowBalanceException for transactionID= " + transaction.getId());
//                    logger.info("LowBalanceException for transactionID= " + transaction.getAmount());
//                    messageFromServer = "server says:LowBalanceException";
//                } catch (LimitedUpperBoundException e) {
//                    System.out.println("LimitedUpperBoundException for transactionID= " + transaction.getId());
//                    logger.info("LimitedUpperBoundException for transactionID= " + transaction.getAmount());
//                    messageFromServer = "server says: LimitedUpperBoundException";
//                } catch (InvalidTransactionTypeException e) {
//                    System.out.println("Unknown deposit type! " + transaction.getId());
//                    logger.info("Unknown deposit type! " + transaction.getId());
//                    messageFromServer = "server says: Unknown deposit type!";
//                }
//                //server sends message to client....
//                DataOutputStream dataOutputStream = new DataOutputStream(this.getSocket().getOutputStream());
//                dataOutputStream.writeUTF(messageFromServer);
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
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

            } catch (IOException e) {
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            }
        }
        // serverSocket.close();
    }
}
