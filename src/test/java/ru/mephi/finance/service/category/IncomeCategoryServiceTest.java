package ru.mephi.finance.service.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.repository.category.IncomeCategoryRepository;
import ru.mephi.finance.repository.operation.IncomeOperationRepository;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeCategoryServiceTest {

    @Mock
    private IncomeCategoryRepository categoryRepository;

    @Mock
    private IncomeOperationRepository operationRepository;

    @InjectMocks
    private IncomeCategoryService incomeCategoryService;

    @Test
    void createCategory_Success() {
        String name = "Salary";
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        incomeCategoryService.createCategory(name, null);

        verify(categoryRepository, times(1)).save(any(IncomeCategory.class));
    }

    @Test
    void createCategory_DuplicateName_ThrowsException() {
        String name = "Salary";
        IncomeCategory existing = new IncomeCategory();
        existing.setName(name);

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(existing));

        assertThrows(IllegalArgumentException.class, () -> incomeCategoryService.createCategory(name, null)
        );
    }

    @Test
    void getCategoryById_Success() {
        Integer id = 1;
        IncomeCategory category = new IncomeCategory();
        category.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        IncomeCategory result = incomeCategoryService.getCategoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getCategoryById_NotFound_ThrowsException() {
        Integer id = 1;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> incomeCategoryService.getCategoryById(id)
        );
    }

    @Test
    void deleteCategoryById_Success() {
        Integer id = 1;
        IncomeCategory category = new IncomeCategory();
        category.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        incomeCategoryService.deleteCategoryById(id);

        verify(operationRepository, times(1)).deleteOperationsForCategory(category);
        verify(categoryRepository, times(1)).delete(category);
    }
}
