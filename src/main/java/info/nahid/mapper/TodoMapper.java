package info.nahid.mapper;

import info.nahid.dto.TodoWithoutTaskDTO;
import info.nahid.entity.Todo;
import info.nahid.request.TodoRequest;

public class TodoMapper {

    private TodoMapper() {

    }

    public static Todo convertTaskRequestWithoutId(TodoRequest todoRequest) {
        return new Todo(null,
                todoRequest.getName(), todoRequest.getDescription());
    }

    public static TodoWithoutTaskDTO convertTodoWithoutTaskDto(Todo todo) {
        return new TodoWithoutTaskDTO(
                todo.getId(),todo.getName(),todo.getDescription());
    }
}
