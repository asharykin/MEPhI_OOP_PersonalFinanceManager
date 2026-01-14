package ru.mephi.finance.model;

import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.model.operation.ExpenseOperation;
import ru.mephi.finance.model.operation.IncomeOperation;
import ru.mephi.finance.model.operation.Operation;

import java.math.BigDecimal;
import java.util.Collection;

public class Wallet {
    private BigDecimal balance;
    private Collection<IncomeOperation> incomes;
    private Collection<ExpenseOperation> expenses;

    public void setExpenses(Collection<ExpenseOperation> expenses) {
        this.expenses = expenses;
    }

    public void setIncomes(Collection<IncomeOperation> incomes) {
        this.incomes = incomes;
    }

    public BigDecimal getTotalIncome() {
        return getTotalSum(incomes);
    }

    public BigDecimal getTotalExpense() {
        return getTotalSum(expenses);
    }

    public BigDecimal getBalance() {
        BigDecimal balance = getTotalIncome().subtract(getTotalExpense());
        this.balance = balance;
        return balance;
    }

    private <O extends Operation<? extends Category>> BigDecimal getTotalSum(Collection<O> operations) {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (O operation : operations) {
            totalSum = totalSum.add(operation.getAmount());
        }
        return totalSum;
    }
}
