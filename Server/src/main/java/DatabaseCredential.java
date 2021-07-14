public class DatabaseCredential {
    private static final String URL = "jdbc:mariadb://localhost:3306/DataBase";
    private static final String user = "root";
    private static final String password = "Kittymiki20@";

    public static String getURL() {
        return URL;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
