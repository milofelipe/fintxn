import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FinTxnServiceTests {
    private FinTxnRepository finTxnRepository;
    private FinTxnService finTxnService;

    @Before
    public void setup() {
        finTxnRepository = new FinTxnRepository();
        finTxnService = new FinTxnService(finTxnRepository);
    }

    @Test
    public void processTransactionFile() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("transactions.csv");
        List<Transaction> transactions = finTxnService.processTransactionFile(url.getPath());

        Assert.assertEquals(5, transactions.size());
    }

    @Test
    public void analyse() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("transactions.csv");
        finTxnService.loadTransactionsFromCsvFile(url.getPath());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        FinTxnAnalysisResult finTxnAnalysisResult = finTxnService.analyse("ACC334455",
                LocalDateTime.parse("20/10/2018 12:00:00", dateTimeFormatter),
                LocalDateTime.parse("20/10/2018 19:00:00", dateTimeFormatter));

        Assert.assertEquals(new BigDecimal("-25.00"), finTxnAnalysisResult.getBalance());
        Assert.assertEquals(1, finTxnAnalysisResult.getNumberOfTransactions());
    }
}