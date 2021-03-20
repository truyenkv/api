package company.core.api;

public class SSORoute {
    //Using for Register account
    public static final String users = "api/users";
    //Using for Login
    public static final String login = "api/users/sign_in";
    //Using for Login with 2FA Authen
    public static final String login2FA = "api/users/verify_otp";
    //Using for Generate 2FA Coode
    public static final String FAGeneral = "api/two_factor_authentications/generate";
    //Using for verify 2FA Code
    public static final String FAVerify = "api/two_factor_authentications/verify";
    //Using for Disable login 2FA
    public static final String FADisable = "api/two_factor_authentications/disable";
    //Using for Resend confirm email
    public static final String resetEmail = "api/users/resend";
    //Using for Refresh Token
    public static final String refreshToken = "api/refresh_tokens";
    //Using for Reset password
    public static final String resetPass = "api/users/password";
    //Using for confirm Email
    public static final String confirmEmail = "api/users/confirmation";
    //Using for Get me
    public static final String getMe = "api/users/me?";
    //Using for get health
    public static final String health = "health";
    //Using for Sign out
    public static final String signOut = "api/users/sign_out";
    //Using for Antiphishing
    public static final String AntiPhishing = "api/anti_phishing_codes?";
    //Using for Hook
    public static final String hook = "hook/users";
}
