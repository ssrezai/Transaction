/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *  @author samira Rezaei
 */

import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Terminal {


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        //read terminal.xml
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ////first terminal.....read transactions....
        TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
        File file = new File("../Transaction/src/main/Resource/terminal.xml");
        saxParser.parse((file), terminalXmlHandler);
        Logger logger = Logger.getLogger(terminalXmlHandler.getTerminalName());
        FileHandler fileHandler = new FileHandler(terminalXmlHandler.getTerminalLogFile());
        logger.addHandler(fileHandler);

        //second terminal....read transactions...
        File file2 = new File("../Transaction/src/main/Resource/terminal.xml");
        TerminalXmlHandler terminalXmlHandler2 = new TerminalXmlHandler();
        saxParser.parse(file2, terminalXmlHandler2);
        //list of deposits
        ArrayList<Transaction> transactionsList = terminalXmlHandler.getTransactionsList();
        //ArrayList<Transaction> transactionsList2 = terminalXmlHandler2.getTransactionsList();


        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8080);
        try {
            int numOfTransaction = transactionsList.size();
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dataInputStream= new DataInputStream(clientSocket.getInputStream());
            dataOutputStream.writeUTF(String.valueOf(numOfTransaction));
            logger.info("client sent #of total transactions..");

        /////send all of transaction in this loop
            for (Transaction aTransactionsList : transactionsList) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                logger.info("sending request for transaction ID:" + aTransactionsList.getId());
                ////////send one transaction to server for checking...///////
                objectOutputStream.writeObject(aTransactionsList);
                logger.info("successfully sent request for transaction ID:" + aTransactionsList.getId());
                String str = dataInputStream.readUTF();
                System.out.println(str);
                logger.info(str);
            }

        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }


        System.out.print(InetAddress.getLocalHost());


        //System.exit(0);
    }


}
