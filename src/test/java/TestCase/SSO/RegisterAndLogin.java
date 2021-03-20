package TestCase.SSO;

import company.core.Util.LoadMessage;
import company.core.api.BaseAPI;
import company.core.api.InitConnection;
import company.core.api.SSORoute;
import company.core.api.SetHeader;
import company.core.helper.UsersHelper;
import company.core.jdbcPostgree.PostGresDB;
import company.core.model.pojo.request.LoginInfor;
import company.core.model.pojo.request.User;
import company.core.model.pojo.request.RegisterInfor;
import company.core.model.pojo.response.UserLoginResponse;
import company.core.model.pojo.response.UserRegisterResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterAndLogin extends BaseAPI {
    InitConnection initConnection = new InitConnection("SSODBNAME","CEWL_ST"); //init the db infor
    Connection connection = PostGresDB.ConnectDB(initConnection.getDataBaseName(),initConnection.getSchemaName()); // create connection with data infor above
    RequestSpecification requestSpecification = requestSpec().headers(SetHeader.SetHeaderDefault()); //create requestSpecication with header default
    Response response = null; //declare response
    User user = new User(); //create the User pojo class
    RegisterInfor registerInfor =  new RegisterInfor(); //delare the RegisterInfor pojo class
    static String lang = System.getProperty("lang"); // get value of param "lang" from command line.
    UserRegisterResponse userResponse = null; //declare UserRegisterResponse pojo class
    UsersHelper usersHelper = new UsersHelper(); //create UserHelper for excuting with DB
    UserLoginResponse userLoginResponse = null;

    @BeforeClass
    public void BeforeTest(){
        log.info("\n"+"\n"+"%----- STARET VERIFY REGISTER ACCOUNT-----%");
    }

    @BeforeMethod
    public void BeforeMethod(){
        log.info("\n");
    }
    @AfterTest
    public void Connection(){
        DisConnect();
    }

    /* This method create the data for case register with invalid information*/
    @DataProvider(name = "invalidUser")
    public Object[][] InvalidEmail() {
        return new Object[][]{{"", "truyenkv", "Aa123456@", "emailNull"}, {"truyen", "truyenkv", "Aa123456@", "invalidEmail"},
                {"truyen@@yopmail.com", "truyenkv", "Aa123456@", "invalidEmail"}, {"truyenkieu@vncdevs.com", "truyenkv", "Aa123456@", "existedEmail"},
                {"truyen1232@yopmail.com", "", "Aa123456@", "invalidUsername"}, {"truyen1232@yopmail.com", "TruyenKV", "Aa123456@", "invalidUsername"},
                {"truyen1232@yopmail.com", "truyen3133", "Aa123456@", "existedUsername"}, {"truyen1232@yopmail.com", "truyen31333", "", "passNull"},
                {"truyen1232@yopmail.com", "truyen31333", "123", "passInvalid"}};
    }

    @DataProvider(name = "tokenInvalid")
    public Object[][]Token(){
        return new Object[][]{{""},{"L121UqCBaUkzq2_nSJ_7"}};
    }

    @DataProvider(name = "loginInfor")
    public Object[][]LoginInfor(){
        return new Object[][]{{"truyen1555@yopmail.com","Truyen1234@"},{"truyen9161","Truyen1234@"}};
    }

    @DataProvider(name = "loginInvalid")
    public Object[][]LoginInvalid(){
        return new Object[][]{{"", "","withoutRegister"},{"truyen.com.vn", "Truyen1234@","loginInvalid"},{"truyen1231@com.vn", "Truyen1234@","loginInvalid"},
                {"truyen1231@com.vn", "","loginInvalid"}};
    }


    /* Using Data Driven by Data Provider of TestNG*/
    @Test(priority = 1,dataProvider = "invalidUser")
    public void VerifyRegisterWithInvalidInfor(String email, String username, String password, String error) {
        log.info("1. TEST CASE - VerifyRegisterWithInvalidInfor: Verify the error message with register with invalid email, username and password");
        log.info("Step 1: Declare the user information");
        user.setUser(new RegisterInfor(email, username, password));
        response = requestSpecification.body(user).post(SSORoute.users);

        log.info("Step 2: Get the response and verify the statusLine, status and message");
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 422 Unprocessable Entity");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, error));
    }

    @Test(priority = 2)
    public void VerifyRegisterSuccessful() {
        log.info("2. TEST CASE - VerifyRegisterSuccessful: Verify user register account ");
        log.info("Step 1: Declare the user information");
        user.setUser(registerInfor);
        response = requestSpecification.body(user).post(SSORoute.users);

        log.info("Step 2: Verify response include email, username, default status, and default otp_require_login_code");
        userResponse = response.body().as(UserRegisterResponse.class);
        Assert.assertEquals(registerInfor.getEmail(), userResponse.email, "the email is incorrectly");
        Assert.assertEquals(registerInfor.getUsername(), userResponse.username, "the username is incorrectly");
        Assert.assertEquals(userResponse.status, "email_unverified", "the email is incorrectly");
        Assert.assertEquals(userResponse.first_login, true, "The first time user logintrue must be FALSE? "); // check is the first time login = true
        Assert.assertFalse(usersHelper.OTPRequireLogin(connection, initConnection.getSchemaName(), registerInfor.getEmail())); //check in database otp_require_login is false
    }

    /* Verify login with account that hadn't been confirmed email.*/
    @Test(priority = 3)
    public void VerifyLoginWithoutActiveEmail(){
        log.info("3. TEST CASE - VerifyLoginWithoutActiveEmail: Verify login when user hadn't actived email ");
        log.info("Step 1: Declare the user information");
        user.setUser(new LoginInfor("truyen4922@yopmail.com","Truyen1234@"));

        log.info("Step 2: Get response and verify status, error message ");
        response = requestSpecification.body(user).post(SSORoute.login);
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang,"emailWithoutActive"));
    }

    /*Verify Token Invalid*/
    @Test(priority = 4, dataProvider = "tokenInvalid")
    public void VerifyConfirmEmailFail(String token){
        log.info("4. TEST CASE - VerifyConfirmEmailFail: Verify confirm email with invalid token");
        log.info("Step 1: Declare the user information");
        requestSpecification.queryParam("token", token);

        log.info("Step 2: Get response and verify status, error message ");
        response = requestSpecification.get(SSORoute.confirmEmail);
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "tokenInValid"));
    }

    @Test(priority = 5, dependsOnMethods = {"VerifyRegisterSuccessful"})
    public void VerifyConfirmSuccessful() throws SQLException {
        log.info("5. TEST CASE - VerifyConfirmSuccessful: Verify confirm user confirms login email successful");
        log.info("Step 1: Connect the DB and get the emailConfirmToken");
        String emailConfirmToken = usersHelper.GetConfirmationTokenByEmail(connection, initConnection.getSchemaName(), registerInfor.getEmail());
        requestSpecification.queryParam("token", emailConfirmToken);

        log.info("Step 2: Get response with emailConfirmToken from DB");
        response = requestSpecification.get(SSORoute.confirmEmail);

        log.info("Step 3: Get kyc_level default of user after actived the email");
        String kyc = usersHelper.GetKYCCodeByEmail(connection, initConnection.getSchemaName(), registerInfor.getEmail());

        log.info("Step 4: Verify the status, message and kyc = 1");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "success");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "emailConfirmSucc"));
        Assert.assertEquals(kyc, "1");
    }

    @Test(priority = 6,dataProvider = "loginInfor")
    public void VerifyLoginByEmailOrUsername(String login, String password) throws ParseException {
        log.info("6. TEST CASE - VerifyLoginByEmailOrUsername: Verify user can login by email or username succesful");
        log.info("Step 1: Declare the user information");
        user.setUser(new LoginInfor(login, password));
        response = requestSpecification.body(user).post(SSORoute.login);

        log.info("Step 2: Get response and verify the message, status");
        userLoginResponse = response.getBody().as(UserLoginResponse.class);
        Assert.assertEquals(userLoginResponse.message, "success");
        Assert.assertEquals(userLoginResponse.status, "success");
    }

    @Test(priority = 7, dataProvider = "loginInvalid")
    public void VerifyLoginByInvalidAccount(String login, String password, String messageError){
        log.info("7. TEST CASE - VerifyByInvalidAccount: Verify the status and message when user login invalid infomation");
        log.info("Step 1: Declare the user information");
        user.setUser(new LoginInfor(login, password));

        log.info("Step 2: Get response and verify the message, status");
        response = requestSpecification.body(user).post(SSORoute.login);
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, messageError));
    }

    @Test(priority = 8, dependsOnMethods = {"VerifyConfirmSuccessful"})
    public void VerifyLoginAfterConfirmEmail() {
        log.info("8. TEST CASE - VerifyLoginAfterConfirmEmail: Verify user login immediately actives account");
        log.info("Step 1: Declare the user information");
        user.setUser(new LoginInfor(registerInfor.getEmail(), registerInfor.getPassword()));
        response = requestSpecification.body(user).post(SSORoute.login);

        log.info("Step 2: Get response and verify the message, status");
        userLoginResponse = response.getBody().as(UserLoginResponse.class);
        Assert.assertEquals(userLoginResponse.message, "success");
        Assert.assertEquals(userLoginResponse.status, "success");
    }
}