package ru.mephi.finance.repository.operation;

import org.springframework.stereotype.Repository;
import ru.mephi.finance.model.category.IncomeCategory;
import ru.mephi.finance.model.operation.IncomeOperation;

@Repository
public class IncomeOperationRepository extends OperationRepository<IncomeOperation, IncomeCategory> {

}
