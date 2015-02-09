/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 * @author Samira Rezai
 */

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerClass extends Thread {


    public static void main(String[] args) throws Exception {
        ReadJsonFile readJsonFile = new ReadJsonFile();
        readJsonFile.readJSON();
        ServerSocket serverSocket = new ServerSocket(readJsonFile.getPort());
        ServerClass server = new ServerClass();
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                String st=((Transaction) objectInputStream.readObject()).getDeposit();
            //System.out.println(((Transaction) objectInputStream.readObject()).getAmount());

                File xmlFile = server.saveFile(socket);
                ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
                transactionsList = server.readXMLFile(xmlFile);
                for (int i = 0; i < transactionsList.size(); i++) {
                    System.out.println(transactionsList.get(i).getAmount());
                }

            }
        } finally {
            System.out.println("Done......");
            serverSocket.close();
        }
    }

    public File saveFile(Socket socket) throws Exception {
        try {

            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            File clientToServer = new File("../Transaction/src/main/Resource/clientToServer.xml");
            OutputStream outStream = new FileOutputStream(clientToServer);
            outStream.write(buffer);
            return clientToServer;
        } finally {
            socket.close();

        }

    }

    public ArrayList<Transaction> readXMLFile(File file) {
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
            saxParser.parse((file), terminalXmlHandler);
            transactionsList = terminalXmlHandler.getTransactionsList();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionsList;
    }
}

