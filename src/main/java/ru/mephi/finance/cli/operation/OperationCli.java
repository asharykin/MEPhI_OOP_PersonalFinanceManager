package ru.mephi.finance.cli.operation;

import ru.mephi.finance.cli.BaseCli;
import ru.mephi.finance.cli.category.CategoryCli;
import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.model.operation.Operation;
import ru.mephi.finance.service.category.CategoryService;
import ru.mephi.finance.service.operation.OperationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class OperationCli<O extends Operation<C>, C extends Category> extends BaseCli {
    protected OperationService<O, C> operationService;
    protected CategoryService<C> categoryService;
    protected CategoryCli<C> categoryCli;
    protected String typeRoot;

    public void manageOperations() {
        boolean managingOperations = true;

        while (managingOperations) {
            try {
                printMenu();
                int choice = readIntInput();
                System.out.println();
                switch (choice) {
                    case 1 -> printHelp();
                    case 2 -> listOperations();
                    case 3 -> addOperation();
                    case 4 -> getTotalOperationSum();
                    case 5 -> getOperationSumByCategories();
                    case 0 -> managingOperations = false;
                    default -> System.out.println("Недопустимый выбор. Введите номер одного из пунктов списка.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void printMenu() {
        System.out.printf("%n===== %sЫ ===== %n", typeRoot.toUpperCase());
        System.out.printf("1.  Помощь %n");
        System.out.printf("2.  Список %sов %n", typeRoot);
        System.out.printf("3.  Добавить новый %s %n", typeRoot);
        System.out.printf("4.  Общий %s %n", typeRoot);
        System.out.printf("5.  Общие %sы по категориям %n", typeRoot);
        System.out.printf("0.  Вернуться в главное меню %n");
        System.out.print("Введите ваш выбор: ");
    }

    public void listOperations() {
        Collection<O> operations = operationService.getAllEntities();

        if (operations.isEmpty()) {
            String message = String.format("Никаких %sов не найдено. Добавьте новый через меню.", typeRoot);
            throw new NoSuchElementException(message);
        }

        System.out.printf("Список %sов: %n", typeRoot);
        printOperations(operations);
    }

    public void addOperation() {
        categoryCli.listCategories();
        System.out.println();

        System.out.printf("Выберите из списка ID категории, к которой будет относиться данный %s: ", typeRoot);
        Integer categoryId = readIntInput();
        C category = categoryService.getCategoryById(categoryId);

        System.out.printf("Введите сумму %sа: ", typeRoot);
        BigDecimal amount = readBigDecimalInput();

        validateOperationAmountIfNeeded(amount, category);

        operationService.createOperation(category, amount);
        System.out.printf("Новый %s успешно добавлен. %n", typeRoot);
    }

    public abstract void getTotalOperationSum();

    public void getOperationSumByCategories() {
        categoryCli.listCategories();
        System.out.println();

        System.out.println("Далее вам нужно будет или ввести через запятую ID категорий из списка, " +
                           "или оставить ввод пустым, если хотите рассчитать суммы по всем категориям. ");
        System.out.println("Если вас интересует только одна категория, то запятые можно не использовать.");
        System.out.println();

        System.out.printf("Выберите ID категорий, по которым вы хотите рассчитать сумму %sов: ", typeRoot);
        String categoryIdsStr = scanner.nextLine().trim();
        System.out.println();

        if (categoryIdsStr.isEmpty()) {
            for (C category : categoryService.getAllEntities()) {
                BigDecimal sum = operationService.getSumByCategory(category);
                printOperationSumByCategory(category, sum);
            }
            return;
        }

        List<Integer> categoryIds = getCategoryIds(categoryIdsStr);

        for (Integer id : categoryIds) {
            C category = categoryService.getCategoryById(id);
            BigDecimal sum = operationService.getSumByCategory(category);
            printOperationSumByCategory(category, sum);
        }
    }

    private void printOperations(Collection<O> operations) {
        String header = String.format("| %-5s | %-20s | %12s |", "ID", "Категория", "Сумма");
        String separator = "+-------+----------------------+--------------+";

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (O operation : operations) {
            System.out.println(operation);
        }
        System.out.println(separator);
    }

    protected void validateOperationAmountIfNeeded(BigDecimal amount, C category) {
    }

    protected void printOperationSumByCategory(C category, BigDecimal sum) {
        System.out.printf("%s: %.2f", category.getName(), sum);
    }

    private List<Integer> getCategoryIds(String categoryIdsStr) {
        List<Integer> categoryIds = new ArrayList<>();
        for (String idStr : categoryIdsStr.split("\\s*,\\s*")) {
            try {
                Integer id = Integer.parseInt(idStr);
                categoryIds.add(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Требуется ввести целочисленные ID, разделяя их запятыми.");
            }
        }
        return categoryIds;
    }
}

