package com.testing.piggybank;

import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.helper.CurrencyConverterService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Currency;
import com.testing.piggybank.model.Direction;
import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.CreateTransactionRequest;
import com.testing.piggybank.transaction.TransactionRepository;
import com.testing.piggybank.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyConverterService converterService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactions_shouldReturnFilteredAndLimitedTransactions() {

        long accountId = 1L;
        int limit = 5;

        List<Transaction> allTransactions = createMockTransactions();
        when(transactionRepository.findAll()).thenReturn(allTransactions);


        List<Transaction> result = transactionService.getTransactions(limit, accountId);


        assertEquals(limit, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getTransactions_shouldReturnAllTransactionsWhenLimitIsNull() {

        long accountId = 1L;

        List<Transaction> allTransactions = createMockTransactions();
        when(transactionRepository.findAll()).thenReturn(allTransactions);


        List<Transaction> result = transactionService.getTransactions(null, accountId);


        assertEquals(allTransactions.size(), result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void createTransaction_shouldThrowExceptionForInvalidSenderAccount() {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setSenderAccountId(1L);
        request.setReceiverAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency(Currency.valueOf("EURO"));
        request.setDescription("Test");

        when(accountService.getAccount(request.getSenderAccountId())).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> transactionService.createTransaction(request));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_shouldThrowExceptionForInvalidReceiverAccount() {

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setSenderAccountId(1L);
        request.setReceiverAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency(Currency.valueOf("EURO"));
        request.setDescription("Test");

        Account senderAccount = new Account();
        senderAccount.setId(1L);
        when(accountService.getAccount(request.getSenderAccountId())).thenReturn(Optional.of(senderAccount));
        when(accountService.getAccount(request.getReceiverAccountId())).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> transactionService.createTransaction(request));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    private List<Transaction> createMockTransactions() {
        List<Transaction> transactions = new ArrayList<>();


        Account account1 = new Account();
        account1.setId(1L);

        Account account2 = new Account();
        account2.setId(2L);


        Transaction transaction1 = new Transaction();
        transaction1.setAmount(new BigDecimal("50.00"));
        transaction1.setCurrency(Currency.valueOf("USD"));
        transaction1.setDescription("Mock Transaction 1");
        transaction1.setSenderAccount(account1);
        transaction1.setReceiverAccount(account2);
        transaction1.setDateTime(Instant.parse("2022-01-01T12:00:00Z"));

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(new BigDecimal("75.00"));
        transaction2.setCurrency(Currency.valueOf("EURO"));
        transaction2.setDescription("Mock Transaction 2");
        transaction2.setSenderAccount(account2);
        transaction2.setReceiverAccount(account1);
        transaction2.setDateTime(Instant.parse("2022-02-01T14:30:00Z"));


        transactions.add(transaction1);
        transactions.add(transaction2);

        return transactions;
    }
}
