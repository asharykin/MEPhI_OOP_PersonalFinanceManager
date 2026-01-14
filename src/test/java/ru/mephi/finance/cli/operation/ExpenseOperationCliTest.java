package ru.mephi.finance.cli.operation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.cli.category.ExpenseCategoryCli;
import ru.mephi.finance.model.User;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.ExpenseCategoryService;
import ru.mephi.finance.service.operation.ExpenseOperationService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseOperationCliTest {

    @Mock
    private ExpenseOperationService operationService;

    @Mock
    private ExpenseCategoryService categoryService;

    @Mock
    private ExpenseCategoryCli categoryCli;

    @Mock
    private User mockUser;

    @Mock
    private Wallet mockWallet;

    @InjectMocks
    private ExpenseOperationCli expenseOperationCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        expenseOperationCli.setScanner(scanner);
    }

    @Test
    void addOperation_ShouldCreateOperation_WhenNoWarnings() {
        ExpenseCategory cat = new ExpenseCategory();
        cat.setBudget(new BigDecimal("1000.00"));

        when(categoryService.getCategoryById(1)).thenReturn(cat);
        when(operationService.getSumByCategory(cat)).thenReturn(BigDecimal.ZERO);

        try (MockedStatic<UserService> userServiceMock = mockStatic(UserService.class)) {
            userServiceMock.when(UserService::getCurrentUser).thenReturn(mockUser);
            when(mockUser.getWallet()).thenReturn(mockWallet);
            when(mockWallet.getTotalExpense()).thenReturn(BigDecimal.ZERO);
            when(mockWallet.getTotalIncome()).thenReturn(new BigDecimal("5000.00"));

            provideInput("1\n100.00\n");
            expenseOperationCli.addOperation();

            verify(operationService).createOperation(cat, new BigDecimal("100.00"));
        }
    }
    @Test
    void addOperation_ShouldAbort_WhenBudgetExceededAndUserSaysNo() {
        ExpenseCategory cat = new ExpenseCategory();
        cat.setBudget(new BigDecimal("100.00"));

        when(categoryService.getCategoryById(1)).thenReturn(cat);
        when(operationService.getSumByCategory(cat)).thenReturn(new BigDecimal("90.00"));

        provideInput("1\n20.00\nнет\n");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> expenseOperationCli.addOperation());
        assertEquals("Добавление расхода прервано.", ex.getMessage());

        verify(operationService, never()).createOperation(any(), any());
    }
}