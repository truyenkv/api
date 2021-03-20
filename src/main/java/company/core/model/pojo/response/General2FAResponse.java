package company.core.model.pojo.response;

import lombok.Data;

@Data
public class General2FAResponse {
    public String status;
    public String message;
    public Object data;
}
