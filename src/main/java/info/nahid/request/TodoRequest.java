package info.nahid.request;

import info.nahid.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequest {

    @NotNull(message = Constants.NOT_EMPTY_NAME)
    @Size(max = 150)
    private String name;

    private String description;
}
