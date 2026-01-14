package ru.mephi.finance.service.operation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.model.operation.IncomeOperation;
import ru.mephi.finance.repository.operation.IncomeOperationRepository;
import ru.mephi.finance.service.category.IncomeCategoryService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeOperationServiceTest {

    @Mock
    private IncomeOperationRepository operationRepository;

    @Mock
    private IncomeCategoryService categoryService;

    @InjectMocks
    private IncomeOperationService incomeOperationService;

    @Test
    void createOperation_Success() {
        IncomeCategory category = new IncomeCategory();
        BigDecimal amount = BigDecimal.valueOf(500);

        incomeOperationService.createOperation(category, amount);

        verify(operationRepository, times(1)).save(any(IncomeOperation.class));
    }

    @Test
    void getSumByCategory_Success() {
        IncomeCategory category = new IncomeCategory();
        category.setId(2);

        IncomeOperation op1 = new IncomeOperation();
        op1.setCategory(category);
        op1.setAmount(BigDecimal.valueOf(1000));

        when(operationRepository.findAll()).thenReturn(List.of(op1));

        BigDecimal result = incomeOperationService.getSumByCategory(category);

        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result));
    }

    @Test
    void processEntityBeforeImport_Success() {
        IncomeCategory category = new IncomeCategory();
        category.setId(2);

        IncomeOperation operation = new IncomeOperation();
        operation.setCategory(category);

        when(categoryService.getCategoryById(2)).thenReturn(category);

        incomeOperationService.processEntityBeforeImport(operation);

        assertEquals(category, operation.getCategory());
        verify(categoryService).getCategoryById(2);
    }
}