import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class FinTxnService {
    private FinTxnRepository finTxnRepository;

    FinTxnService(FinTxnRepository finTxnRepository) {
        this.finTxnRepository = finTxnRepository;
    }

    FinTxnAnalysisResult analyse(String accountId, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        // Get all transactions of the account then filter by date time range and reversal
        List<Transaction> accountTransactions = finTxnRepository.queryTransactions(accountId).stream()
                .filter(transaction -> (transaction.getCreateAt().isAfter(fromDateTime) || transaction.getCreateAt().equals(fromDateTime)) &&
                        (transaction.getCreateAt().isBefore(toDateTime) || transaction.getCreateAt().equals(toDateTime)))
                .filter(transaction -> !finTxnRepository.transactionReversed(transaction.getTransactionId()))
                .collect(Collectors.toList());

        BigDecimal balance = BigDecimal.ZERO;
        int numberOfTransactions = 0;

        for (Transaction transaction : accountTransactions) {
            numberOfTransactions++;
            if (transaction.getFromAccountId().equals(accountId)) {
                balance = balance.subtract(transaction.getAmount());
            } else if (transaction.getToAccountId().equals(accountId)) {
                balance = balance.add(transaction.getAmount());
            }
        }

        return new FinTxnAnalysisResult(balance, numberOfTransactions);
    }

    void loadTransactionsFromCsvFile(String inputFile) {
        List<Transaction> transactions = processTransactionFile(inputFile);
        finTxnRepository.save(transactions);
    }

    List<Transaction> processTransactionFile(String inputFilePath) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            File inputFile = new File(inputFilePath);
            InputStream fileInputStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            // skip the header of the csv
            transactions = br.lines().skip(1).map(mapLineToTransaction).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            System.out.println("There was a problem reading the input file.");
            System.exit(0);
        }

        return transactions;
    }

    private Function<String, Transaction> mapLineToTransaction = (line) -> {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String[] tokens = line.split(",");
        Transaction.Builder builder = new Transaction.Builder(tokens[0].trim(), tokens[1].trim(), tokens[2].trim())
                .createAt(LocalDateTime.parse(tokens[3].trim(), dateTimeFormatter))
                .amount(new BigDecimal(tokens[4].trim()))
                .type(TransactionType.valueOf(tokens[5].trim()));

        if (tokens.length == 7) {
            builder.relatedTransaction(tokens[6].trim());
        }

        return builder.build();
    };
}