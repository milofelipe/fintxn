import java.math.BigDecimal;

class FinTxnAnalysisResult {
    private BigDecimal balance;
    private int numberOfTransactions;

    FinTxnAnalysisResult(BigDecimal balance, int numberOfTransactions) {
        this.balance = balance;
        this.numberOfTransactions = numberOfTransactions;
    }

    BigDecimal getBalance() {
        return balance;
    }

    int getNumberOfTransactions() {
        return numberOfTransactions;
    }
}