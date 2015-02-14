import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by DOTIN SCHOOL 3 on 2/14/2015.
 */
public class Terminal2 {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        //read terminal.xml
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ////first terminal.....read transactions....
        TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
        File file = new File("../Transaction/src/main/Resource/terminal2.xml");
        saxParser.parse((file), terminalXmlHandler);
        Logger logger = Logger.getLogger(terminalXmlHandler.getTerminalName());
        FileHandler fileHandler = new FileHandler(terminalXmlHandler.getTerminalLogFile());
        logger.addHandler(fileHandler);


        //list of deposits
        ArrayList<Transaction> transactionsList = terminalXmlHandler.getTransactionsList();

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
