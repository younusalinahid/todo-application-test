package info.nahid.repository;

import info.nahid.TestData;
import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TodoRepository todoRepository;

    @Test
    public void findAll_successful() {
        Todo todo = TestData.createTodo();
        Todo todo1 = todoRepository.save(todo);

        Task task = TestData.createTask();
        task.setTodo(todo1);
        taskRepository.save(task);

        assertEquals(1, List.of(taskRepository.findAll()).size());
    }
}
