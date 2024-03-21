package info.nahid.integration;

import info.nahid.TestData;
import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.request.TaskRequest;
import info.nahid.request.TodoRequest;
import info.nahid.response.ApiResponse;
import info.nahid.response.ObjectResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TodoControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    private final String BASE_URL = "/todos";
    HttpHeaders headers;

    @BeforeAll
    public void setup() {
        headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void createTodo_successful() {
        TodoRequest todoRequest = TestData.createTodoRequest();
        HttpEntity<TodoRequest> request = new HttpEntity<>(todoRequest, headers);

        ResponseEntity<ObjectResponse> result = restTemplate.postForEntity(
                BASE_URL,
                request,
                ObjectResponse.class);
        assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
    }

    @Test
    public void updateTodo_successful() {
        Todo updatedTodo = TestData.createUpdatedTodo();
        HttpEntity<Todo> requestEntity = new HttpEntity<>(updatedTodo, headers);

        ResponseEntity<ObjectResponse> responseEntity = restTemplate.exchange(
                BASE_URL + "/" + TestData.todoId,
                HttpMethod.PUT,
                requestEntity,
                ObjectResponse.class);
        assertNotNull(responseEntity);
        assertEquals(TestData.todoName + " update", updatedTodo.getName());

    }

    @Test
    public void getAllTodos_successful() {
        List<Todo> todoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Todo> todoPage = new PageImpl<>(todoList, pageable, todoList.size());

        ResponseEntity<ObjectResponse> responseEntity = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                null,
                ObjectResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteTodo_Successful() {
//        TodoRequest todoRequest = TestData.createTodoRequest();
//        HttpEntity<TodoRequest> request = new HttpEntity<>(todoRequest, headers);
//        ResponseEntity<ObjectResponse> result = restTemplate.exchange(
//                BASE_URL + "/" + TestData.todoId,
//                HttpMethod.DELETE,
//                request,
//                ObjectResponse.class);
//        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ObjectResponse> result = restTemplate.exchange(
                BASE_URL + "/1", HttpMethod.DELETE, request, ObjectResponse.class
        );
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }

    @Test
    public void createTask_successful() {
        TaskRequest taskRequest = TestData.createTaskRequest();
        HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest, headers);
        ResponseEntity<ObjectResponse> result = restTemplate.postForEntity(
                BASE_URL,
                request,
                ObjectResponse.class);

        assertEquals(HttpStatus.CREATED.value(), result.getStatusCodeValue());
    }

    @Test
    public void updateTask_successful() {
        Task updatedTask = TestData.createUpdatedTask();
        HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask, headers);

        ResponseEntity<ObjectResponse> responseEntity = restTemplate.exchange(
                BASE_URL + "/" + TestData.todoId + "/tasks/" + TestData.taskId,
                HttpMethod.PUT,
                requestEntity,
                ObjectResponse.class);
        assertNotNull(responseEntity);
        assertEquals(TestData.taskName + " update", updatedTask.getName());

    }

//    @Test
//    public void deleteTask_successful() {
//
//        ResponseEntity<ObjectResponse> responseEntity = restTemplate.exchange(
//                (BASE_URL + "/" + TestData.todoId + "/tasks/" + TestData.taskId),
//                HttpMethod.DELETE,
//                null,
//                Void.class
//        );
//
//        // Ensure that the response is successful (status code 2xx)
//        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
//
//        // Optionally, you can check for additional response data or headers if needed
//    }

}
