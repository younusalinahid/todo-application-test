package info.nahid.service;

import info.nahid.TestData;
import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.repository.TodoRepository;
import info.nahid.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    TodoServiceImpl todoService;

    @Mock
    TodoRepository todoRepository;

    @Test
    public void createTodo_successful() throws ConstraintsViolationException {

        Todo todo = TestData.createTodo();
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo createTodo = todoService.create(todo);

        assertEquals(todo, createTodo);
        assertEquals(todo.getName(), createTodo.getName());
    }

    @Test
    public void createTodo_duplicateKey_throwsConstraintsViolationException() {
        Todo todo = TestData.createTodo();

        doThrow(new DataIntegrityViolationException(Constants.ALREADY_EXISTS))
                .when(todoRepository).save(any(Todo.class));

        assertThrows(ConstraintsViolationException.class, () -> todoService.create(todo));
    }

    @Test
    public void getAll_successful() {
        Todo todo = TestData.createTodo();

        Page<Todo> todoPage = new PageImpl<Todo>(List.of(todo));
        given(todoRepository.findAll(any(Pageable.class))).willReturn(todoPage);

        Page<Todo> result = todoService.getAll(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void getById_successful() {
        Todo expectedTodo = TestData.createTodoWithTasks();
        when(todoRepository.findById(TestData.todoId)).thenReturn(Optional.of(expectedTodo));

        Todo actualTodo = todoService.getById(TestData.todoId);

        assertEquals(expectedTodo, actualTodo);
        assertEquals(1, actualTodo.getTasks().size());
    }

    @Test
    public void getById_nonExistingTodo_throwsEntityNotFoundException() {
        when(todoRepository.findById(TestData.todoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> todoService.getById(TestData.todoId));
    }

    @Test
    public void deleteById_successful() {
        todoService.deleteById(TestData.todoId);

        verify(todoRepository, times(1)).deleteById(TestData.todoId);
    }

    @Test
    public void deleteById_nonExistingTodo_throwsEntityNotFoundException() {
        doThrow(EmptyResultDataAccessException.class)
                .when(todoRepository).deleteById(TestData.todoId);

        assertThrows(EntityNotFoundException.class, () -> todoService.deleteById(TestData.todoId));
    }

    @Test
    public void updateTodo_successful() throws ConstraintsViolationException {
        Todo existingTodo = TestData.createTodo();
        Todo upatedTodo = TestData.createUpdatedTodo();

        when(todoRepository.findById(TestData.todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(upatedTodo);

        Todo actualTodo = todoService.update(upatedTodo);

        verify(todoRepository, times(1)).findById(TestData.todoId);
        verify(todoRepository, times(1)).save(existingTodo);
        assertEquals(upatedTodo, actualTodo);
    }

    @Test
    public void updateTodo_nonExistingTodo_throwsEntityNotFoundException() {
        Todo updateTodo = TestData.createTodo();

        when(todoRepository.findById(TestData.todoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> todoService.update(updateTodo));
    }

    @Test
    public void updateTodo_duplicateKey_throwsConstraintsViolationException() {
        Todo existingtodo = TestData.createTodo();
        Todo updatedTodo = TestData.createUpdatedTodo();

        when(todoRepository.findById(TestData.todoId)).thenReturn(Optional.of(existingtodo));


        doThrow(new DataIntegrityViolationException(Constants.ALREADY_EXISTS))
                .when(todoRepository).save(existingtodo);

        assertThrows(ConstraintsViolationException.class, () -> todoService.update(updatedTodo));
    }


}
