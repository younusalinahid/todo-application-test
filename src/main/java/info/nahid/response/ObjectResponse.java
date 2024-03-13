package info.nahid.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectResponse {

    private Object object;

    public ObjectResponse(boolean success, String message, Object object) {
        super();
        this.object = object;
    }
}
