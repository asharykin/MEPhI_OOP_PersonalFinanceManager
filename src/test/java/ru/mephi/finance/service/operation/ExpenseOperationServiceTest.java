package ru.mephi.finance.service.operation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.model.operation.ExpenseOperation;
import ru.mephi.finance.repository.operation.ExpenseOperationRepository;
import ru.mephi.finance.service.category.ExpenseCategoryService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseOperationServiceTest {

    @Mock
    private ExpenseOperationRepository operationRepository;

    @Mock
    private ExpenseCategoryService categoryService;

    @InjectMocks
    private ExpenseOperationService expenseOperationService;

    @Test
    void createOperation_Success() {
        ExpenseCategory category = new ExpenseCategory();
        BigDecimal amount = BigDecimal.valueOf(100);

        expenseOperationService.createOperation(category, amount);

        verify(operationRepository, times(1)).save(any(ExpenseOperation.class));
    }

    @Test
    void getSumByCategory_Success() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);

        ExpenseOperation op1 = new ExpenseOperation();
        op1.setCategory(category);
        op1.setAmount(BigDecimal.valueOf(100));

        ExpenseOperation op2 = new ExpenseOperation();
        op2.setCategory(category);
        op2.setAmount(BigDecimal.valueOf(50));

        when(operationRepository.findAll()).thenReturn(List.of(op1, op2));

        BigDecimal result = expenseOperationService.getSumByCategory(category);

        assertEquals(0, BigDecimal.valueOf(150).compareTo(result));
    }

    @Test
    void processEntityBeforeImport_Success() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);

        ExpenseOperation operation = new ExpenseOperation();
        operation.setCategory(category);

        when(categoryService.getCategoryById(1)).thenReturn(category);

        expenseOperationService.processEntityBeforeImport(operation);

        assertEquals(category, operation.getCategory());
        verify(categoryService).getCategoryById(1);
    }

    @Test
    void getAllEntities_Success() {
        ExpenseOperation operation = new ExpenseOperation();

        when(operationRepository.findAll()).thenReturn(List.of(operation));
        Collection<ExpenseOperation> result = expenseOperationService.getAllEntities();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(operationRepository, times(1)).findAll();
    }

    @Test
    void deleteAllEntities_Success() {
        expenseOperationService.deleteAllEntities();

        verify(operationRepository, times(1)).deleteAll();
    }
}
