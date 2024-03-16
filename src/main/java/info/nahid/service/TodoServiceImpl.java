package info.nahid.service;

import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TodoRepository;
import info.nahid.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
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
        Pageable todoPageable = PageRequest.of(pageNo, pageSize);
        return todoRepository.findAll(todoPageable);
    }

    @Override
    public Todo getById(UUID id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            return todo.get();
        } else {
            logger.warn(Constants.TODO_NOT_FOUND + id);
            throw new EntityNotFoundException(Constants.TODO_NOT_FOUND + id);
        }
    }


    @Override
    public Todo update(Todo todo) throws ConstraintsViolationException {
        Todo existingTodo = getById(todo.getId());
        BeanUtils.copyProperties(todo, existingTodo, "id");
        try {
            return todoRepository.save(existingTodo);
        } catch (DataIntegrityViolationException exception) {
            logger.warn(Constants.DATA_VIOLATION + exception.getMessage());
            throw new ConstraintsViolationException(Constants.ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            todoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            logger.warn(Constants.TODO_NOT_FOUND + id);
            throw new EntityNotFoundException(Constants.TODO_NOT_FOUND + id);
        }
    }
}
