package info.nahid;

import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.request.TaskRequest;
import info.nahid.request.TodoRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestData {
    private TestData() {

    }

    public static final UUID todoId = UUID.fromString("7037bd1e-aa11-4f33-9a3d-bae5a34b034c");
    public static final String todoName = "Todo 10";
    private static final String todoDescription = "Todo Description";

    public static final UUID taskId = UUID.fromString("e07012f4-d07c-4e9a-b1ad-80f5ffc15178");
    public static final String taskName = "Task Item 10";
    public static final String taskDescription = "Task description";

    public static TodoRequest createTodoRequest() {
        return new TodoRequest(
                todoName, todoDescription
        );
    }

    public static TaskRequest createTaskRequest() {
        return new TaskRequest(
                taskName, taskDescription
        );
    }

    public static Todo createTodo() {
        return new Todo(
                todoId, todoName, todoDescription
        );
    }

    public static Task createTask() {
        Todo todo = new Todo();
        todo.setId(TestData.todoId);
        return new Task(
                taskId, taskName,taskDescription, todo
        );
    }

    public static Todo createTodoWithTasks() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(createTask());

        return new Todo(
                todoId, todoName, todoDescription, taskList
        );
    }

    public static Todo createUpdatedTodo() {
        return new Todo(
                todoId,
                todoName + "update",
                todoDescription + "updated"
        );
    }

    public static Task createUpdatedTask(){
        Todo todo = new Todo();
        todo.setId(TestData.todoId);
        return new Task(
                taskId,
                taskName + "update",
                taskDescription + "update",
                todo
        );
    }

}
