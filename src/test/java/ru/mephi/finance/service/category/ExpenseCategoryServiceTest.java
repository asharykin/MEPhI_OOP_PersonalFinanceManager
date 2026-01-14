package ru.mephi.finance.service.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.repository.category.ExpenseCategoryRepository;
import ru.mephi.finance.repository.operation.ExpenseOperationRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceTest {

    @Mock
    private ExpenseCategoryRepository categoryRepository;

    @Mock
    private ExpenseOperationRepository operationRepository;

    @InjectMocks
    private ExpenseCategoryService expenseCategoryService;

    @Test
    void createCategory_Success() {
        String name = "Food";
        BigDecimal budget = BigDecimal.valueOf(100);
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        expenseCategoryService.createCategory(name, budget);

        verify(categoryRepository, times(1)).save(any(ExpenseCategory.class));
    }

    @Test
    void createCategory_DuplicateName_ThrowsException() {
        String name = "Food";
        ExpenseCategory existing = new ExpenseCategory();
        existing.setName(name);
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(existing));

        assertThrows(IllegalArgumentException.class, () -> expenseCategoryService.createCategory(name, BigDecimal.TEN)
        );
    }

    @Test
    void getCategoryById_Success() {
        Integer id = 1;
        ExpenseCategory category = new ExpenseCategory();
        category.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        ExpenseCategory result = expenseCategoryService.getCategoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getCategoryById_NotFound_ThrowsException() {
        Integer id = 1;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> expenseCategoryService.getCategoryById(id)
        );
    }

    @Test
    void deleteCategoryById_Success() {
        Integer id = 1;
        ExpenseCategory category = new ExpenseCategory();
        category.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        expenseCategoryService.deleteCategoryById(id);

        verify(operationRepository, times(1)).deleteOperationsForCategory(category);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void getAllEntities_Success() {
        ExpenseCategory category = new ExpenseCategory();
        category.setId(1);
        category.setName("Food");
        category.setBudget(BigDecimal.valueOf(100));

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        Collection<ExpenseCategory> result = expenseCategoryService.getAllEntities();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void deleteAllEntities_Success() {
        expenseCategoryService.deleteAllEntities();

        verify(categoryRepository, times(1)).deleteAll();
    }
}
