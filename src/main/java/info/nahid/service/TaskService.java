package info.nahid.service;

import info.nahid.entity.Task;
import info.nahid.exception.ConstraintsViolationException;

import java.util.UUID;

public interface TaskService {
    Task create(Task task) throws ConstraintsViolationException;

    Task getById(UUID id);

    void deleteById(UUID id);

    Task update(Task task) throws ConstraintsViolationException;
}
