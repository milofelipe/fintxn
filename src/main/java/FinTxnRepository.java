import java.util.*;

class FinTxnRepository {
    // Stores transactionIds that have been reversed
    private List<String> transactionIdsWithReversals = new ArrayList<>();
    // Stores transactions per account
    private Map<String, List<Transaction>> accountTransactions = new HashMap<>();

    void save(List<Transaction> transactions) {
        transactions.forEach(this::saveTransaction);
    }

    List<Transaction> queryTransactions(String accountId) {
        List<Transaction> transactions = accountTransactions.get(accountId);
        return Objects.nonNull(transactions) ? new ArrayList<>(transactions) : Collections.emptyList();
    }

    boolean transactionReversed(String transactionId) {
        return transactionIdsWithReversals.contains(transactionId);
    }

    private void saveTransaction(Transaction transaction) {
        if (transaction.getRelatedTransaction() != null) {
            // Track transaction Ids with reversals then ignore
            transactionIdsWithReversals.add(transaction.getRelatedTransaction());
            return;
        }

        // Track transactions of from accounts
        List<Transaction> accountTxns = accountTransactions.get(transaction.getFromAccountId());
        if (accountTxns == null) {
            accountTxns = new ArrayList<>();
        }
        accountTxns.add(transaction);

        accountTransactions.put(transaction.getFromAccountId(), accountTxns);

        // Tracks transactions of to accounts
        accountTxns = accountTransactions.get(transaction.getToAccountId());
        if (accountTxns == null) {
            accountTxns = new ArrayList<>();
        }
        accountTxns.add(transaction);

        accountTransactions.put(transaction.getToAccountId(), accountTxns);
    }
}