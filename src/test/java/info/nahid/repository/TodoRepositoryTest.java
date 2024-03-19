package info.nahid.repository;

import info.nahid.TestData;
import info.nahid.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void findAll_successful() {
        Todo todo = TestData.createTodo();
        todoRepository.save(todo);

        assertEquals(1, List.of(todoRepository.findAll()).size());
    }
}
