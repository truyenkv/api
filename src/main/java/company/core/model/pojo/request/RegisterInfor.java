package company.core.model.pojo.request;

import company.core.Util.DataFaker;
import lombok.Data;

@Data
public class RegisterInfor implements UserUniform{
    private String email;
    private String username;
    private String password;

    public RegisterInfor() {
        this.email = DataFaker.email();
        this.username = DataFaker.firstName();
        this.password = "Truyen1234@";
    }

    public RegisterInfor(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
