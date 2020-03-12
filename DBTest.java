import java.sql.SQLException;
import java.util.Scanner;

public class DBTest {
    public static void main(String[] args) throws SQLException {
        String Username = "raculber";
        String mysqlPassword = "aib2Oi5L";
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
                String city;
                System.out.println("Enter a city:");
                city = in.nextLine();
                query = "SELECT * FROM CLIENTS WHERE C_CITY = " + city;
                db.query(query);
                query = "SELECT * FROM AGENTS WHERE A_CITY = " + city;
                db.query(query);
            }
            else if (choice == 2) {
                String name, city;
                int zip;
                System.out.println("Enter your name:");
                name = in.nextLine();
                System.out.println("Enter your city:");
                city = in.nextLine();
                System.out.println("Enter your zip code:");
                zip = in.nextInt();
                String values = name + "," + city + "," + zip;
                db.insert("CLIENTS", values);
            }
            else if (choice == 3) {

            }
            else if (choice == 4) {

            }
            else if (choice == 5) {
                
            }
            else if (choice == 6) {
                db.disconnect();
            }
        }

    }
}