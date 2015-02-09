import exception.LowBalanceException;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 * Transactions....
 */
public class Transaction implements Serializable {
    private String id;
    private String type;
    private BigDecimal amount;
    private String depositId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDeposit() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

//    public BigDecimal depositValue() {
//        BigDecimal result;
//        try {
//           if( Validator.validateDeposit(this))
//               result=this.getDeposit().subtract(this.getAmount());
//        }
//        catch (LowBalanceException ex)
//        {
//            System.out.println("Low balance...");
//        }
//        return result;
//    }
}
