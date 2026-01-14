package ru.mephi.finance.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.finance.cli.category.ExpenseCategoryCli;
import ru.mephi.finance.cli.category.IncomeCategoryCli;
import ru.mephi.finance.cli.operation.ExpenseOperationCli;
import ru.mephi.finance.cli.operation.IncomeOperationCli;
import ru.mephi.finance.service.UserService;

@Component
public class SessionCli extends BaseCli {
    private final IncomeCategoryCli incomeCategoryCli;
    private final ExpenseCategoryCli expenseCategoryCli;
    private final IncomeOperationCli incomeOperationCli;
    private final ExpenseOperationCli expenseOperationCli;
    private final MigrationCli migrationCli;
    private final UserService userService;

    @Autowired
    public  SessionCli(IncomeCategoryCli incomeCategoryCli, ExpenseCategoryCli expenseCategoryCli,
                       IncomeOperationCli incomeOperationCli, ExpenseOperationCli expenseOperationCli,
                       MigrationCli migrationCli, UserService userService) {
        this.incomeCategoryCli = incomeCategoryCli;
        this.expenseCategoryCli = expenseCategoryCli;
        this.incomeOperationCli = incomeOperationCli;
        this.expenseOperationCli = expenseOperationCli;
        this.migrationCli = migrationCli;
        this.userService = userService;
    }

    public void manageSession() {
        boolean authorized = true;

        while (authorized) {
            try {
                printMenu();
                int choice = readIntInput();
                switch (choice) {
                    case 1 -> printHelp();
                    case 2 -> incomeCategoryCli.manageCategories();
                    case 3 -> expenseCategoryCli.manageCategories();
                    case 4 -> incomeOperationCli.manageOperations();
                    case 5 -> expenseOperationCli.manageOperations();
                    case 6 -> migrationCli.manageMigration();
                    case 0 -> {
                        logOut();
                        authorized = false;
                    }
                    default -> System.out.println("Недопустимый выбор. Введите номер одного из пунктов списка.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void printMenu() {
        System.out.println("\n===== ГЛАВНОЕ МЕНЮ =====");
        System.out.println("1.  Помощь");
        System.out.println("2.  Доходные категории");
        System.out.println("3.  Расходные категории");
        System.out.println("4.  Доходы");
        System.out.println("5.  Расходы");
        System.out.println("6.  Экспорт/импорт данных");
        System.out.println("0.  Выйти");
        System.out.print("Введите ваш выбор: ");
    }

    @Override
    protected void printHelp() {
        System.out.println();
        System.out.println("2.  Здесь вы можете перейти в раздел для управления доходными категориями.");
        System.out.println("3.  Здесь вы можете перейти в раздел для управления расходными категориями.");
        System.out.println("4.  Здесь вы можете перейти в раздел для управления доходами.");
        System.out.println("5.  Здесь вы можете перейти в раздел для управления расходами.");
        System.out.println("6.  Здесь вы можете перейти в раздел для экспорта и импорта данных пользователя.");
        System.out.println("0.  Здесь вы можете выйти из учётной записи с сохранением данных пользователя на диск.");
    }

    public void logOut() {
        userService.deauthorize();
    }
}
