package ru.mephi.finance.cli.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.service.category.IncomeCategoryService;

@Component
public class IncomeCategoryCli extends CategoryCli<IncomeCategory> {

    @Autowired
    public IncomeCategoryCli(IncomeCategoryService categoryService) {
        this.categoryService = categoryService;
        this.typeRoot = "доход";
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете получить список доходных категорий с ID и названием.");
        System.out.println("3.  Здесь вы можете добавить новую доходную категорию, указав название.");
        System.out.println("\tУчтите, что название не должно повторять названий уже существующих категорий.");
        System.out.println("4.  Здесь вы можете изменить название существующей доходной категории.");
        System.out.println("\tУчтите, что название не должно повторять названий других категорий.");
        System.out.println("5.  Здесь вы можете удалить существующую доходную категорию.");
        System.out.println("\tУчтите, что это также приведёт к удалению всех связанных с ней доходов.");
        System.out.println("0.  Здесь вы можете вернуться в главное меню.");
    }

    @Override
    protected String getTableHeader() {
        return String.format("| %-5s | %-20s |", "ID", "Название");
    }

    @Override
    protected String getTableSeparator() {
        return "+-------+----------------------+";
    }
}
