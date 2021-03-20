package company.core.model.pojo.request;
import lombok.Data;

@Data
public class LoginBackup implements UserUniform{
    private String token;
    private String otp_backup_code_attempt;

    public LoginBackup(String token, String otp_backup_code_attempt){
        this.token = token;
        this.otp_backup_code_attempt = otp_backup_code_attempt;
    }
}
