package info.nahid.controller;

import info.nahid.entity.Task;
import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.mapper.TaskMapper;
import info.nahid.mapper.TodoMapper;
import info.nahid.request.TaskRequest;
import info.nahid.request.TodoRequest;
import info.nahid.response.ApiResponse;
import info.nahid.response.ObjectResponse;
import info.nahid.response.PageResponse;
import info.nahid.service.TaskService;
import info.nahid.service.TodoService;
import info.nahid.utils.Constants;
import info.nahid.validators.ValidUuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<ObjectResponse> create(@Valid @RequestBody TodoRequest todoRequest)
        throws ConstraintsViolationException {
        Todo todo = TodoMapper.convertTaskRequestWithoutId(todoRequest);
        return new ResponseEntity<>(new ObjectResponse(true, Constants.TODO_CREATED, TodoMapper
                .convertTodoWithoutTasksDto(todoService.create(todo))),
        HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectResponse> update(@PathVariable("id") @ValidUuid UUID id,
                                                 @Valid @RequestBody TodoRequest todoRequest) throws ConstraintsViolationException {
        Todo todo = TodoMapper.convertTodoRequestWithId(todoRequest, id);
        return ResponseEntity.ok(
                new ObjectResponse(true, Constants.TODO_UPDATED,
                        TodoMapper.convertTodoWithoutTasksDto(todoService.update(todo)))
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Todo> todoPage = todoService.getAll(pageNo, pageSize);
        return ResponseEntity.ok(
                new PageResponse(true, "",
                        todoPage.getTotalElements(),
                        todoPage.getTotalPages(),
                        todoPage.getNumber(),
                        List.of(TodoMapper.convertTodoWithoutTasksDto(todoPage.getContent().stream().toList()))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectResponse> getById(@PathVariable("id") @ValidUuid UUID id) {
        Todo todo = todoService.getById(id);
        return ResponseEntity.ok(new ObjectResponse(true, Constants.TODO_FOUND, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") @ValidUuid UUID id) {
        todoService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, Constants.TODO_DELETED));
    }

    @PostMapping("/{todoId}/tasks")
    public ResponseEntity<ObjectResponse> createTask(@PathVariable("todoId") @ValidUuid UUID todoId,
                                                     @Valid @RequestBody TaskRequest taskRequest)
        throws ConstraintsViolationException {
        Task task = TaskMapper.convertTaskRequestWithoutId(todoId, taskRequest);
        return new ResponseEntity<>(
                new ObjectResponse(true, Constants.TASK_CREATED, taskService.create(task)), HttpStatus.CREATED
        );
    }

    @PutMapping("/{todoId}/tasks/{taskId}")
    public ResponseEntity<ObjectResponse> updateTask(@PathVariable("todoId") @ValidUuid UUID todoId,
                                                     @PathVariable("taskId") @ValidUuid UUID taskId,
                                                     @Valid @RequestBody TaskRequest taskRequest) throws ConstraintsViolationException {
        Task task = TaskMapper.convertTaskRequestWithId(todoId, taskId,taskRequest);
        return ResponseEntity.ok(
                new ObjectResponse(true, Constants.TASK_UPDATED, taskService.update(task))
        );
    }

    @GetMapping("/{todoId}/tasks/{taskId}")
    public ResponseEntity<ObjectResponse> getTaskById(@PathVariable("todoId") @ValidUuid UUID todoId,
                                                      @PathVariable("taskId") @ValidUuid UUID taskId) {
        todoService.getById(todoId);
        Task task = taskService.getById(taskId);
        return ResponseEntity.ok(new ObjectResponse(true, Constants.TASK_FOUND,task));
    }

    @DeleteMapping("/{todoId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("todoId") @ValidUuid UUID todoId,
                                                  @PathVariable("taskId") @ValidUuid UUID taskId) {
        todoService.getById(todoId);
        taskService.deleteById(taskId);
        return ResponseEntity.ok(new ApiResponse(true, Constants.TASK_DELETED));
    }
}