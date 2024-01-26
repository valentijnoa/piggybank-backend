package com.testing.piggybank;

import com.testing.piggybank.account.AccountRepository;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetAccount() {

        long accountId = 1L;
        Account mockAccount = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));


        Optional<Account> result = accountService.getAccount(accountId);


        assertEquals(Optional.of(mockAccount), result);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testGetAccountsByUserId() {

        long userId = 1L;
        when(accountRepository.findAllByUserId(userId)).thenReturn(List.of(new Account(), new Account()));


        List<Account> result = accountService.getAccountsByUserId(userId);


        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testUpdateBalanceCredit() {

        long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        Direction direction = Direction.CREDIT;
        Account mockAccount = new Account();
        mockAccount.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));


        accountService.updateBalance(accountId, amount, direction);


        assertEquals(new BigDecimal("400.00"), mockAccount.getBalance());
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testUpdateBalanceDebit() {

        long accountId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        Direction direction = Direction.DEBIT;
        Account mockAccount = new Account();
        mockAccount.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));


        accountService.updateBalance(accountId, amount, direction);


        assertEquals(new BigDecimal("550.00"), mockAccount.getBalance());
        verify(accountRepository, times(1)).save(mockAccount);
    }
}
