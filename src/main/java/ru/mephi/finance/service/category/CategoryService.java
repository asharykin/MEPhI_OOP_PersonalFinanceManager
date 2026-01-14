package ru.mephi.finance.service.category;

import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.model.operation.Operation;
import ru.mephi.finance.repository.operation.OperationRepository;
import ru.mephi.finance.service.EntityService;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public abstract class CategoryService<C extends Category> extends EntityService<C> {
    protected OperationRepository<? extends Operation<C>, C> operationRepository;

    protected CategoryService() {
        this.typeStr = "categories";
    }

    public abstract void createCategory(String name, BigDecimal budget);

    public C getCategoryById(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String message = String.format("Категории с ID %d не существует. Выберите категорию из списка.", id);
            return new NoSuchElementException(message);
        });
    }

    public void deleteCategoryById(Integer id) {
        C category = getCategoryById(id);
        operationRepository.deleteOperationsForCategory(category);
        repository.delete(category);
    }

    public void checkCategoryName(String name) {
        for (C category : getAllEntities()) {
            if (category.getName().equals(name)) {
                String message = String.format("Категория с названием '%s' уже существует. Задайте другое.", name);
                throw new IllegalArgumentException(message);
            }
        }
    }
}
