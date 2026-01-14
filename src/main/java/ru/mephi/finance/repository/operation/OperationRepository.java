package ru.mephi.finance.repository.operation;

import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.model.operation.Operation;
import ru.mephi.finance.repository.EntityRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class OperationRepository<O extends Operation<C>, C extends Category> extends EntityRepository<O> {

    public void deleteOperationsForCategory(C category) {
        List<O> operations = new ArrayList<>(findAll()); // чтобы избежать ConcurrentModificationException
        for (O operation : operations) {
            if (operation.getCategory().equals(category)) {
                delete(operation);
            }
        }
    }
}
