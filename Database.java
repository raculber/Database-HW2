import java.sql.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
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
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + Username + "?" + "user=" + Username + "&password=" + mysqlPassword);
        } catch (Exception e) {
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
        } catch (SQLException e) {
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
        String query = "INSERT into " + table + " values (" + values + ")";
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

        insert("CLIENTS", "101,'CHRIS','DALLAS',43214");
        insert("CLIENTS", "102,'OLIVIA','BOSTON',83125");
        insert("CLIENTS", "103,'ETHAN','FAYETTEVILLE',72701");
        insert("CLIENTS", "104,'DANIEL','NEWYORK',53421");
        insert("CLIENTS", "105,'TAYLOR','ROGERS',78291");
        insert("CLIENTS", "106,'CLAIRE','PHOENIX',85011");

        insert("AGENTS", "201,'ANDREW','DALLAS',43214");
        insert("AGENTS", "202,'PHILIP','PHOENIX',85011");
        insert("AGENTS", "203,'JERRY','BOSTON',83125");
        insert("AGENTS", "204,'BRYAN','ROGERS',78291");
        insert("AGENTS", "205,'TOMMY','DALLAS',43214");
        insert("AGENTS", "206,'BRANT','FAYETTEVILLE',72701");
        insert("AGENTS", "207,'SMITH','ROGERS',78291");

        insert("POLICY", "301,'CIGNAHEALTH','DENTAL',5");
        insert("POLICY", "302,'GOLD','LIFE',8");
        insert("POLICY", "303,'WELLCARE','HOME',10");
        insert("POLICY", "304,'UNITEDHEALTH','HEALTH',7");
        insert("POLICY", "305,'UNITEDCAR','VEHICLE',9");

        insert("POLICIES_SOLD", "401,204,106,303,STR_To_DATE('02,01,2020', '%d,%m,%Y'),2000.00");
        insert("POLICIES_SOLD", "402,201,105,305,STR_To_DATE('11,08,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD", "403,203,106,301,STR_To_DATE('11,09,2019', '%d,%m,%Y'),3000.00");
        insert("POLICIES_SOLD", "404,207,101,305,STR_To_DATE('21,06,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD", "405,203,104,302,STR_To_DATE('14,11,2019', '%d,%m,%Y'),4500.00");
        insert("POLICIES_SOLD", "406,207,105,305,STR_To_DATE('25,12,2019', '%d,%m,%Y'),1500.00");
        insert("POLICIES_SOLD", "407,205,103,304,STR_To_DATE('15,10,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD", "408,204,103,304,STR_To_DATE('15,02,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD", "409,203,103,304,STR_To_DATE('10,01,2020', '%d,%m,%Y'),5000.00");
        insert("POLICIES_SOLD", "410,202,103,303,STR_To_DATE('30,01,2020', '%d,%m,%Y'),2000.00");
    }
    //Find all exisiting agents in a given city
    public void item1() {
        Scanner in = new Scanner(System.in);
        String city;
        System.out.println("Please enter a city: ");
        city = in.nextLine();
        String query = "SELECT * FROM CLIENTS WHERE C_CITY = \'" + city + "\';";
        query(query);
        query = "SELECT * FROM AGENTS WHERE A_CITY = \'" + city + "\';";
        query(query);
    }
    //Purchase an avaiable policy from a particular agent
    public void item2() throws SQLException {
        Scanner in = new Scanner(System.in);
        String name, city, type;
        int zip, policyId;
        double amount;
        System.out.println("Client table before insertion:");
        query("SELECT * FROM CLIENTS;");
        System.out.println("Please enter your name: ");
        name = in.nextLine();
        System.out.println("Please enter your city: ");
        city = in.nextLine();
        System.out.println("Please enter your zip code: ");
        zip = in.nextInt();
        String values = "\'" + name + "\',\'" + city + "\'," + zip;
        try {
            statement.executeUpdate("INSERT INTO CLIENTS (C_NAME, C_CITY, C_ZIP) VALUES (" + values + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Client table after insertion:");
        query("SELECT * FROM CLIENTS;");
        System.out.println("Please enter the policy type: ");
        in.nextLine();
        type = in.nextLine();
        int a_id = 0;
        String query = "SELECT * FROM AGENTS WHERE A_CITY = \'" + city + "\';";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                a_id = resultSet.getInt("A_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query(query);
        int c_id = 0;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CLIENTS;");
            while (resultSet.next()) {
                c_id = resultSet.getInt("C_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "SELECT * FROM POLICY;";
        query(query);
        System.out.println("Please enter the policy id: ");
        policyId = in.nextInt();
        System.out.println("Please enter the policy amount: ");
        amount = in.nextDouble();
        System.out.println("POLICIES_SOLD before insertion:");
        query("SELECT * FROM POLICIES_SOLD;");
        try {
            LocalDate date = java.time.LocalDate.now();
            statement.executeUpdate("INSERT INTO POLICIES_SOLD " + 
            "(AGENT_ID, CLIENT_ID, POLICY_ID, DATE_PURCHASED, AMOUNT) VALUES (" + a_id
             + "," + c_id + "," + policyId + ",\'" + date +
             "\'," + String.format("%.2f", amount) + ");"); 
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("POLICIES_SOLD after insertion:");
        query("SELECT * FROM POLICIES_SOLD;");
    }
    //List all policies sold by a particular agent
    public void item3() {
        Scanner in = new Scanner(System.in);
        String name, city;
        System.out.println("Please enter the agent's name: ");
        name = in.nextLine();
        System.out.println("Please enter the agent's city: ");
        city = in.nextLine();
        String query = "SELECT * FROM POLICIES_SOLD " + 
        "WHERE AGENT_ID = (SELECT A_ID FROM " + 
        "AGENTS WHERE A_NAME = \'" + name + "\');";
        query(query);
        query = "SELECT NAME, TYPE, COMMISSION_PERCENTAGE " + 
        "FROM POLICY " + 
        "WHERE POLICY_ID IN (SELECT POLICY_ID FROM " +  
        "POLICIES_SOLD, AGENTS " + 
        "WHERE AGENT_ID = A_ID " + 
        "AND A_NAME = \'" + name + "\');"; 
        query(query);
    }
    //cancel a policy
    public void item4() throws SQLException {
        Scanner in = new Scanner(System.in);
        String query = "SELECT * FROM POLICIES_SOLD;";
        query(query);
        int id;
        System.out.println("Please enter the purchase id: ");
        id = in.nextInt();
        query = "DELETE FROM POLICIES_SOLD WHERE PURCHASE_ID = " + id + ";";
        try {
            statement.executeUpdate(query); 
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("POLICIES_SOLD after deletion:");
        query("SELECT * FROM POLICIES_SOLD");
    }
    //Adding new agent for a city
    public void item5() {
        Scanner in = new Scanner(System.in);
        int id, zip;
        String name, city;
        System.out.println("Please enter the agent's ID: ");
        id = in.nextInt();
        System.out.println("Please enter the agent's name: ");
        in.nextLine();
        name = in.nextLine();
        System.out.println("Please enter the agent's city: ");
        city = in.nextLine();
        System.out.println("Please enter the agent's zip: ");
        zip = in.nextInt();
        System.out.println("Agent's in city before insertion: ");
        query("SELECT * FROM AGENTS WHERE A_CITY = \'" + city + "\';");
        String values = id + ",\'" + name + "\',\'" + city + "\'," + zip;
        insert("AGENTS", values);
        System.out.println("Agent's in city after insertion:");
        String query = "SELECT * FROM AGENTS WHERE A_CITY = \'" + city + "\';";
        query(query);
    }
    //Quit
    public void item6() throws SQLException {
        disconnect();
    }
}