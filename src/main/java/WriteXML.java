/**
 * Created by DOTIN SCHOOL 3 on 2/15/2015.
 *
 */
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class WriteXML {
    public static void writeTransactionResult(ArrayList<Transaction> transactions,ArrayList<String> statusList,ArrayList<String> balanceList,String name) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("transactions");
            doc.appendChild(rootElement);

            // staff elements
            for(int counter=0; counter<transactions.size();counter++){
                Element transaction = doc.createElement("transaction");
                rootElement.appendChild(transaction);

                // set attribute to staff element
                Attr id = doc.createAttribute("id");
                Attr type = doc.createAttribute("type");
                id.setValue(transactions.get(counter).getId());
                type.setValue(transactions.get(counter).getType());
                transaction.setAttributeNode(id);
                transaction.setAttributeNode(type);


                // message elements
                Element status = doc.createElement("status");
                status.appendChild(doc.createTextNode(statusList.get(counter)));
                transaction.appendChild(status);

                // balance elements
                Element balance = doc.createElement("balance");
                balance.appendChild(doc.createTextNode(balanceList.get(counter)));
                transaction.appendChild(balance);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("../Transaction/src/main/Outputs/"+name+".xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
