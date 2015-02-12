import exception.InvalidDepositID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //test LOG file
//        boolean append = false;
//        FileHandler fileHandler = new FileHandler("ServerLog.log", append);
//
//        WriteLogEntriesToLogFile.serverWriteLogFile("SERVERclass", Level.SEVERE, fileHandler);
//        WriteLogEntriesToLogFile.serverWriteLogFile("SERVERclass", Level.CONFIG, fileHandler);
//        WriteLogEntriesToLogFile.serverWriteLogFile("SERVERclass", Level.INFO, fileHandler);
//
//        //test read xml file
//        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//        SAXParser saxParser = saxParserFactory.newSAXParser();
//        TerminalXmlHandler terminalXmlHandler = new TerminalXmlHandler();
//
//      saxParser.parse(new File("../Transaction/src/main/Resource/terminal.xml"), terminalXmlHandler);
//        for (int i = 0; i < terminalXmlHandler.getTransactionsList().size(); i++) {
//            System.out.println(terminalXmlHandler.getTransactionsList().get(i).getId());
//        }
//
//        //test read json
//        ReadJsonFile readJsonFile = new ReadJsonFile();
//        readJsonFile.readJSON();
//        System.out.print(readJsonFile.getDepositArrayList().get(3).getName());


        ArrayList<String>arrayList= new ArrayList<String>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add("6");
      try{  if(validate("6",arrayList))
        {
            System.out.print("yes");

        }}
        catch(InvalidDepositID e)
        {
            System.out.print("no");
        }
    }

    public static boolean validate(String str, ArrayList<String> arrayList) throws InvalidDepositID {
        boolean result = false;
        for (int counter = 0; counter < arrayList.size(); counter++) {
            String s1 = str;
            String s2 = arrayList.get(counter);
            if (s1.equals(s2)) {
                result = true;
                return result;
               // break;
            }
        }
        throw new InvalidDepositID("Invalid deposit ID!");
    }

}