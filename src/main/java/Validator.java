import exception.InvalidDepositIDException;
import exception.InvalidTransactionTypeException;
import exception.LimitedUpperBoundException;
import exception.LowBalanceException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 *
 * @author samira Rezaei
 */
public class Validator {
    public static boolean validateDepositID(Transaction transaction, ArrayList<Deposit> depositArrayList) throws InvalidDepositIDException {
        String transactionDepositID = transaction.getDeposit();
        int counter = 0;
        while (counter < depositArrayList.size()) {
            String depositID = depositArrayList.get(counter).getId();
            if (transactionDepositID.equals(depositID)) {
                return true;
            }
            counter++;
        }
        throw new InvalidDepositIDException("Invalid deposit ID!");
    }

    public static boolean validateDepositBalance(Transaction transaction, ArrayList<Deposit> depositArrayList, int position)
            throws InvalidTransactionTypeException, LimitedUpperBoundException, LowBalanceException {
        String transactionType = transaction.getType();

        if (transactionType.equals("deposit")) {
            if (!validateDeposit(transaction, depositArrayList, position)) {
                throw new LimitedUpperBoundException("");
            }

        } else if (transactionType.equals("withdraw")) {
            if (!validateWithdraw(transaction, depositArrayList, position)) {
                throw new LowBalanceException("");
            }
        } else
            throw new InvalidTransactionTypeException("");
        return true;
    }

    public static boolean validateDeposit(Transaction transaction, ArrayList<Deposit> depositArrayList, int position) throws LimitedUpperBoundException {
        BigDecimal amount = transaction.getAmount();
        BigDecimal initialBalance = depositArrayList.get(position).getInitialBalance();
        BigDecimal afterDeposit = amount.add(initialBalance);
        if (afterDeposit.compareTo(depositArrayList.get(position).getUpperBound()) < 0) {
            return true;
        } else {
            throw new LimitedUpperBoundException("");
        }

    }

    public static boolean validateWithdraw(Transaction transaction, ArrayList<Deposit> depositArrayList, int position) throws LowBalanceException {
        BigDecimal amount = transaction.getAmount();
        BigDecimal initialBalance = depositArrayList.get(position).getInitialBalance();
        if (amount.compareTo(initialBalance) < 0) {
            return true;
        } else {
            throw new LowBalanceException("");
        }
    }

    public static int getTransactionID(Transaction transaction, ArrayList<Deposit> depositArrayList) {
        int position = -100;
        boolean find = false;
        while (!find) {
            for (int counter = 0; counter < depositArrayList.size(); counter++) {
                if (transaction.getDeposit().equals(depositArrayList.get(counter).getId())) {
                    position = counter;
                    find = true;

                }
            }
        }
        return position;
    }
}
