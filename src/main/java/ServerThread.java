import exception.InvalidDepositID;
import exception.InvalidTransactionTypeException;
import exception.LimitedUpperBoundException;
import exception.LowBalanceException;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 * @author  Samira Rezaei
 */
public class ServerThread implements Runnable {

    private List<Deposit> depositArrayList = Collections.synchronizedList(new ArrayList<Deposit>());

    private Socket socket;
    private Server server;

    //    public ServerThread(Socket socket,ArrayList<Deposit>depositArrayList) {
//
//        this.socket = socket;
//        this.depositArrayList=depositArrayList;
//    }
    public ServerThread(Socket socket, Server server) {

        this.socket = socket;
        this.server = server;
        this.depositArrayList = server.getDepositArrayList();
    }

    public BigDecimal updateDepositBalance(Transaction transaction, ArrayList<Deposit> depositArrayList, int position) {
        BigDecimal result;
        if (transaction.getType().equals("deposit")) {
            synchronized (depositArrayList.get(position)) {
                result = depositArrayList.get(position).getInitialBalance().add(transaction.getAmount());
            }
        } else {
            result = depositArrayList.get(position).getInitialBalance().subtract(transaction.getAmount());
        }
        // this.depositArrayList.get(position).setInitialBalance(result);
        return result;
    }

    @Override
    public void run() {
        try {
            String messageFromServer = "";
            DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
            String numberOfTransactionStr = dataInputStream.readUTF();
            System.out.println("#of Transaction: " + numberOfTransactionStr);
            int numberOfTransactions = Integer.parseInt(numberOfTransactionStr);
            for (int counter = 0; counter < numberOfTransactions; counter++) {
                ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
                Transaction transaction = (Transaction) objectInputStream.readObject();
                this.server.getLogger().info("receive new transaction. transaction ID:" + transaction.getId());
                System.out.println("id: " + transaction.getId());

                try {
                    if (Validator.validateDepositID(transaction, this.server.getDepositArrayList())) {
                        this.server.getLogger().info("Valid user id");
                        ///now we have a Valid deposit ID, so we can find position of it!
                        int position = Validator.getTransactionID(transaction, this.server.getDepositArrayList());

                        synchronized (this.depositArrayList.get(position)) {
//                            if (!this.depositArrayList.get(position).getSynch()) {
//                                this.depositArrayList.get(position).setSynch(true);
                            if (Validator.validateDepositBalance(transaction, this.server.getDepositArrayList(), position)) {
                                System.out.println("we can do your request...");
                                messageFromServer = "server says: validate transaction";
                                BigDecimal newValue = updateDepositBalance(transaction, server.getDepositArrayList(), position);
                                this.depositArrayList.get(position).setInitialBalance(newValue);
                                System.out.println(newValue);
                                this.server.getDepositArrayList().get(position).setInitialBalance(newValue);
                            }
                            this.depositArrayList.get(position).setSynch(false);
                            // notify();
//                            } else {
//                                wait();
//                            }
                        }
                    }
                } catch (InvalidDepositID invalidDepositID) {
                    System.out.println("Invalid Deposit ID");
                    this.server.getLogger().info("Invalid user DepositID");
                    messageFromServer = "server says:Invalid user DepositID";
                } catch (LowBalanceException e) {
                    System.out.println("LowBalanceException for transactionID= " + transaction.getId());
                    this.server.getLogger().info("LowBalanceException for transactionID= " + transaction.getAmount());
                    messageFromServer = "server says:LowBalanceException";
                } catch (LimitedUpperBoundException e) {
                    System.out.println("LimitedUpperBoundException for transactionID= " + transaction.getId());
                    this.server.getLogger().info("LimitedUpperBoundException for transactionID= " + transaction.getAmount());
                    messageFromServer = "server says: LimitedUpperBoundException";
                } catch (InvalidTransactionTypeException e) {
                    System.out.println("Unknown deposit type! " + transaction.getId());
                    this.server.getLogger().info("Unknown deposit type! " + transaction.getId());
                    messageFromServer = "server says: Unknown deposit type!";
                } //catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //server sends message to client....
                DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
                dataOutputStream.writeUTF(messageFromServer);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
