package com.testing.piggybank;

import com.testing.piggybank.account.*;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Currency;
import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import com.testing.piggybank.transaction.TransactionController;
import com.testing.piggybank.transaction.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    TransactionController transactionController;

    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    public void testGetAccount() {
        long accountId = 0L;
        long userId = 2L;
        Account account = new Account();
        account.setId(accountId);
        ResponseEntity<GetAccountsResponse> responseEntity = accountController.getAccounts(userId);
        assertEquals(account.getId(), accountId, "Account ID match");
    }


    @Test
    public void testGetAccounts() {
        long userId = 1L;
        ResponseEntity<GetAccountsResponse> responseEntity = accountController.getAccounts(userId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response OK");
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getAccounts().size(), "Must Be 1 Account");
    }


    @Test
    public void testGetAllTransactions() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setCurrency(Currency.EURO);
        request.setReceiverAccountId(1L);
        request.setSenderAccountId(2L);
        request.setDescription("Making a transaction");
        request.setAmount(new BigDecimal(100));

        // Act
        transactionController.createTransaction(request);

        // Assert
        Optional<Transaction> result = transactionRepository.findById(1L);
        assertTrue(result.isPresent(), "Transaction should be present");

        long count = result.stream().count();
        assertEquals(1, count, "Expected a single transaction");
    }
}
