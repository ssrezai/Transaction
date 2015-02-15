import exception.InvalidDepositIDException;
import exception.InvalidTransactionTypeException;
import exception.LimitedUpperBoundException;
import exception.LowBalanceException;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 *
 * @author Samira Rezaei
 */
public class ServerThread implements Runnable {

    //private List<Deposit> depositArrayList = Collections.synchronizedList(new ArrayList<Deposit>());
    private List<Deposit> depositArrayList = new ArrayList<Deposit>();
    private Socket socket;
    private Server server;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.depositArrayList = server.getDepositArrayList();
    }

    public BigDecimal updateDepositBalance(Transaction transaction, ArrayList<Deposit> depositArrayList, int position) {
        BigDecimal result;
        if (transaction.getType().equals("deposit")) {
            result = depositArrayList.get(position).getInitialBalance().add(transaction.getAmount());
        } else {
            result = depositArrayList.get(position).getInitialBalance().subtract(transaction.getAmount());
        }
        return result;
    }

    @Override
    public void run() {
        try {
            String messageFromServer = "";
            String status = "";
            String balance = " ";
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String numberOfTransactionStr = dataInputStream.readUTF();
            System.out.println("#of Transaction: " + numberOfTransactionStr);
            int numberOfTransactions = Integer.parseInt(numberOfTransactionStr);
            ///for each transaction
            for (int counter = 0; counter < numberOfTransactions; counter++) {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Transaction transaction = (Transaction) objectInputStream.readObject();
                synchronized (server.getLogger()) {
                    server.getLogger().info("receive new transaction. transaction ID:" + transaction.getId());
                }
                System.out.println("id: " + transaction.getId());
                try {
                    if (Validator.validateDepositID(transaction, server.getDepositArrayList())) {
                        synchronized (server.getLogger()) {
                            server.getLogger().info("Valid user id");
                        }
                        ///now we have a Valid deposit ID, so we can find index of it!
                        int index = Validator.getTransactionID(transaction, server.getDepositArrayList());
                        System.out.println("initialBalance: " + depositArrayList.get(index).getInitialBalance());
                        synchronized (depositArrayList.get(index)) {
                            if (Validator.validateDepositBalance(transaction, server.getDepositArrayList(), index)) {
                                System.out.println("we can do your request...");
                                messageFromServer = "server says: validate transaction";
                                BigDecimal newValue = updateDepositBalance(transaction, server.getDepositArrayList(), index);
                                Thread.sleep(1100);
                                depositArrayList.get(index).setInitialBalance(newValue);
                                System.out.println("new Balance: " + depositArrayList.get(index).getInitialBalance());
                                server.getDepositArrayList().get(index).setInitialBalance(newValue);
                                status = "Successful";
                                balance = String.valueOf(server.getDepositArrayList().get(index).getInitialBalance());
                            }
                        }
                    }
                } catch (InvalidDepositIDException invalidDepositID) {
                    System.out.println("Invalid Deposit ID");
                    synchronized (server.getLogger()) {
                        server.getLogger().info("Invalid user DepositID");
                    }
                    messageFromServer = "server says:Invalid user DepositID";
                    status = "Unsuccessful";
                } catch (LowBalanceException e) {
                    System.out.println("LowBalanceException for transactionID= " + transaction.getId());
                    synchronized (server.getLogger()) {
                        server.getLogger().info("LowBalanceException for transactionID= " + transaction.getAmount());
                    }
                    messageFromServer = "server says:LowBalanceException";
                    status = "Unsuccessful";
                } catch (LimitedUpperBoundException e) {
                    System.out.println("LimitedUpperBoundException for transactionID= " + transaction.getId());
                    synchronized (server.getLogger()) {
                        server.getLogger().info("LimitedUpperBoundException for transactionID= " + transaction.getAmount());
                    }
                    messageFromServer = "server says: LimitedUpperBoundException";
                } catch (InvalidTransactionTypeException e) {
                    System.out.println("Unknown deposit type! " + transaction.getId());
                    synchronized (server.getLogger()) {
                        server.getLogger().info("Unknown deposit type! " + transaction.getId());
                    }
                    messageFromServer = "server says: Unknown deposit type!";
                    status = "Unsuccessful";
                } //catch (InterruptedException e) {
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                    e.printStackTrace();
//                }
                //server sends message to client....
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(messageFromServer);
                dataOutputStream.writeUTF(status);
                dataOutputStream.writeUTF(balance);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
