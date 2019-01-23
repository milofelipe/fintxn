import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

class Transaction {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private LocalDateTime createAt;
    private BigDecimal amount;
    private TransactionType type;
    private String relatedTransaction;

    static class Builder {
        private String transactionId;
        private String fromAccountId;
        private String toAccountId;
        private LocalDateTime createAt;
        private BigDecimal amount = BigDecimal.ZERO;
        private TransactionType type = TransactionType.PAYMENT;
        private String relatedTransaction;

        Builder(String transactionId, String fromAccountId, String toAccountId) {
            this.transactionId = transactionId;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
        }

        Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        Builder relatedTransaction(String relatedTransaction) {
            this.relatedTransaction = relatedTransaction;
            return this;
        }

        Transaction build() {
            Transaction transaction = new Transaction();
            transaction.transactionId = this.transactionId;
            transaction.fromAccountId = this.fromAccountId;
            transaction.toAccountId = this.toAccountId;

            if (Objects.isNull(this.createAt)) {
                transaction.createAt = LocalDateTime.now();
            } else {
                transaction.createAt = this.createAt;
            }

            transaction.amount = this.amount;
            transaction.type = this.type;
            transaction.relatedTransaction = this.relatedTransaction;

            return transaction;
        }
    }

    private Transaction() {
    }

    String getTransactionId() {
        return transactionId;
    }

    String getFromAccountId() {
        return fromAccountId;
    }

    String getToAccountId() {
        return toAccountId;
    }

    LocalDateTime getCreateAt() {
        return createAt;
    }

    BigDecimal getAmount() {
        return amount;
    }

    TransactionType getType() {
        return type;
    }

    String getRelatedTransaction() {
        return relatedTransaction;
    }
}