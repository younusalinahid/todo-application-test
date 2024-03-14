package info.nahid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoWithTasksDTO {

    private UUID id;
    private String name;
    private String description;
    private List<TaskDTO> tasks;
}
