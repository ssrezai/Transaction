import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by DOTIN SCHOOL 3 on 2/9/2015.
 */
public class ServerThread implements Runnable {

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

            Transaction t = (Transaction) objectInputStream.readObject();

            objectOutputStream.writeObject(t);
            System.out.println(t.getId() + "send from client");
            System.out.println(t.getAmount() + " :send from client");
            objectOutputStream.writeObject(t);

        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
