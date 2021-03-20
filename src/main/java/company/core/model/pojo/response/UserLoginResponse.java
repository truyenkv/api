package company.core.model.pojo.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    public String status;
    public String message;
    public Object data;
}
