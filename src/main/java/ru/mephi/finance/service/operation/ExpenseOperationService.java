package ru.mephi.finance.service.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.model.operation.ExpenseOperation;
import ru.mephi.finance.repository.operation.ExpenseOperationRepository;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.ExpenseCategoryService;

import java.math.BigDecimal;

@Service
@Order(4)
@Qualifier("migratingService")
public class ExpenseOperationService extends OperationService<ExpenseOperation, ExpenseCategory> {

    @Autowired
    public ExpenseOperationService(ExpenseOperationRepository operationRepository,
                                   ExpenseCategoryService categoryService) {
        this.repository = operationRepository;
        this.categoryService = categoryService;
        this.subtypeStr = "expenses";
    }

    @Override
    public void loadData() {
        super.loadData();
        Wallet wallet = UserService.getCurrentUser().getWallet();
        wallet.setExpenses(getAllEntities());
    }

    @Override
    public void createOperation(ExpenseCategory category, BigDecimal amount) {
        ExpenseOperation operation = new ExpenseOperation();
        operation.setId(++index);
        operation.setCategory(category);
        operation.setAmount(amount);
        repository.save(operation);
    }
}
