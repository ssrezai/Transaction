import java.io.*;
import java.net.Socket;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 */
public class ServerThread extends Thread {

    private Transaction transaction;
    private Socket socket;

    public ServerThread(Socket socket) {

        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream =new DataOutputStream(socket.getOutputStream());

            String numberOfTransactions=dataInputStream.readUTF();
            int loopSize=new Integer(numberOfTransactions);
            dataOutputStream.writeUTF(String.valueOf(loopSize));
            for(int i=0;i<loopSize;i++)
            {
                Transaction t = (Transaction) objectInputStream.readObject();

                System.out.println("i= "+i+" "+t.getDeposit());
            }


        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
