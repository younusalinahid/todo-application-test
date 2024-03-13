package info.nahid.service;

import info.nahid.entity.Task;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TaskRepository;
import info.nahid.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Task update(Task task) throws ConstraintsViolationException {
        return null;
    }
}
