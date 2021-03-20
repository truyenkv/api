package company.core.Util;

import com.jayway.jsonpath.JsonPath;
import org.jboss.aerogear.security.otp.Totp;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Soldier {
    public static Timestamp timestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static int radome(int num){
        Random random = new Random();
        return random.nextInt(num);
    }

    public static String getOTP(String secrectKey){
        Totp otp = new Totp(secrectKey);
        String optNow = otp.now();
        return optNow;
    }

    public static void WaitUntil( int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String GetValueByPath(Object object, String path){
        return JsonPath.read(object.toString().replace("=",":"), path);
    }


}
