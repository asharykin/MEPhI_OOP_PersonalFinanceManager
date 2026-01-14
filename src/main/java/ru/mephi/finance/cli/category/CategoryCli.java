package ru.mephi.finance.cli.category;

import ru.mephi.finance.cli.BaseCli;
import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.service.category.CategoryService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.NoSuchElementException;

public abstract class CategoryCli<C extends Category> extends BaseCli {
    protected CategoryService<C> categoryService;
    protected String typeRoot;

    public void manageCategories() {
        boolean managingCategories = true;

        while (managingCategories) {
            try {
                printMenu();
                int choice = readIntInput();
                System.out.println();
                switch (choice) {
                    case 1 -> printHelp();
                    case 2 -> listCategories();
                    case 3 -> addCategory();
                    case 4 -> updateCategory();
                    case 5 -> deleteCategory();
                    case 0 -> managingCategories = false;
                    default -> System.out.println("Недопустимый выбор. Введите номер одного из пунктов списка.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void printMenu() {
        System.out.printf("%n===== %sНЫЕ КАТЕГОРИИ ===== %n", typeRoot.toUpperCase());
        System.out.printf("1.  Помощь %n");
        System.out.printf("2.  Список %sных категорий %n", typeRoot);
        System.out.printf("3.  Добавить %sную категорию %n", typeRoot);
        System.out.printf("4.  Редактировать %sную категорию %n", typeRoot);
        System.out.printf("5.  Удалить %sную категорию %n", typeRoot);
        System.out.printf("0.  Вернуться в главное меню %n");
        System.out.print("Введите ваш выбор: ");
    }

    public void listCategories() {
        Collection<C> categories = categoryService.getAllEntities();

        if (categories.isEmpty()) {
            String message = String.format("Никаких %sных категорий не найдено. Добавьте новую через меню.", typeRoot);
            throw new NoSuchElementException(message);
        }

        System.out.printf("Список %sных категорий: %n", typeRoot);
        printCategories(categories);
    }

    public void addCategory() {
        String name = getName();
        BigDecimal budget = getBudget();

        categoryService.createCategory(name, budget);
        System.out.printf("Новая %sная категория успешно добавлена. %n", typeRoot);
    }

    public void updateCategory() {
        listCategories();
        System.out.println();

        System.out.print("Выберите из списка ID категории, которую хотите отредактировать: ");
        Integer id = readIntInput();
        C category = categoryService.getCategoryById(id);

        setNameIfNeeded(category);
        setBudgetIfNeeded(category);
    }

    public void deleteCategory() {
        listCategories();
        System.out.println();

        System.out.print("Выберите из списка ID категории, которую хотите удалить: ");
        Integer id = readIntInput();

        categoryService.deleteCategoryById(id);
        System.out.println("Категория и все связанные с ней операции успешно удалены.");
    }

    private void printCategories(Collection<C> categories) {
        String header = getTableHeader();
        String separator = getTableSeparator();

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (C category : categories) {
            System.out.println(category);
        }
        System.out.println(separator);
    }

    protected abstract String getTableHeader();

    protected abstract String getTableSeparator();

    private String getName() {
        System.out.printf("Введите название новой %sной категории: ", typeRoot);
        return readNonEmptyStringInput("Название категории");
    }

    protected BigDecimal getBudget() {
        return null;
    }

    private void setNameIfNeeded(C category) {
        System.out.printf("Хотите ли вы изменить название этой %sной категории (да/нет): ", typeRoot);

        if (readPositiveReply()) {
            System.out.print("Введите новое название: ");
            String name = readNonEmptyStringInput("Название категории");

            categoryService.checkCategoryName(name);
            category.setName(name);
            System.out.println("Название успешно изменено.");
        } else {
            System.out.println("Название оставлено прежним.");
        }
    }

    protected void setBudgetIfNeeded(C category) {
    }
}
