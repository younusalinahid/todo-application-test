package info.nahid.controller;

import info.nahid.entity.Todo;
import info.nahid.exception.ConstraintsViolationException;
import info.nahid.mapper.TodoMapper;
import info.nahid.request.TodoRequest;
import info.nahid.response.ObjectResponse;
import info.nahid.service.TaskService;
import info.nahid.service.TodoService;
import info.nahid.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
                .convertTodoWithoutTaskDto(todoService.create(todo))),
        HttpStatus.CREATED);
    }
}
