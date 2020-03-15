import java.sql.*;
import java.util.Scanner;

public class Database {
    private Connection connection;
    private Statement statement;
    public Database() {
        connection = null;
        statement = null;
    }
    public void connect(String Username, String mysqlPassword) throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + Username + "?" +
            "user=" + Username + "&password=" + mysqlPassword);
        }
        catch(Exception e) {
            throw e;
        }
    }
    public void disconnect() throws SQLException {
        connection.close();
        statement.close();
    }
    public void query(String q) {
        try {
            ResultSet resultSet = statement.executeQuery(q);
            System.out.println("\n---------------------------------");
            System.out.println("Query: \n" + q + "\n\nResult: ");
            print(resultSet);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        printHeader(metaData, numColumns);
        printRecords(resultSet, numColumns);
    }
    public void printHeader(ResultSetMetaData metaData, int numColumns) throws SQLException {
        for (int i = 1; i <= numColumns; i++) {
            if (i > 1)
                System.out.print(",  ");
            System.out.print(metaData.getColumnName(i));
        }
        System.out.println();
    }
    public void printRecords(ResultSet resultSet, int numColumns) throws SQLException {
        String columnValue;
        while (resultSet.next()) {
            for (int i = 1; i <= numColumns; i++) {
                if (i > 1)
                    System.out.print(",  ");
                columnValue = resultSet.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }
    }
    public void insert(String table, String values) {
        String query = "INSERT into " + table + " values (" + values + ")" ;
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void initDatabase(String Username, String Password, String SchemaName) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("DELETE from POLICIES_SOLD");
        statement.executeUpdate("DELETE from CLIENTS");
        statement.executeUpdate("DELETE from AGENTS");
        statement.executeUpdate("DELETE from POLICY");

        insert("CLIENTS","101,'CHRIS','DALLAS',43214");
        insert("CLIENTS","102,'OLIVIA','BOSTON',83125");
        insert("CLIENTS","103,'ETHAN','FAYETTEVILLE',72701");
        insert("CLIENTS","104,'DANIEL','NEWYORK',53421");
        insert("CLIENTS","105,'TAYLOR','ROGERS',78291");
        insert("CLIENTS","106,'CLAIRE','PHOENIX',85011");

        insert("AGENTS","201,'ANDREW','DALLAS',43214");
        insert("AGENTS","202,'PHILIP','PHOENIX',85011");
        insert("AGENTS","203,'JERRY','BOSTON',83125");
        insert("AGENTS","204,'BRYAN','ROGERS',78291");
        insert("AGENTS","205,'TOMMY','DALLAS',43214");
        insert("AGENTS","206,'BRANT','FAYETTEVILLE',72701");
        insert("AGENTS","207,'SMITH','ROGERS',78291");

        insert("POLICY","301,'CIGNAHEALTH','DENTAL',5");
        insert("POLICY","302,'GOLD','LIFE',8");
        insert("POLICY","303,'WELLCARE','HOME',10");
        insert("POLICY","304,'UNITEDHEALTH','HEALTH',7");
        insert("POLICY","305,'UNITEDCAR','VEHICLE',9");

        insert("POLICIES_SOLD","401,204,106,303,STR_To_DATE('02,01,2020', '%d,%m,%Y'),2000.00");
        insert("POLICIES_SOLD","402,201,105,305,STR_To_DATE('11,08,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD","403,203,106,301,STR_To_DATE('11,09,2019', '%d,%m,%Y'),3000.00");
        insert("POLICIES_SOLD","404,207,101,305,STR_To_DATE('21,06,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD","405,203,104,302,STR_To_DATE('14,11,2019', '%d,%m,%Y'),4500.00");
        insert("POLICIES_SOLD","406,207,105,305,STR_To_DATE('25,12,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD","407,205,103,304,STR_To_DATE('15,10,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD","408,204,103,304,STR_To_DATE('15,02,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD","409,203,103,304,STR_To_DATE('10,01,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD","410,202,103,303,STR_To_DATE('30,01,2020', '%d,%m,%Y'),2000.00");
    }
    public void item1() {
        Scanner in = new Scanner(System.in);
        String city;
        System.out.println("Enter a city:");
        in.nextLine(); 
        city = in.nextLine();
        String query = "SELECT * FROM CLIENTS WHERE C_CITY = " + city + ";";
        query(query);
        query = "SELECT * FROM AGENTS WHERE A_CITY = " + city + ";";
        query(query);
    }
    public void item2() {
        Scanner in = new Scanner(System.in);
        String name, city, type;
        int zip, id;
        double amount;
        System.out.println("Enter your name:");
        in.nextLine();
        name = in.nextLine();
        System.out.println("Enter your city:");
        in.nextLine();
        city = in.nextLine();
        System.out.println("Enter your zip code:");
        zip = in.nextInt();
        String values = name + "," + city + "," + zip;
        insert("CLIENTS", values);
        System.out.println("Enter the policy type:");
        in.nextLine();
        type = in.nextLine();
        String query = "SELECT * FROM AGENTS WHERE A_CITY = " + city + ";";
        query(query);
        query = "SELECT * FROM POLICY;";
        query(query);
        System.out.println("Enter the policy id:");
        id = in.nextInt();
        System.out.println("Enter the policy amount:");
        amount = in.nextDouble();

    }
    public void item3() {
        Scanner in = new Scanner(System.in);
        String name, city;
        System.out.println("Enter the agent's name:");
        in.nextLine();
        name = in.nextLine();
        System.out.println("Enter the agent's city:");
        in.nextLine();
        city = in.nextLine();
        String query = "SELECT * FROM POLICIES_SOLD" + 
        "WHERE AGENT_ID = (SELECT A_ID FROM" + 
        "AGENTS WHERE A_NAME = " + name + ");";
        query(query);
        query = "SELECT NAME, TYPE, COMMISSION_PERCENTAGE " + 
        "FROM POLICY " + 
        "WHERE POLICY_ID IN (SELECT POLICY_ID FROM " +  
        "POLICIES_SOLD, AGENTS " + 
        "WHERE AGENT_ID = A_ID " + 
        "AND A_NAME = " + name + ");"; 
        query(query);
    }
    public void item4() {
        Scanner in = new Scanner(System.in);
        String query = "SELECT * FROM POLICIES_SOLD;";
        int id;
        System.out.println("Enter the policy id:");
        id = in.nextInt();
        query = "DELETE FROM POLICIES_SOLD WHERE POLICY_ID =" + id + ";";
        query(query);
    }
    public void item5() {
        Scanner in = new Scanner(System.in);
        int id, zip;
        String name, city;
        System.out.println("Enter the agent's ID:");
        id = in.nextInt();
        System.out.println("Enter the agent's name:");
        in.nextLine();
        name = in.nextLine();
        System.out.println("Enter the agent's city:");
        in.nextLine();
        city = in.nextLine();
        System.out.println("Enter the agent's zip:");
        zip = in.nextInt();
        String values = id + "," + name + "," + city + "," + zip;
        insert("AGENTS", values);
        String query = "SELECT * FROM AGENTS WHERE A_CITY = " + city + ";";
        query(query);
    }
}