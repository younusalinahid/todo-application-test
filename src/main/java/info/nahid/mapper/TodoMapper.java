package info.nahid.mapper;

import info.nahid.dto.TaskDTO;
import info.nahid.dto.TodoWithTasksDTO;
import info.nahid.dto.TodoWithoutTaskDTO;
import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.request.TodoRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoMapper {

    private TodoMapper() {

    }

    public static Todo convertTaskRequestWithoutId(TodoRequest todoRequest) {
        return new Todo(null,
                todoRequest.getName(), todoRequest.getDescription());
    }

    public static Todo convertTodoRequestWithId(TodoRequest todoRequest, UUID id) {
        return new Todo(
                id, todoRequest.getName(), todoRequest.getDescription()
        );
    }

    public static TodoWithTasksDTO convertTodoWithTasksDTO(Todo todo) {
        List<TaskDTO> taskDTOS = todo.getTasks().stream()
                .map(TodoMapper::convertTaskToTaskDTO)
                .collect(Collectors.toList());

        return new TodoWithTasksDTO(
                todo.getId(), todo.getName(), todo.getDescription(),taskDTOS
        );
    }

    public static TodoWithoutTaskDTO convertTodoWithoutTasksDto(Todo todo) {
        return new TodoWithoutTaskDTO(
                todo.getId(),todo.getName(),todo.getDescription());
    }

    public static List<TodoWithoutTaskDTO> convertTodoWithoutTasksDto(List<Todo> todoList) {
        return todoList.stream()
                .map(TodoMapper::convertTodoWithoutTasksDto)
                .collect(Collectors.toList());
    }

    private static TaskDTO convertTaskToTaskDTO(Task task) {
        return new TaskDTO(
                task.getId(), task.getName(), task.getDescription()
        );
    }
}
