package ru.mephi.finance.cli.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.service.category.ExpenseCategoryService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryCliTest {

    @Mock
    private ExpenseCategoryService categoryService;

    @InjectMocks
    private ExpenseCategoryCli expenseCategoryCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        expenseCategoryCli.setScanner(scanner);
    }

    @Test
    void listCategories_ShouldPrintCategories_WhenNotEmpty() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);
        category.setName("Еда");
        category.setBudget(new BigDecimal("500"));

        when(categoryService.getAllEntities()).thenReturn(List.of(category));

        assertDoesNotThrow(() -> expenseCategoryCli.listCategories());
        verify(categoryService).getAllEntities();
    }
    @Test
    void listCategories_ShouldThrowException_WhenEmpty() {
        when(categoryService.getAllEntities()).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> expenseCategoryCli.listCategories());
    }
    @Test
    void addCategory_ShouldCallServiceWithCorrectParams() {
        provideInput("Такси\n150.00\n");
        expenseCategoryCli.addCategory();

        verify(categoryService).createCategory("Такси", new BigDecimal("150.00"));
    }
    @Test
    void updateCategory_ShouldUpdateBothNameAndBudget() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);
        category.setName("Старая");
        category.setBudget(new BigDecimal("100"));

        when(categoryService.getAllEntities()).thenReturn(List.of(category));
        when(categoryService.getCategoryById(1)).thenReturn(category);

        provideInput("1\nда\nНовая\nда\n200.00\n");
        expenseCategoryCli.updateCategory();

        verify(categoryService).checkCategoryName("Новая");
        assertEquals("Новая", category.getName());
        assertEquals(new BigDecimal("200.00"), category.getBudget());
    }
    @Test
    void deleteCategory_ShouldCallService_WhenConfirmed() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);
        category.setName("Категория");
        category.setBudget(new BigDecimal("1000"));

        when(categoryService.getAllEntities()).thenReturn(List.of(category));

        provideInput("1\n");
        expenseCategoryCli.deleteCategory();

        verify(categoryService).deleteCategoryById(1);
    }
}
