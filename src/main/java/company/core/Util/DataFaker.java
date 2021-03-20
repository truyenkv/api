package company.core.Util;

public class DataFaker {
    public static String firstName() {
        return "truyen" + Soldier.radome(10000);
    }

    public static String lastName() {
        return "kieu" + Soldier.radome(10000);
    }

    public static String email() {
        return firstName() + "@" + "yopmail.com";
    }
}
