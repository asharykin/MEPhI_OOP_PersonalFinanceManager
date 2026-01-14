package ru.mephi.finance.cli.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.service.category.ExpenseCategoryService;

import java.math.BigDecimal;

@Component
public class ExpenseCategoryCli extends CategoryCli<ExpenseCategory> {

    @Autowired
    public ExpenseCategoryCli(ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
        this.typeRoot = "расход";
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете получить список расходных категорий с ID, названием и бюджетом.");
        System.out.println("3.  Здесь вы можете добавить новую расходную категорию, указав название и бюджет.");
        System.out.println("\tУчтите, что название не должно повторять названий уже существующих категорий, " +
                           "а бюджет должен быть положительным.");
        System.out.println("4.  Здесь вы можете изменить название и/или бюджет существующей расходной категории.");
        System.out.println("\tУчтите, что новое название не должно повторять названий других категорий, " +
                           "а новый бюджет должен быть положительным.");
        System.out.println("5.  Здесь вы можете удалить существующую расходную категорию.");
        System.out.println("\tУчтите, что это также приведёт к удалению всех связанных с ней расходов.");
        System.out.println("0.  Здесь вы можете вернуться в главное меню.");
    }

    @Override
    protected String getTableHeader() {
        return String.format("| %-5s | %-20s | %12s |", "ID", "Название", "Бюджет");
    }

    @Override
    protected String getTableSeparator() {
        return "+-------+----------------------+--------------+";
    }

    @Override
    protected BigDecimal getBudget() {
        System.out.print("Введите бюджет для новой расходной категории: ");
        return readBigDecimalInput();
    }

    @Override
    protected void setBudgetIfNeeded(ExpenseCategory category) {
        System.out.print("Хотите ли вы установить новый бюджет для этой расходной категории (да/нет): ");

        if (readPositiveReply()) {
            System.out.print("Введите новый бюджет: ");
            BigDecimal budget = readBigDecimalInput();

            category.setBudget(budget);
            System.out.println("Бюджет успешно изменён.");
        } else {
            System.out.println("Бюджет оставлен прежним.");
        }
    }
}
