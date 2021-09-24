import java.sql.*;

public class Component {

    String urlDB = "jdbc:mysql://localhost:3306/testdb?user=root&password=pluralsight&serverTimezone=UTC";


    // create connect method that verifies connection to DB -testDB
    public String tryConnection() throws Exception {
        try (Connection conn = DriverManager
                .getConnection(urlDB)) {
            return conn.isValid(2) ? "Connected successfully to database" : "Connection failed";
        }
    }

    // create query method that gets all rows from -testDB
    public void browseAllRows() throws Exception {
        try (Connection conn = DriverManager
                .getConnection(urlDB);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM user_login");
             ResultSet result = statement.executeQuery();) {


            while (result.next()) {
                int userId = result.getInt("user_login_id");
                String username = result.getString("user_login_username");
                String userPassword = result.getString("user_login_password");
                String userEmail = result.getString("user_login_email");

                System.out.println("Printing user registration data:");
                System.out.println("user login id: " + userId);
                System.out.println("user login username: " + username);
                System.out.println("user login password: " + userPassword);
                System.out.println("user login email: " + userEmail);
                System.out.println("end of data.\n");
            }
        }


    }



    //browse only all usernames
    public void browseUsernames() throws Exception {
        try (Connection conn = DriverManager.getConnection(urlDB);
        PreparedStatement statement = conn.prepareStatement("SELECT user_login_username " +
                                                                "FROM user_login")) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String user = result.getString("user_login_username");
                System.out.println(user);
            }
        }
    }


    // check username availability
    public boolean usernameFound(String username) throws Exception {
        try (Connection conn = DriverManager
                .getConnection(urlDB);
             PreparedStatement statement = conn.prepareStatement("SELECT user_login_username " +
                                                                     "FROM user_login " +
                                                                     "WHERE user_login_username = ?")) {

            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            boolean found = false;

            while (result.next()) {
                if (result.getString("user_login_username").equals(username)) {
                    found = true;
                    break;
                }
            }
            return found;
        }
    }


    // create insert method that inserts new username, email and password
    public void addUser(User user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword(); // NEED TO ENCRYPT/DECRYPT PASSWORD?
        String email = user.getEmail();

        if (usernameFound(username)) { // checks if username is available, if not: return and exit method call.
            System.out.println("Username already exists for: " + username);
            return;
        }


        try (Connection conn = DriverManager
                .getConnection(urlDB);
             PreparedStatement statement = conn.prepareStatement("INSERT INTO " +
                                                                     "user_login(" +
                                                                     "user_login_username," +
                                                                     "user_login_password," +
                                                                     "user_login_email) " +
                                                                     "VALUES(?,?,?)");) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.executeUpdate();
            System.out.println("Added user: " + username);

        }
    }

    // create login method for users that check username with password
    public String loginUser(String username, String password) throws Exception {
        if (!usernameFound(username)) // verifies that username exists
            return "Sorry, username not found!";

        // do query to find user password and match it with input
        try (Connection conn = DriverManager.getConnection(urlDB);
        PreparedStatement statement = conn.prepareStatement("SELECT user_login_password " +
                                                                "FROM user_login " +
                                                                "WHERE user_login_username = ?")) {

            statement.setString(1,username);
            ResultSet result = statement.executeQuery();


            boolean pwFound = false;
            while (result.next()) {
                if (result.getString("user_login_password").equals(password)) {
                    pwFound = true;
                    break;
                }
            }


            return pwFound ? "Logging in user: " + username : "Incorrect password";
        }
    }
}
