package company.core.model.database;

public class PartnerUserTable {
    private static String id;
    private static String email;
    private static String name;
    private static String createdAt;
    private static String updateAt;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        PartnerUserTable.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        PartnerUserTable.email = email;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        PartnerUserTable.name = name;
    }

    public static String getCreatedAt() {
        return createdAt;
    }

    public static void setCreatedAt(String createdAt) {
        PartnerUserTable.createdAt = createdAt;
    }

    public static String getUpdateAt() {
        return updateAt;
    }

    public static void setUpdateAt(String updateAt) {
        PartnerUserTable.updateAt = updateAt;
    }

}
