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
 *
 * @author Samira Rezaei
 */
public class Terminal2 {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        //read terminal.xml
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ////first terminal.....read transactions....
        TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
        File file = new File("../Transaction/src/main/Resource/Client/terminal2.xml");
        saxParser.parse((file), terminalXmlHandler);
        Logger logger = Logger.getLogger(terminalXmlHandler.getTerminalName());
        FileHandler fileHandler = new FileHandler(terminalXmlHandler.getTerminalLogFile());
        logger.addHandler(fileHandler);
        int port=terminalXmlHandler.getPort();

        //list of deposits
        ArrayList<Transaction> transactionsList = terminalXmlHandler.getTransactionsList();
        //list of outputs..
        ArrayList<String> statusList = new ArrayList<String>();
        ArrayList<String> balanceList = new ArrayList<String>();

        Socket clientSocket = new Socket(InetAddress.getLocalHost(), port);
        try {
            int numOfTransaction = transactionsList.size();
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream.writeUTF(String.valueOf(numOfTransaction));
            logger.info("client sent #of total transactions..");

            /////send all of transaction in this loop
            for (int counter = 0; counter < transactionsList.size(); counter++) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                logger.info("sending request for transaction ID:" + transactionsList.get(counter).getId());
                ////////send one transaction to server for checking...///////
                objectOutputStream.writeObject(transactionsList.get(counter));
                logger.info("successfully sent request for transaction ID:" + transactionsList.get(counter).getId());
                String str = dataInputStream.readUTF();
                String status = dataInputStream.readUTF();
                String balance = dataInputStream.readUTF();
                statusList.add(counter, status);
                balanceList.add(counter, balance);
                System.out.println(str);
                logger.info(str);
            }

        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }

        WriteXML.writeTransactionResult(transactionsList, statusList, balanceList, terminalXmlHandler.getTerminalName());
        System.out.print(InetAddress.getLocalHost());
        clientSocket.close();
        //System.exit(0);
    }


}
