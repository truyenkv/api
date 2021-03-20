package TestCase.SSO;

import com.jayway.jsonpath.JsonPath;
import company.core.Util.LoadMessage;
import company.core.Util.Soldier;
import company.core.api.BaseAPI;
import company.core.api.InitConnection;
import company.core.api.SSORoute;
import company.core.api.SetHeader;
import company.core.helper.UsersHelper;
import company.core.jdbcPostgree.PostGresDB;
import company.core.model.pojo.request.*;
import company.core.model.pojo.response.General2FAResponse;
import company.core.model.pojo.response.UserLoginResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;

public class TwoFAAuthen extends BaseAPI {
    RequestSpecification requestSpecification = requestSpec().headers(SetHeader.SetHeaderDefault()); //create requestSpecication with header default
    RequestSpecification requestWithToken = null;
    Response response = null; //declare response
    User user = new User(); //create the User pojo class
    UsersHelper usersHelper = new UsersHelper();
    LoginInfor loginInfor = null;
    JSONParser parser = new JSONParser();
    InitConnection initConnection = new InitConnection("SSODBNAME","CEWL_ST"); //init the db infor
    Connection connection = PostGresDB.ConnectDB(initConnection.getDataBaseName(),initConnection.getSchemaName());
    static String lang = System.getProperty("lang"); // get value of param "lang" from command line.
    UserLoginResponse userLoginResponse = null;
    static String otp_secrect = null;
    static String accessToken = null;
    static String token2FA = null;
    TwoFA twoFABody = null;


    @BeforeClass
    public void BeforeTest(){
        log.info("\n"+"\n"+"%----- STARET VERIFY 2FA-----%");
    }

    @BeforeMethod
    public void BeforeMethod(){
        log.info("\n");
    }

    @AfterTest
    public void Connection(){
        DisConnect();
    }

    @DataProvider(name = "Enable2FAFail")
    public Object[][]Enable2FAFail(){
        return new Object[][]{{"", "", "incorrectPass"}, {"Truyen1234", "323423", "incorrectPass"}, {"Truyen1234@", "323423", "towFAEnableFail"}};
    }

    @DataProvider(name = "Invalid2FALogin")
    public Object[][]Invalid2FALogin(){
        return new Object[][]{{"", "", "tokenInValid"}, {"", "323423", "tokenInValid"}, {"2342fwwerwer2423423d", "", "tokenInValid"}, {"2342fwwerwer2423423d", "234234", "tokenInValid"}};
    }

    @DataProvider(name = "Invalid2FADisable")
    public Object[][]Invalid2FADisable(){
        return new Object[][]{{"", "", "incorrectPass"}, {"", "323423", "incorrectPass"}, {"Truyen", "323423", "incorrectPass"}, {"Truyen1234@", "234234", "towFAEnableFail"},{"Truyen1234@", "", "backupCodeRequire"}};
    }

    @Test(priority = 1)
    public void Generate2FAWithoutToken() {
        log.info("1. TESTCASE - Generate2FAWithoutToken: Verify the return the message required user must login");
        log.info("Step 1: General 2FA without token ");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(""));
        response = requestWithToken.post(SSORoute.FAGeneral);
        log.info("Step 2: Return message require login ");
        Assert.assertEquals(response.getBody().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.getBody().jsonPath().getString("message"), LoadMessage.getMessage(lang, "gen2FAWithoutToken"));
    }

    @Test(priority = 2)
    public void Generate2FASuccessful() throws InterruptedException, SQLException {
        log.info("2. TESTCASE 2 - Generate2FASuccessful: Verify user login and generates 2FA code successful");
        log.info("Step 1: Force update otp_require_tokem = false");
        loginInfor = new LoginInfor("truyen1555@yopmail.com", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false,loginInfor.getLogin());
        Soldier.WaitUntil(1);
        user.setUser(loginInfor);

        log.info("Step 2: Login and store accessToken");
        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken

        log.info("Step 3: General 2FA code");
        response = requestWithToken.post(SSORoute.FAGeneral); // General 2FA code
        General2FAResponse general2FAResponse = response.getBody().as(General2FAResponse.class); //map the response to pojo class
        Assert.assertEquals(general2FAResponse.status, "success");
        Assert.assertEquals(general2FAResponse.message, LoadMessage.getMessage(lang, "gen2FaSuccess"));

        log.info("Step 4: General success and store the otp_secrect");
        otp_secrect = Soldier.GetValueByPath(general2FAResponse.data, "$.otp_secret");
    }

    @Test(priority = 3,dependsOnMethods = {"Generate2FASuccessful"})
    public void VerifyEnable2FASuccessful() {
        log.info("3. TESTCASE - VerifyEnable2FASuccessful: Enable the 2FA after generating at Generate2FASuccessful");
        log.info("Step 1: Declare 2FA body includes Password and OTP");
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect)); //create 2FA with password anh OTP
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 2: Enable 2FA success!");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "success");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "towFAEnabled"));
    }

    @Test(priority = 4, dependsOnMethods = {"VerifyEnable2FASuccessful"})
    public void VerifyEnable2WithSessionRevoked() {
        log.info("4. TESTCASE - VerifyEnable2WithSessionRevoked: Verify message when user enalbe 2FA with session was revoked");
        log.info("Step 1: Declare 2FA body includes Password and OTP");
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect));
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));

        log.info("Step 2: Get response with accesToken was revoked in step 1.VerifyEnable2FASuccessful");
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 3: Done and verify status, message");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "sessionRevoked"));

    }

    @Test(priority = 5, dependsOnMethods = {"VerifyEnable2FASuccessful"})
    public void VerifyLogin2FA() throws SQLException {
        log.info("5. TESTCASE - VerifyLogin2FA: Verify login with 2FA after endabled 2FA successful");
        log.info("Step 1: Declare user and turn on otp_require_code in data(cheat)");
        loginInfor = new LoginInfor("truyen9161", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),true, loginInfor.getLogin());
        user.setUser(loginInfor);

        log.info("Step 2: Login to get token2fa");
        response = requestSpecification.body(user).post(SSORoute.login); //login to get token2fa
        userLoginResponse = response.getBody().as(UserLoginResponse.class);

        log.info("Step 3: Stored token2fa");
        token2FA = Soldier.GetValueByPath(userLoginResponse.data, "$.token");

        log.info("Step 4: Wait 31s and get new OTP and declare user for Login2FA");
        Soldier.WaitUntil(31); //wait over 15s for enable OTP
        user.setUser(new Login2FAInfor(token2FA, Soldier.getOTP(otp_secrect)));

        log.info("Step 5: Login 2FA by token 2FA and OTP");
        response = requestSpecification.body(user).post(SSORoute.login2FA);
        userLoginResponse = response.getBody().as(UserLoginResponse.class);

        log.info("Step 6: Verify response includes message and status, store new accessToken");
        Assert.assertEquals(userLoginResponse.message, "success");
        Assert.assertEquals(userLoginResponse.status, "success");
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");
    }

    @Test(priority = 6, dataProvider = "Invalid2FALogin")
    public void VerifyLogin2FAWithInvalidData(String token, String otp, String errMess){
        log.info("6. TESTCASE - VerifyLogin2FAWithInvalidData: Verify the error message when login with invalid token2Fa and invalid password");
        log.info("Step 1: Declare the user with invalid value");
        user.setUser(new Login2FAInfor(token, otp));
        response = requestSpecification.body(user).post(SSORoute.login2FA);
        log.info("Step 2: Verify the status and error message");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, errMess));
    }

    @Test(priority = 7, dependsOnMethods = {"VerifyLogin2FA"})
    public void VerifyDisable2FASuccessful() {
        log.info("7. TESTCASE - VerifyDisable2FASuccessful: Verify disable 2FA after enabled");
        log.info("Step 1: Wait 31s before get new Token");
        Soldier.WaitUntil(31); //wait over 15s for enable OTP

        log.info("Step 2: Declare the 2FA body with password and OTP");
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect));
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FADisable);

        log.info("Step 3: Verify response include status and message");
        Assert.assertEquals(response.body().jsonPath().getString("status"),"success");
        Assert.assertEquals(response.body().jsonPath().getString("message"),LoadMessage.getMessage(lang, "twoFADisabled"));

        log.info("Step 4: Disable 2FA again for make sure successful");
        response = requestWithToken.body(twoFABody).put(SSORoute.FADisable);
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "sessionRevoked"));
    }

    @Test(priority = 8, dataProvider = "Enable2FAFail")
    public void VerifyEnable2FAWithInvalidData(String password, String token, String errMess) throws SQLException {
        log.info("8. TESTCASE - VerifyEnable2FAWithInvalidData: Verify message when disable with invalid password, token, errMess");
        log.info("Step 1: force update otp_require_tokem = false");
        loginInfor = new LoginInfor("truyen1555@yopmail.com", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false,loginInfor.getLogin());
        Soldier.WaitUntil(1);
        user.setUser(loginInfor);

        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class

        log.info("Step 2: General Store accessToken");
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken

        log.info("Step 3: General 2FA code");
        response = requestWithToken.post(SSORoute.FAGeneral); // General 2FA code
        General2FAResponse general2FAResponse = response.getBody().as(General2FAResponse.class); //map the response to pojo class
        Assert.assertEquals(general2FAResponse.status, "success");
        Assert.assertEquals(general2FAResponse.message, LoadMessage.getMessage(lang, "gen2FaSuccess"));

        log.info("Step 4: Declare 2FA body with dataProvider");
        twoFABody = new TwoFA(password, token);
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 5: Verify the status and message error");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, errMess));
    }

    @Test(priority = 9)
    public void VerifyDisable2FAHadnotEnabledYet() throws SQLException {
        log.info("9. TESTCASE - VerifyDisable2FAHadnotEnabledYet: Verify behavior after disable 2FA");
        log.info("Step 1: Declare the user");
        loginInfor = new LoginInfor("truyen9161", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false, loginInfor.getLogin());
        Soldier.WaitUntil(1);
        user.setUser(loginInfor);

        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class

        log.info("Step: 2: Store the accessToken");
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken
        twoFABody = new TwoFA("Truyen1234@", "123456");

        log.info("Step 3: Send request and verify the status and message error");
        response = requestWithToken.body(twoFABody).put(SSORoute.FADisable);
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "twoIsDisabled"));
    }

    @Test(priority = 10)
    public void VerifyDisable2FAWithSessionRevoked() throws SQLException {
        log.info("10. TESTCASE - VerifyDisable2FAWithSessionRevoked: Verify behavior after disabled 2FA success");
        log.info("Step 1: Declare user");
        loginInfor = new LoginInfor("truyen9750@yopmail.com", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false,loginInfor.getLogin());
        user.setUser(loginInfor);

        log.info("Step 2: Login to get accessToken");
        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class

        log.info("Step 3: Store the accessToken");
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken
        response = requestWithToken.post(SSORoute.FAGeneral); // General 2FA code

        log.info("Step 4: General 2FA and store the otp_secrect");
        General2FAResponse general2FAResponse = response.getBody().as(General2FAResponse.class); //map the response to pojo class
        otp_secrect = Soldier.GetValueByPath(general2FAResponse.data, "$.otp_secret");

        log.info("Step 5: Declare the 2FA body and Verify 2FA");
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect)); //create 2FA with password anh OTP
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 6: Disable with the old accessToken");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FADisable);

        log.info("Step 7: Verify the status and error message");
        Assert.assertEquals(response.body().jsonPath().getString("status"), "fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"), LoadMessage.getMessage(lang, "sessionRevoked"));
    }

    @Test(priority = 11, dataProvider = "Invalid2FADisable")
    public void VerifyDisable2FAWithInvalidInfor(String password, String otp, String errorMess) throws SQLException {
        log.info("11. TESTCASE - VerifyDisable2FAWithInvalidInfor: Verify disable 2FA with invalid password, otp, errorMess");
        log.info("Step 1: Declare the user ");
        loginInfor = new LoginInfor("truyen9750@yopmail.com", "Truyen1234@");
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false,loginInfor.getLogin());
        Soldier.WaitUntil(1);
        user.setUser(loginInfor);

        log.info("Step 2: User login and store the accessToken");
        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");

        log.info("Step 3: General 2FA and store the otp_secrect");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken
        response = requestWithToken.post(SSORoute.FAGeneral); // General 2FA code
        General2FAResponse general2FAResponse = response.getBody().as(General2FAResponse.class); //map the response to pojo class
        otp_secrect = Soldier.GetValueByPath(general2FAResponse.data, "$.otp_secret");

        log.info("Step 4: Wait 31s and create 2FABody include password and OTP");
        Soldier.WaitUntil(31);
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect)); //create 2FA with password anh OTP
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 5: Login after enabled 2FA");
        response = requestSpecification.body(user).post(SSORoute.login); //login to get token2fa
        userLoginResponse = response.getBody().as(UserLoginResponse.class);

        log.info("Step 6: Store the token2FA and wait 31s before get new OTP");
        token2FA = JsonPath.read(userLoginResponse.data.toString().replace("=", ":"), "$.token"); //get the Token by loginFunction
        Soldier.WaitUntil(31); //wait over 15s for enable OTP
        user.setUser(new Login2FAInfor(token2FA, Soldier.getOTP(otp_secrect)));

        log.info("Step 7: Login 2FA with token and OTP ");
        response = requestSpecification.body(user).post(SSORoute.login2FA);
        userLoginResponse = response.getBody().as(UserLoginResponse.class);

        log.info("Step 8: Store the accessToken");
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");

        log.info("Step 9: Declare 2FA body for disable with dataProvider");
        twoFABody = new TwoFA(password, otp);
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FADisable);
        Assert.assertEquals(response.body().jsonPath().getString("status"),"fail");
        Assert.assertEquals(response.body().jsonPath().getString("message"),LoadMessage.getMessage(lang, errorMess));
    }

    @Test(priority = 12)
    public void LoginBackUpSucessful() throws ParseException, SQLException {
        log.info("12. TESTCASE - LoginBackUpSucessful: Verify login backup successul");
        log.info("Step 1: Declare the user and set otp_require_otp = false in database");
        loginInfor = new LoginInfor("truyen2121@yopmail.com", "Truyen1234@");
        user.setUser(loginInfor);
        usersHelper.TurnOf2FAByEmail(connection, initConnection.getSchemaName(),false,loginInfor.getLogin());
        Soldier.WaitUntil(1);

        log.info("Step 2: Login and store the accessToken");
        response = requestSpecification.body(user).post(SSORoute.login); //user login
        userLoginResponse = response.getBody().as(UserLoginResponse.class); //map the response to pojo class
        accessToken = Soldier.GetValueByPath(userLoginResponse.data, "$.access_token");

        log.info("Step 3: General 2FA and store the otp_secrect and backupOtp");
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken)); //general requestSpect with accessToken
        response = requestWithToken.post(SSORoute.FAGeneral); // General 2FA code
        General2FAResponse general2FAResponse = response.getBody().as(General2FAResponse.class); //map the response to pojo class
        otp_secrect = Soldier.GetValueByPath(general2FAResponse.data, "$.otp_secret"); //store the otp_secrect code
        JSONArray backupOtp = (JSONArray) parser.parse(JsonPath.read(general2FAResponse.data.toString().replace("=", ":"), "$.backup_codes").toString()); //store the otp_secrect code

        log.info("Step 4: Enable 2FA ");
        twoFABody = new TwoFA("Truyen1234@", Soldier.getOTP(otp_secrect));
        requestWithToken = requestSpec().headers(SetHeader.SetHeaderWithAccessToken(accessToken));
        response = requestWithToken.body(twoFABody).put(SSORoute.FAVerify);

        log.info("Step 5: Login after enable 2FA and get the token2FA ");
        response = requestSpecification.body(user).post(SSORoute.login); //login to get token2fa
        userLoginResponse = response.getBody().as(UserLoginResponse.class);
        token2FA = Soldier.GetValueByPath(userLoginResponse.data, "$.token");

        log.info("Step 6: Declare the loginBackup with token2Fa and backupOTP");
        user.setUser(new LoginBackup(token2FA, backupOtp.get(1).toString()));
        response = requestSpecification.body(user).post(SSORoute.login2FA);
        userLoginResponse = response.getBody().as(UserLoginResponse.class);

        log.info("Step 7: Veirfy the response inclide status, messag and email");
        Assert.assertEquals(userLoginResponse.status, "success");
        Assert.assertEquals(userLoginResponse.message, "success");
        Assert.assertEquals(Soldier.GetValueByPath(userLoginResponse.data, "$.email"), loginInfor.getLogin());
    }

}