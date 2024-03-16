package info.nahid.mapper;

import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.request.TaskRequest;

import java.util.UUID;

public class TaskMapper {

    private TaskMapper() {

    }

    public static Task convertTaskRequestWithoutId(UUID todoId, TaskRequest taskRequest) {
        Todo todo = new Todo();
        todo.setId(todoId);
        return new Task(null, taskRequest.getName(), taskRequest.getDescription(), todo);
    }

    public static Task convertTaskRequestWithId(UUID todoId, UUID taskId, TaskRequest taskRequest) {
        Todo todo = new Todo();
        todo.setId(todoId);
        return new Task(
                taskId, taskRequest.getName(), taskRequest.getDescription(),todo
        );
    }

}
