package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:6000";
        String username = "mlcaurorauser";
        String password = "CrypticDefaultPasswordVariable42";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            checkSumTables(conn);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void checkSumTables(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            List<String> tables = new ArrayList<>();
            ResultSet rsTables = stmt.executeQuery("SELECT table_schema, table_name FROM " +
                    "information_schema.tables where table_schema in " +
                    "('mlc_ddo_documents', 'mlc_yfys_microsites', " +
                    "'mlcaurorauser', 'self_service_forms', " +
                    "'user_activity_log', 'wrapaurorauser')");
            while (rsTables.next()) {
                tables.add(rsTables.getString(1) + "." + rsTables.getString(2));
            }
            tables.forEach(table -> {
                try {
                    ResultSet checksum = stmt.executeQuery("checksum table " + table);
                    if(checksum.next()) {
                        System.out.println("Table: " + checksum.getString(1) + " , Checksum: " + checksum.getString(2));
                    }
                } catch (SQLException e) {
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch(SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }


    }
}