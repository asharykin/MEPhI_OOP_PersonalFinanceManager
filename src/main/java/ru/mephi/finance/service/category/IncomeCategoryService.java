package ru.mephi.finance.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.repository.category.IncomeCategoryRepository;
import ru.mephi.finance.repository.operation.IncomeOperationRepository;

import java.math.BigDecimal;

@Service
@Order(1)
@Qualifier("migratingService")
public class IncomeCategoryService extends CategoryService<IncomeCategory> {

    @Autowired
    public IncomeCategoryService(IncomeCategoryRepository categoryRepository,
                                 IncomeOperationRepository operationRepository) {
        this.repository = categoryRepository;
        this.operationRepository = operationRepository;
        this.subtypeStr = "incomes";
    }

    @Override
    public void createCategory(String name, BigDecimal budget) {
        checkCategoryName(name);
        IncomeCategory category = new IncomeCategory();
        category.setId(++index);
        category.setName(name);
        repository.save(category);
    }
}
