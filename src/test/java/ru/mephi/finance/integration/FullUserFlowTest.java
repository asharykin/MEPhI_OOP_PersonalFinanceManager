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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class FullUserFlowTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private ExpenseOperationService expenseOperationService;

    @BeforeEach
    void setUp() {
        userService.deleteAllEntities();
        expenseCategoryService.deleteAllEntities();
        expenseOperationService.deleteAllEntities();
    }

    @AfterEach
    void tearDown() {
        if (UserService.isUserAuthorized()) {
            userService.deauthorize();
        }
    }

    @Test
    void testFullUserFlow_CreateCategoryAndAddOperations() {
        // 1. Регистрация нового пользователя
        String username = "integration_user";
        String password = "secure_password";
        userService.createUser(username, password);

        // 2. Авторизация
        // При авторизации UserService вызовет loadData() у всех migratingServices
        userService.authorize(username, password);
        assertTrue(UserService.isUserAuthorized());
        assertEquals(username, UserService.getCurrentUser().getUsername());

        // 3. Создание категории расходов
        String categoryName = "Продукты";
        BigDecimal budget = BigDecimal.valueOf(5000);
        expenseCategoryService.createCategory(categoryName, budget);

        // Получаем созданную категорию (она должна быть первой в списке)
        ExpenseCategory category = expenseCategoryService.getAllEntities()
                .stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElseThrow();

        // 4. Добавление нескольких операций в эту категорию
        expenseOperationService.createOperation(category, BigDecimal.valueOf(1200));
        expenseOperationService.createOperation(category, BigDecimal.valueOf(800));

        // 5. Проверка бизнес-логики: расчет суммы по категории
        BigDecimal totalExpenses = expenseOperationService.getSumByCategory(category);

        // Сравниваем BigDecimal (0 означает, что числа равны)
        assertEquals(0, BigDecimal.valueOf(2000).compareTo(totalExpenses),
                "Сумма операций по категории должна быть 2000");

        // 6. Проверка удаления категории и связанных данных
        // В твоем коде deleteCategoryById удаляет и операции
        expenseCategoryService.deleteCategoryById(category.getId());

        assertTrue(expenseCategoryService.getAllEntities().isEmpty(),
                "Список категорий должен быть пуст после удаления");

        // Проверяем, что в репозитории операций тоже стало пусто (через сервис)
        assertTrue(expenseOperationService.getAllEntities().stream()
                        .noneMatch(op -> op.getCategory().equals(category)),
                "Операции удаленной категории должны быть стерты");
    }
}
