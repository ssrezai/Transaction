import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *
 * @author Samira Rezai
 *         READ core.json
 */
public class ReadJsonFile {
    private ArrayList<Deposit> depositArrayList = new ArrayList<Deposit>();
    private int port;
    private String logName;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public ArrayList<Deposit> getDepositArrayList() {
        return depositArrayList;
    }

    public void readJSON() {

        org.json.simple.parser.JSONParser jsonParser1 = new org.json.simple.parser.JSONParser();
        try {

            Object object = jsonParser1.parse(new FileReader("..\\Transaction\\src\\main/Resource/core.json"));

            JSONObject jsonObject = (JSONObject) object;

            int port = Integer.parseInt(jsonObject.get("port").toString());
            this.setPort(port);

            JSONArray deposits = (JSONArray) jsonObject.get("deposits");
            for (Object deposit1 : deposits) {
                JSONObject innerObj = (JSONObject) deposit1;
                Deposit deposit = new Deposit();
                deposit.setName((String) innerObj.get("customer"));
                deposit.setId((String) innerObj.get("id"));
                BigDecimal bigDecimal = new BigDecimal((String) innerObj.get("initialBalance"));
                deposit.setInitialBalance(bigDecimal);
                BigDecimal upperBound = new BigDecimal((String) innerObj.get("upperBound"));
                deposit.setUpperBound(upperBound);
                depositArrayList.add(deposit);
            }


            String outLogName = (String) jsonObject.get("outLog");
            this.setLogName(outLogName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

