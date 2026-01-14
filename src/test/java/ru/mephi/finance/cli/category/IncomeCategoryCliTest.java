package ru.mephi.finance.cli.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.service.category.IncomeCategoryService;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeCategoryCliTest {

    @Mock
    private IncomeCategoryService categoryService;

    @InjectMocks
    private IncomeCategoryCli incomeCategoryCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        incomeCategoryCli.setScanner(scanner);
    }

    @Test
    void listCategories_ShouldPrintData_WhenCategoriesExist() {
        IncomeCategory category = new IncomeCategory();
        category.setId(1);
        category.setName("Зарплата");

        when(categoryService.getAllEntities()).thenReturn(List.of(category));

        assertDoesNotThrow(() -> incomeCategoryCli.listCategories());
        verify(categoryService).getAllEntities();
    }
    @Test
    void listCategories_ShouldThrowException_WhenNoCategoriesFound() {
        when(categoryService.getAllEntities()).thenReturn(Collections.emptyList());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> incomeCategoryCli.listCategories());
        assertTrue(exception.getMessage().contains("Никаких доходных категорий не найдено"));
    }
    @Test
    void addCategory_ShouldCallServiceWithNullBudget() {
        provideInput("Фриланс\n");
        incomeCategoryCli.addCategory();

        verify(categoryService).createCategory("Фриланс", null);
    }
    @Test
    void updateCategory_ShouldOnlyUpdateName() {
        IncomeCategory category = new IncomeCategory();
        category.setId(2);
        category.setName("Старое имя");

        when(categoryService.getAllEntities()).thenReturn(List.of(category));
        when(categoryService.getCategoryById(2)).thenReturn(category);

        provideInput("2\nда\nНовое имя\n");
        incomeCategoryCli.updateCategory();

        verify(categoryService).checkCategoryName("Новое имя");
        assertEquals("Новое имя", category.getName());
    }
    @Test
    void updateCategory_ShouldDoNothing_WhenUserSaysNo() {
        IncomeCategory category = new IncomeCategory();
        category.setId(3);
        category.setName("Постоянное имя");

        when(categoryService.getAllEntities()).thenReturn(List.of(category));
        when(categoryService.getCategoryById(3)).thenReturn(category);

        provideInput("3\nнет\n");
        incomeCategoryCli.updateCategory();

        verify(categoryService, never()).checkCategoryName(anyString());
        assertEquals("Постоянное имя", category.getName());
    }
    @Test
    void deleteCategory_ShouldCallServiceWithCorrectId() {
        IncomeCategory category = new IncomeCategory();
        category.setId(10);
        category.setName("Категория");
        
        when(categoryService.getAllEntities()).thenReturn(List.of(category));

        provideInput("10\n");
        incomeCategoryCli.deleteCategory();
        verify(categoryService).deleteCategoryById(10);
    }
}
