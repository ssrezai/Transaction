import java.math.BigDecimal;

/**
 * Created by DOTIN SCHOOL 3 on 2/7/2015.
 * @author Samira Rezai
 * Deposit class
 */
public class Deposit {
    private String name;
    private String id;
   private BigDecimal initialBalance;
  private   BigDecimal upperBound;
//  private   Boolean synchronize=false;
//
//    public Boolean getSynchronize() {
//        return synchronize;
//    }
//
//    public void setSynchronize(Boolean synchronize) {
//        this.synchronize = synchronize;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public String getName() {
        return name;
    }
}
