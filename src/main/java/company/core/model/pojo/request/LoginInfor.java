package company.core.model.pojo.request;
import lombok.Data;

@Data
public class LoginInfor implements UserUniform{
    private String login;
    private String password;

    public LoginInfor(String login, String password){
        this.login = login;
        this.password = password;
    }
}
