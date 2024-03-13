package info.nahid.service;

import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface TodoService {
    Todo create(Todo todo) throws ConstraintsViolationException;

    Page<Todo> getAll(int pageNo, int pageSize);

    void getById(UUID id);

    void deleteById(UUID id);

    Todo update(Todo todo) throws ConstraintsViolationException;
}
