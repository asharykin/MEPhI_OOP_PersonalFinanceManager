package ru.mephi.finance.repository.operation;

import org.springframework.stereotype.Repository;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.model.operation.ExpenseOperation;

@Repository
public class ExpenseOperationRepository extends OperationRepository<ExpenseOperation, ExpenseCategory> {

}
