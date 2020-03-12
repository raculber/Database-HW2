import java.sql.*;

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

        insert("POLICIES_SOLD","401,204,106,303,'2020-01-02',2000.00");
        insert("POLICIES_SOLD","402,201,105,305,'2019-08-11',1500.00");
        insert("POLICIES_SOLD","403,203,106,301,'2019-09-11',3000.00");
        insert("POLICIES_SOLD","404,207,101,305,'2019-06-21',1500.00");
        insert("POLICIES_SOLD","405,203,104,302,'2019-11-14',4500.00");
        insert("POLICIES_SOLD","406,207,105,305,'2019-12-25',1500.00");
    }
}