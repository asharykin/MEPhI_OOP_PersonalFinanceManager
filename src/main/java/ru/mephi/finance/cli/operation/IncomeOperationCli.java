package ru.mephi.finance.cli.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.finance.cli.category.IncomeCategoryCli;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.model.operation.IncomeOperation;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.IncomeCategoryService;
import ru.mephi.finance.service.operation.IncomeOperationService;

import java.math.BigDecimal;

@Component
public class IncomeOperationCli extends OperationCli<IncomeOperation, IncomeCategory> {

    @Autowired
    public IncomeOperationCli(IncomeOperationService operationService, IncomeCategoryService categoryService,
                              IncomeCategoryCli categoryCli) {
        this.operationService = operationService;
        this.categoryService = categoryService;
        this.categoryCli = categoryCli;
        this.typeRoot = "доход";
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете получить список доходов с ID, категорией и полученной суммой.");
        System.out.println("3.  Здесь вы можете добавить новый доход, указав ID категории, к которой он относится, " +
                           "и полученную сумму.");
        System.out.println("\tУчтите, что перед добавление доходов нужно сначала добавить доходные категории.");
        System.out.println("4.  Здесь вы можете узнать вашу общую сумму доходов.");
        System.out.println("5.  Здесь вы можете узнать общую сумму доходов по одной или нескольким категориям.");
        System.out.println("0.  Здесь вы можете вернуться в главное меню.");
    }

    @Override
    public void getTotalOperationSum() {
        Wallet wallet = UserService.getCurrentUser().getWallet();
        System.out.printf("Общий доход: %.2f %n", wallet.getTotalIncome());
    }

    @Override
    protected void printOperationSumByCategory(IncomeCategory category, BigDecimal sum) {
        super.printOperationSumByCategory(category, sum);
        System.out.println();
    }
}
