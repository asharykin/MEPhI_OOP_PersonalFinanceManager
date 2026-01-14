package ru.mephi.finance.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.ExpenseCategoryService;
import ru.mephi.finance.service.operation.ExpenseOperationService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class BudgetOperationIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseCategoryService categoryService;

    @Autowired
    private ExpenseOperationService operationService;

    @BeforeEach
    void setUp() {
        userService.deleteAllEntities();
        categoryService.deleteAllEntities();
        operationService.deleteAllEntities();
    }

    @AfterEach
    void tearDown() {
        if (UserService.isUserAuthorized()) {
            userService.deauthorize();
        }
    }

    @Test
    void testComplexBudgetCalculations() {
        userService.createUser("finance_admin", "admin");
        userService.authorize("finance_admin", "admin");

        categoryService.createCategory("Еда", BigDecimal.valueOf(10000));
        categoryService.createCategory("Такси", BigDecimal.valueOf(5000));

        ExpenseCategory food = categoryService.getAllEntities().stream()
                .filter(c -> c.getName().equals("Еда")).findFirst().get();
        ExpenseCategory taxi = categoryService.getAllEntities().stream()
                .filter(c -> c.getName().equals("Такси")).findFirst().get();

        operationService.createOperation(food, BigDecimal.valueOf(1500));
        operationService.createOperation(food, BigDecimal.valueOf(2500));
        operationService.createOperation(taxi, BigDecimal.valueOf(600));

        assertEquals(0, BigDecimal.valueOf(4000).compareTo(operationService.getSumByCategory(food)));
        assertEquals(0, BigDecimal.valueOf(600).compareTo(operationService.getSumByCategory(taxi)));

        assertEquals(3, operationService.getAllEntities().size());
    }
}
