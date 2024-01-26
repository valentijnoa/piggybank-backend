package com.testing.piggybank;

import com.testing.piggybank.model.Currency;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionServiceAPITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetTransactionsEndpoint() {
        long accountId = 1;
        int limit = 5;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/transactions/{accountId}?limit={limit}",
                String.class,
                accountId, limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFilterAndLimitTransactionsEndpoint() {
        long accountId = 1;
        int limit = 5;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/transactions/filter-transactions?accountId={accountId}&limit={limit}",
                String.class,
                accountId, limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCreateTransactionEndpoint() {
        CreateTransactionRequest request = new CreateTransactionRequest();

        request.setSenderAccountId(1);
        request.setReceiverAccountId(2);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency(Currency.valueOf("EURO"));
        request.setDescription("Test transaction");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/transactions",
                request,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
}
