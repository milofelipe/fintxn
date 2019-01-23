import java.time.LocalDateTime;

class TransactionQueryInput {
    private String accountId;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    TransactionQueryInput(String accountId, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        this.accountId = accountId;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    String getAccountId() {
        return accountId;
    }

    LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    LocalDateTime getToDateTime() {
        return toDateTime;
    }
}