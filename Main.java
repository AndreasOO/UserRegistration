import java.sql.*;


public class Main {
    public static void main(String[] args) {

    Component component = new Component();

    try {

        System.out.println(component.tryConnection());

        //component.browseAllRows();
        component.browseUsernames();

        System.out.println(component.loginUser("Example5", "12345"));
        //User user3 = new User("Example5", "12345", "example5@mail.com");
        //component.addUser(user3);


    }
    catch (Exception e) { // can be further developed with more precise SQL exceptions
        System.out.println(e);
        }
    }

}


