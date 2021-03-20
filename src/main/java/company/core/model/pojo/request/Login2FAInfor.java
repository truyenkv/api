package company.core.model.pojo.request;

import lombok.Data;

@Data
public class Login2FAInfor implements UserUniform{
    private String token;
    private String otp_attempt;

    public Login2FAInfor(String token, String otp_attempt){
        this.token = token;
        this.otp_attempt = otp_attempt;
    }

}
