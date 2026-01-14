package ru.mephi.finance.cli.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.finance.cli.category.ExpenseCategoryCli;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.model.operation.ExpenseOperation;
import ru.mephi.finance.service.UserService;
import ru.mephi.finance.service.category.ExpenseCategoryService;
import ru.mephi.finance.service.operation.ExpenseOperationService;

import java.math.BigDecimal;

@Component
public class ExpenseOperationCli extends OperationCli<ExpenseOperation, ExpenseCategory> {

    @Autowired
    public ExpenseOperationCli(ExpenseOperationService operationService, ExpenseCategoryService categoryService,
                               ExpenseCategoryCli categoryCli) {
        this.operationService = operationService;
        this.categoryService = categoryService;
        this.categoryCli = categoryCli;
        this.typeRoot = "расход";
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете получить список расходов с ID, категорией и потраченной суммой.");
        System.out.println("3.  Здесь вы можете добавить новый расход, указав ID категории, к которой он относится, " +
                           "и потраченную сумму.");
        System.out.println("\tУчтите, что перед добавление расходов нужно сначала добавить расходные категории.");
        System.out.println("\tЕсли обнаружится, что после добавления может быть превышен бюджет категории (или даже " +
                           "80 % от него),");
        System.out.println("\tлибо общая сумма расходов может сравняться c/превысить общую сумму доходов, " +
                           "то от вас будет запрошено дополнительное подтверждение.");
        System.out.println("4.  Здесь вы можете узнать вашу общую сумму расходов.");
        System.out.println("5.  Здесь вы можете узнать общую сумму расходов по одной или нескольким категориям.");
        System.out.println("0.  Здесь вы можете вернуться в главное меню.");
    }

    @Override
    protected void validateOperationAmountIfNeeded(BigDecimal amount, ExpenseCategory category) {
        try {
            validateCategoryBudget(amount, category);
            validateUserBalance(amount);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Добавление расхода прервано.");
        }
    }

    private void validateCategoryBudget(BigDecimal amount, ExpenseCategory category) {
        BigDecimal currentExpense = operationService.getSumByCategory(category);
        BigDecimal potentialExpense = currentExpense.add(amount);
        BigDecimal budget = category.getBudget();

        if (potentialExpense.compareTo(budget.multiply(new BigDecimal("0.8"))) > 0) {
            System.out.print("При добавлении этого расхода будет превышено 80% бюджета категории. " +
                    "Действительно ли вы хотите это сделать (да/нет): ");
            if (!readPositiveReply()) {
                throw new IllegalStateException();
            }
        }

        if (potentialExpense.compareTo(budget) > 0) {
            System.out.print("При добавлении этого расхода будет превышен бюджет категории. " +
                    "Действительно ли вы хотите это сделать (да/нет): ");
            if (!readPositiveReply()) {
                throw new IllegalStateException();
            }
        }
    }

    private void validateUserBalance(BigDecimal amount) {
        Wallet wallet = UserService.getCurrentUser().getWallet();
        BigDecimal currentExpense = wallet.getTotalExpense();
        BigDecimal potentialExpense = currentExpense.add(amount);
        BigDecimal currentIncome = wallet.getTotalIncome();

        if (potentialExpense.compareTo(currentIncome) == 0) {
            System.out.print("При добавлении этого расхода общие расходы станут равны общим доходам " +
                    " и возникнет нулевой баланс. Действительно ли вы хотите это сделать (да/нет): ");
            if (!readPositiveReply()) {
                throw new IllegalStateException();
            }
        }

        if (potentialExpense.compareTo(currentIncome) > 0) {
            System.out.print("При добавлении этого расхода общие расходы превысят общие доходы " +
                    " и возникнет перерасход. Действительно ли вы хотите это сделать (да/нет): ");
            if (!readPositiveReply()) {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void getTotalOperationSum() {
        Wallet wallet = UserService.getCurrentUser().getWallet();
        System.out.printf("Общий расход: %.2f %n", wallet.getTotalExpense());
    }

    @Override
    protected void printOperationSumByCategory(ExpenseCategory category, BigDecimal sum) {
        super.printOperationSumByCategory(category, sum);
        System.out.printf(", оставшийся бюджет: %.2f %n", category.getBudget().subtract(sum));
    }
}
