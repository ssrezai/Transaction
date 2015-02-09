import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 * read terminal information
 */
public class TerminalXmlHandler extends DefaultHandler {
    private ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
    private String ipAddress;
    private int port;
    private String TerminalLogFile;

    public ArrayList<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(ArrayList<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTerminalLogFile() {
        return TerminalLogFile;
    }

    public void setTerminalLogFile(String terminalLogFile) {
        TerminalLogFile = terminalLogFile;
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase("transaction")) {
            Transaction transaction = new Transaction();
            transaction.setId(attributes.getValue("id"));
            transaction.setType(attributes.getValue("type"));
            transaction.setAmount(new BigDecimal(attributes.getValue("amount")));
            transaction.setDepositId(attributes.getValue("deposit"));
            transactionsList.add(transaction);

        }
    }

    @Override
    public void endElement(String uri, String localName, String elementName) throws SAXException {

    }

    public void characters(char[] ch, int start, int length) throws SAXException {

    }
}
