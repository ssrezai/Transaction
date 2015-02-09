/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *  @author samira Rezaei
 */

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Terminal {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {


        //   int num;
        //read terminal.xml
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
        File file = new File("../Transaction/src/main/Resource/terminal.xml");
        //  byte[] byteArray = new byte[(int) file.length()];
        saxParser.parse((file), terminalXmlHandler);
        //list of deposits
        ArrayList<Transaction> transactionsList = terminalXmlHandler.getTransactionsList();

        ObjectOutputStream objectOutputStream;
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8080);
        try {
            for (int i = 0; i < 2; i++) {

                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                //send one transaction to server for checking...
                objectOutputStream.writeObject(transactionsList.get(i));
                Transaction transaction = (Transaction) objectInputStream.readObject();
                System.out.println(transaction.getId() + ": id send from server");
                System.out.println(transaction.getAmount() + ": amount send from server");
                objectInputStream.close();
                objectOutputStream.close();
            }
        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }


        System.out.print(InetAddress.getLocalHost());



        //System.exit(0);
    }

}
