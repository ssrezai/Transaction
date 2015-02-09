import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import  org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *
 * @author Samira Rezai
 *         READ core.json
 */
public class ReadJsonFile {
    private ArrayList<Deposit> depositArrayList = new ArrayList<Deposit>();
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<Deposit> getDepositArrayList() {
        return depositArrayList;
    }

    public void readJSON() {
        Deposit deposit = new Deposit();
        org.json.simple.parser.JSONParser jsonParser1 = new org.json.simple.parser.JSONParser();
        try {

            Object object = jsonParser1.parse(new FileReader("..\\Transaction\\src\\main/Resource/core.json"));

            JSONObject jsonObject = (JSONObject) object;

            int port = Integer.parseInt(jsonObject.get("port").toString());
            this.setPort(port);

            JSONArray deposits = (JSONArray) jsonObject.get("deposits");
            Iterator iterator = deposits.iterator();
            while (iterator.hasNext()) {
                JSONObject innerObj = (JSONObject) iterator.next();
                deposit.setName((String) innerObj.get("customer"));
                deposit.setId((String) innerObj.get("id"));
                BigDecimal b = new BigDecimal((String) innerObj.get("initialBalance"));
                deposit.setInitialBalance(b);
                BigDecimal bb = new BigDecimal((String) innerObj.get("upperBound"));
                deposit.setUpperBound(bb);
                depositArrayList.add(deposit);
            }


            String outLogName = (String) jsonObject.get("outLog");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

