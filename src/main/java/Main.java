import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        validateInput(args);

        FinTxnRepository finTxnRepository = new FinTxnRepository();
        FinTxnService finTxnService = new FinTxnService(finTxnRepository);

        finTxnService.loadTransactionsFromCsvFile(args[0]);

        System.out.println("Financial Transactions Analysis Tool");
        System.out.println("====================================");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            TransactionQueryInput queryInput = getTransactionQueryInput(scanner);
            FinTxnAnalysisResult finTxnAnalysisResult = finTxnService.analyse(queryInput.getAccountId(), queryInput.getFromDateTime(), queryInput.getToDateTime());
            printResults(finTxnAnalysisResult);

            System.out.println("Quit [Y/n]?");
            String quit = scanner.nextLine();
            if ("Y".equals(quit)) {
                System.exit(0);
            }
        }
    }

    private static TransactionQueryInput getTransactionQueryInput(Scanner scanner) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String accountId = null;
        while (accountId == null || accountId.equals("")) {
            System.out.println("Please enter the Account ID: ");
            accountId = scanner.nextLine();
        }

        LocalDateTime fromDateTime = null;
        while (fromDateTime == null) {
            try {
                System.out.println("Please enter the from date/time (dd/MM/yyyy HH:mm:ss): ");
                fromDateTime = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid from date/time value entered.");
            }
        }

        LocalDateTime toDateTime = null;
        while (toDateTime == null) {
            try {
                System.out.println("Please enter the to date/time (dd/MM/yyyy HH:mm:ss): ");
                toDateTime = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid to date/time value entered.");
            }
        }

        return new TransactionQueryInput(accountId, fromDateTime, toDateTime);
    }

    private static void printResults(FinTxnAnalysisResult finTxnAnalysisResult) {
        DecimalFormat decimalFormat = new DecimalFormat("$#,##0.00;-$#,##0.00");

        System.out.println();
        System.out.println(String.format("Relative balance for the period is: %s", decimalFormat.format(finTxnAnalysisResult.getBalance())));
        System.out.println(String.format("Number of transactions included is: %d", finTxnAnalysisResult.getNumberOfTransactions()));
        System.out.println("*****");
        System.out.println();
    }

    private static void validateInput(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: Please provide the location of the CSV input file.");
            System.exit(0);
        }
    }
}