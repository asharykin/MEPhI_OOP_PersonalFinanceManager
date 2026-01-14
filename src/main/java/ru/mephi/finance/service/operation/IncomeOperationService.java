package ru.mephi.finance.service.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.model.operation.IncomeOperation;
import ru.mephi.finance.repository.operation.IncomeOperationRepository;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.IncomeCategoryService;

import java.math.BigDecimal;

@Service
@Order(3)
@Qualifier("migratingService")
public class IncomeOperationService extends OperationService<IncomeOperation, IncomeCategory> {

    @Autowired
    public IncomeOperationService(IncomeOperationRepository operationRepository,
                                  IncomeCategoryService categoryService) {
        this.repository = operationRepository;
        this.categoryService = categoryService;
        this.subtypeStr = "incomes";
    }

    @Override
    public void loadData() {
        super.loadData();
        Wallet wallet = UserService.getCurrentUser().getWallet();
        wallet.setIncomes(getAllEntities());
    }

    @Override
    public void createOperation(IncomeCategory category, BigDecimal amount) {
        IncomeOperation operation = new IncomeOperation();
        operation.setId(++index);
        operation.setCategory(category);
        operation.setAmount(amount);
        repository.save(operation);
    }
}
