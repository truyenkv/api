package company.core.Util;

import java.io.*;
import java.util.Properties;

public class LoadMessage {
    private static final String CurrentFolder = "src/main/java/company/data/";

    public static String getMessage(String lang, String key) {
        Properties properties = new Properties();
        File input = new File(CurrentFolder+ lang +".properties");
        try {
            properties.load(new InputStreamReader(new FileInputStream(input),"UTF-8"));
        } catch (IOException e) {
            System.out.println("Load file .properties unsuccessful");
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }


}
