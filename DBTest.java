import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;

public class DBTest {
    public static void main(String[] args) throws SQLException {
        String Username = "mvpanis";
        String mysqlPassword = "";
        int choice = -1; 
        Scanner in = new Scanner(System.in);
        Database db = new Database();
        db.connect(Username, mysqlPassword);  
        db.initDatabase(Username, mysqlPassword, Username);
        String query;
        while (choice != 6) {
            System.out.println("1) Find all agents and clients in a given city");
            System.out.println("2) Add a new client, then purchase an available policy from a particular agent");
            System.out.println("3) List all policies sold by a particular agent");
            System.out.println("4) Cancel a policy");
            System.out.println("5) Add a new agent to a city");
            System.out.println("6) Quit");
            choice = in.nextInt();
            if (choice == 1) {
                db.item1();
            }
            else if (choice == 2) {
                db.item2();
            }
            else if (choice == 3) {
                db.item3();
            }
            else if (choice == 4) {
                db.item4();
            }
            else if (choice == 5) {
                db.item5();
            }
            else if (choice == 6) {
                db.item6();
            }
        }

    }
}