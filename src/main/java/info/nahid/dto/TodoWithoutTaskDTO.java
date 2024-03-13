package info.nahid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoWithoutTaskDTO {

    private UUID id;
    private String name;
    private String description;
}
