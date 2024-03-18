package info.nahid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.request.TaskRequest;
import info.nahid.request.TodoRequest;
import info.nahid.response.ApiResponse;
import info.nahid.service.TaskServiceImpl;
import info.nahid.service.TodoServiceImpl;
import info.nahid.TestData;
import info.nahid.utils.Constants;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoServiceImpl todoService;

    @MockBean
    private TaskServiceImpl taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String Base_Url = "/todos";

    @Test
    public void createTodo_successful() throws Exception {
        TodoRequest todoRequest = TestData.createTodoRequest();
        Todo createdTodo = TestData.createTodo();
        when(todoService.create(any(Todo.class))).thenReturn(createdTodo);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(Base_Url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(todoRequest));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse(true, Constants.TODO_CREATED))))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.todoName, jsonObject.getJSONObject("object").getString("name"));
    }

    @Test
    public void createTodo_missingTitle_returns400() throws Exception {
        TodoRequest todoRequest = new TodoRequest();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(Base_Url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(todoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTodo_successful() throws Exception{
        TodoRequest todoRequest = TestData.createTodoRequest();
        Todo updatedTodo = TestData.createUpdatedTodo();

        when(todoService.update(any(Todo.class))).thenReturn(updatedTodo);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(Base_Url + "/" + TestData.todoId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(todoRequest));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse(true, Constants.TODO_UPDATED))))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.todoName + "update", jsonObject.getJSONObject("object").getString("name"));
    }

    @Test
    public void getAllTodos_successful() throws Exception {
        Page<Todo> todoPage = mock(Page.class);
        when(todoService.getAll(0,10)).thenReturn(todoPage);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(Base_Url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getTodoById_successful() throws Exception{
        Todo todoWithTasks = TestData.createTodoWithTasks();
        when(todoService.getById(TestData.todoId)).thenReturn(todoWithTasks);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(Base_Url + "/" + TestData.todoId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.todoName, jsonObject.getJSONObject("object").getString("name"));
        assertEquals(TestData.taskName,
                jsonObject.getJSONObject("object").getJSONArray("tasks").getJSONObject(0).getString("name"));
        assertEquals(1, jsonObject.getJSONObject("object").getJSONArray("tasks").length());
    }

    @Test
    public void deleteTodoById_successful() throws Exception{
        doNothing().when(todoService).deleteById(TestData.todoId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(Base_Url + "/" + TestData.todoId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ApiResponse(true, Constants.TODO_DELETED)
                )));
        verify(todoService).deleteById(TestData.todoId);

    }

    @Test
    public void deleteTodoById_invalidTodoId_returns400() throws Exception{
        doNothing().when(todoService).deleteById(TestData.todoId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(Base_Url + "/" + "3647-384787-3u")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTask_successful() throws Exception {
        TaskRequest taskRequest = TestData.createTaskRequest();
        Task createdTask = TestData.createTask();

        when(taskService.create(any(Task.class))).thenReturn(createdTask);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(Base_Url + "/" + TestData.todoId + "/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(taskRequest));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse(true, Constants.TASK_CREATED))))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.taskName, jsonObject.getJSONObject("object").getString("name"));
    }

    @Test
    public void createTask_missingTitle_returns400() throws Exception {
        TaskRequest taskRequest = new TaskRequest();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(Base_Url + "/" + TestData.todoId + "/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(taskRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTask_successful() throws Exception{
        TaskRequest taskRequest = TestData.createTaskRequest();
        Task updatedTask = TestData.createUpdatedTask();

        when(taskService.update(any(Task.class))).thenReturn(updatedTask);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(Base_Url + "/" + TestData.todoId + "/tasks/" + TestData.taskId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(taskRequest));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse(true, Constants.TASK_UPDATED))))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.taskName + "update", jsonObject.getJSONObject("object").getString("name"));
    }

    @Test
    public void getTaskById_successful() throws Exception {
        Task task = TestData.createTask();

        when(taskService.getById(TestData.taskId)).thenReturn(task);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(Base_Url + "/" + TestData.todoId + "/tasks/" + TestData.taskId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals(TestData.taskName, jsonObject.getJSONObject("object").getString("name"));
    }


    @Test
    public void deleteTaskById_successful() throws Exception{
        doNothing().when(taskService).deleteById(TestData.taskId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(Base_Url + "/" + TestData.todoId + "/tasks/" + TestData.taskId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ApiResponse(true, Constants.TASK_DELETED)
                )));
        verify(taskService).deleteById(TestData.taskId);

    }

    @Test
    public void deleteTaskId_invalidTaskId_returns400() throws Exception{
        doNothing().when(taskService).deleteById(TestData.taskId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(Base_Url + "/" + TestData.todoId + "3647-384787-3u")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

}
