package info.nahid.service;

import info.nahid.entity.Task;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TaskRepository;
import info.nahid.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    
    private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TodoService todoService;
    
    @Override
    public Task create(Task task) throws ConstraintsViolationException {
        try {
            todoService.getById(task.getTodo().getId());
            return taskRepository.save(task);
        } catch (DataIntegrityViolationException exception) {
            logger.warn(Constants.DATA_VIOLATION + exception.getMessage());
            throw new ConstraintsViolationException(Constants.ALREADY_EXISTS);
        }
    }

    @Override
    public Task getById(UUID id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        } else {
            logger.warn(Constants.TASK_NOT_FOUND + id);
            throw new EntityNotFoundException(Constants.TASK_NOT_FOUND + id);
        }
    }

    @Override
    public Task update(Task task) throws ConstraintsViolationException {

        Task existingTask = getById(task.getId());
        todoService.getById(task.getTodo().getId());
        BeanUtils.copyProperties(task, existingTask, "id");
        try {
            return taskRepository.save(existingTask);
        } catch (DataIntegrityViolationException exception) {
            logger.warn(Constants.DATA_VIOLATION + exception.getMessage());
            throw new ConstraintsViolationException(Constants.ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            taskRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception) {
            logger.warn(Constants.TASK_NOT_FOUND + id);
            throw new EntityNotFoundException(Constants.TASK_NOT_FOUND + id);
        }
    }

}
