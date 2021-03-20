package company.core.model.pojo.request;
import lombok.Data;

@Data
public class TwoFA {
    private String password;
    private String otp_attempt;

    public TwoFA(String password, String otp_attempt){
        this.password = password;
        this.otp_attempt = otp_attempt;
    }
}
