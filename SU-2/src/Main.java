import java.util.Date;
import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

class BankAccount {
    private final int ACCOUNT_DIGITS = 16;
    private final String ACCCOUNT_NUMBER = "3317 2546 6918";
    private double accountBalance;
    private final String CUSTOMER;
    private String accountStatus;
    private final String CREATED_BY;
    private final Date CREATED_ON = new Date();
    private String changedBy;
    private Date changedOn;

    public BankAccount(String customer, String createdBy, double accountBalance, String accountStatus) {
        this.accountBalance = accountBalance;
        this.accountStatus = accountStatus;
        this.CUSTOMER = customer;
        this.CREATED_BY = createdBy;
    }

    public String getAccountNumber() {
        return this.ACCCOUNT_NUMBER;
    }

    public Double getAccountBalance() {
        return this.accountBalance;
    }

    public String getCUSTOMER() {
        return this.CUSTOMER;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public String createAccountNumber() {
        StringJoiner sj = new StringJoiner("");
        for(int i = 0; i < this.ACCOUNT_DIGITS; i++) {
            char digit = (char)(Math.round(Math.random() * 9) + '0');
            sj.add(CharSequence digit);
        }
    }

    protected void Withdraw(double sum) {
        if(sum <= this.accountBalance) {
            this.accountBalance -= sum;
        }
        else {
            System.out.println("Insufficient Balance!");
        }
        System.out.println("Account Balance: " + this.accountBalance);
    }

    protected void Deposit(double sum, String changedBy, Date changedOn) {
        this.accountBalance += sum;
        this.changedBy = changedBy;
        this.changedOn = changedOn;
        System.out.println("Account Balance Updated! New Balance: " + this.accountBalance);
    }
}

interface SavingAccount {
    int calculateInterestRate(int tax);
}

class CurrentAccount extends BankAccount implements SavingAccount{
    private int taxWithdraw;
    private int taxDeposit;
    private int taxMonthlyFee;

    private final double MIN_AMOUNT_WITHDRAW = 1000;

    public CurrentAccount(String customer, String createdBy, double accountBalance, String accountStatus) {
        super(customer, createdBy, accountBalance, accountStatus);
    }

    @Override
    public int calculateInterestRate(int tax) {
        return 0;
    }
}