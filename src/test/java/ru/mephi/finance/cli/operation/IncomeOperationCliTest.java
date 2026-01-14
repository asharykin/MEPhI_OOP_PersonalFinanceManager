package ru.mephi.finance.cli.operation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.cli.category.IncomeCategoryCli;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.service.category.IncomeCategoryService;
import ru.mephi.finance.service.operation.IncomeOperationService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomeOperationCliTest {

    @Mock
    private IncomeOperationService operationService;

    @Mock
    private IncomeCategoryService categoryService;

    @Mock
    private IncomeCategoryCli categoryCli;

    @InjectMocks
    private IncomeOperationCli incomeOperationCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        incomeOperationCli.setScanner(scanner);
    }
    @Test
    void addOperation_SimpleSuccess() {
        IncomeCategory cat = new IncomeCategory();
        cat.setName("Зарплата");

        when(categoryService.getCategoryById(5)).thenReturn(cat);

        provideInput("5\n1000.00\n");
        incomeOperationCli.addOperation();

        verify(operationService).createOperation(cat, new BigDecimal("1000.00"));
    }
    @Test
    void getOperationSumByCategories_AllCategories() {
        IncomeCategory cat = new IncomeCategory();
        cat.setName("Бизнес");

        when(categoryService.getAllEntities()).thenReturn(List.of(cat));
        when(operationService.getSumByCategory(cat)).thenReturn(new BigDecimal("500.00"));

        provideInput("\n");
        incomeOperationCli.getOperationSumByCategories();

        verify(operationService).getSumByCategory(cat);
    }
    @Test
    void getOperationSumByCategories_SpecificId() {
        IncomeCategory cat = new IncomeCategory();
        cat.setId(10);
        cat.setName("Инвестиции");

        when(categoryService.getCategoryById(10)).thenReturn(cat);
        when(operationService.getSumByCategory(cat)).thenReturn(new BigDecimal("200.00"));

        provideInput("10\n");
        incomeOperationCli.getOperationSumByCategories();

        verify(categoryService).getCategoryById(10);
        verify(operationService).getSumByCategory(cat);
    }
}

