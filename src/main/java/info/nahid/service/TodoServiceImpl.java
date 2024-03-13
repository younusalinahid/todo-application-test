package info.nahid.service;

import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TodoRepository;
import info.nahid.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService{

    private static Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo create(Todo todo) throws ConstraintsViolationException {
        try {
            return todoRepository.save(todo);
        } catch (DataIntegrityViolationException exception) {
            logger.warn(Constants.DATA_VIOLATION + exception.getMessage());
            throw new ConstraintsViolationException(Constants.ALREADY_EXISTS);
        }
    }

    @Override
    public Page<Todo> getAll(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public void getById(UUID id) {
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Todo update(Todo todo) throws ConstraintsViolationException {
        return null;
    }
}
