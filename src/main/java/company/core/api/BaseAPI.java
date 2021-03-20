package company.core.api;

import company.core.jdbcPostgree.PostGresDB;
import company.environment.Environment;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseAPI {
    protected final Log log;
    RequestSpecification requestSpecification;
    String env = System.getProperty("envir");
    static String lang = System.getProperty("lang");
    PostGresDB db = new PostGresDB();

    protected BaseAPI(){
        log = LogFactory.getLog(getClass());
    }

    protected RequestSpecification requestSpec() {

        ConfigFactory.setProperty("env", env);
        Environment environment = ConfigFactory.create(Environment.class);
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri(environment.ssoUrl());
        requestSpecification.queryParam("lang", lang);
        return requestSpecification;
    }

    protected void DisConnect(){
        db.CloseConnection();
    }
}
