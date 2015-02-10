import exception.InvalidDepositID;
import exception.LowBalanceException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *
 * @author samira Rezaei
 */
public class Validator {
    public static boolean validateDepositID(Transaction transaction, ArrayList<Deposit> depositArrayList) throws  InvalidDepositID {
        boolean result = false;
        for (int i = 0; i < depositArrayList.size(); i++) {

            if (transaction.getDeposit() == (depositArrayList.get(i).getId())) {
                result = true;
            } else {
                throw new InvalidDepositID("Invalid deposit ID!");
            }
        }
        return result;
    }
}
