package ru.mephi.finance.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.ExpenseCategoryService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class MultiUserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseCategoryService categoryService;

    @BeforeEach
    void setUp() {
        userService.deleteAllEntities();
        categoryService.deleteAllEntities();
    }

    @AfterEach
    void tearDown() {
        if (UserService.isUserAuthorized()) {
            userService.deauthorize();
        }
    }

    @Test
    void testDataIsolationBetweenUsers() {
        userService.createUser("user1", "pass1");
        userService.createUser("user2", "pass2");

        userService.authorize("user1", "pass1");
        categoryService.createCategory("User1_Category", BigDecimal.valueOf(100));
        userService.deauthorize(); // Здесь должен сработать dumpData и очистка репозиториев


        userService.authorize("user2", "pass2");
        assertTrue(categoryService.getAllEntities().isEmpty());
        categoryService.createCategory("User2_Category", BigDecimal.valueOf(200));
        userService.deauthorize();

        userService.authorize("user1", "pass1");
        boolean hasUser1Category = categoryService.getAllEntities().stream()
                .anyMatch(c -> c.getName().equals("User1_Category"));
        assertTrue(hasUser1Category, "Данные первого пользователя должны восстановиться");
    }
}
