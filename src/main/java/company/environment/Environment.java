package company.environment;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/*
* By one the command line: mvn clean test -Denvir='dev', this class will find the environment file name = 'dev'
* in the folder "src/main/java/ce/environment/" to get any information in the file.
*
* */


@Sources({"file:src/main/java/company/environment/${env}.properties"})
public interface Environment extends Config {
    String dbUrl();
    String dbUser();
    String dbPass();
    String dbPort();
    String ssoUrl();
    String ssoDBName();
    String cewlst();
}
