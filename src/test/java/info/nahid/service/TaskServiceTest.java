package info.nahid.service;

import info.nahid.TestData;
import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TaskRepository;
import info.nahid.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    TaskServiceImpl taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    TodoServiceImpl todoService;

    @Test
    public void createTask_successful() throws ConstraintsViolationException {
        Todo todo = TestData.createTodo();
        Task task = TestData.createTask();

        when(todoService.getById(TestData.todoId)).thenReturn(todo);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createTask = taskService.create(task);

        verify(todoService, times(1)).getById(TestData.todoId);
        verify(taskRepository, times(1)).save(task);
        assertEquals(task, createTask);
    }

    @Test
    public void createTask_duplicateKey_throwsConstraintsViolationException() {
        Todo todo = TestData.createTodo();
        Task task = TestData.createTask();

        when(todoService.getById(TestData.todoId)).thenReturn(todo);

        doThrow(new DataIntegrityViolationException(Constants.ALREADY_EXISTS))
                .when(taskRepository).save(task);

        assertThrows(ConstraintsViolationException.class, () -> taskService.create(task));
        verify(todoService, times(1)).getById(TestData.todoId);
        verify(taskRepository, times(1)).save(task);

    }

    @Test
    public void updateTask_successful() throws ConstraintsViolationException{
        Task existingTask = TestData.createTask();
        Task updatedTask = TestData.createUpdatedTask();

        when(taskRepository.findById(TestData.taskId)).thenReturn(Optional.of(existingTask));
        when(todoService.getById(updatedTask.getTodo().getId())).thenReturn(TestData.createUpdatedTodo());
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task actualTask = taskService.update(updatedTask);

        verify(taskRepository, times(1)).findById(TestData.taskId);
        verify(todoService, times(1)).getById(updatedTask.getTodo().getId());
        verify(taskRepository, times(1)).save(existingTask);
        assertEquals(updatedTask, actualTask);
    }

    @Test
    public void updateTask_duplicateKey_throwsConstraintsViolationException() {
        Todo todo = TestData.createTodo();
        Task task = TestData.createTask();

        when(todoService.getById(task.getTodo().getId())).thenReturn(todo);
        when(taskRepository.findById(TestData.taskId)).thenReturn(Optional.of(task));

        doThrow(new DataIntegrityViolationException(Constants.ALREADY_EXISTS))
                .when(taskRepository).save(task);

        assertThrows(ConstraintsViolationException.class, () -> taskService.update(task));
        verify(todoService, times(1)).getById(TestData.todoId);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void getById_successful() {
        Task expectedTask = TestData.createTask();
        when(taskRepository.findById(TestData.taskId)).thenReturn(Optional.of(expectedTask));

        Task actualTask = taskService.getById(TestData.taskId);

        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void getById_nonExistingTodo_throwsEntityNotFoundException() {
        when(taskRepository.findById(TestData.taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getById(TestData.taskId));
    }

    @Test
    public void deleteById_successful() {
        taskService.deleteById(TestData.taskId);

        verify(taskRepository, times(1)).deleteById(TestData.taskId);
    }

    @Test
    public void deleteById_nonExistingTodo_throwsEntityNotFoundException() {
        doThrow(EmptyResultDataAccessException.class)
                .when(taskRepository).deleteById(TestData.taskId);

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteById(TestData.taskId));
    }
}
