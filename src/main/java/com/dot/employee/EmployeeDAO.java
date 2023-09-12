package com.dot.employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final String jdbcURL;
    private final String jdbcUser;
    private final String jdbcPassword;
    private Connection jdbcConnection;

    public EmployeeDAO(String jdbcURL, String jdbcUser, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public boolean insertEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (name, position, tax) VALUES (?, ?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, employee.getName());
        statement.setString(2, employee.getPosition());
        statement.setDouble(3, employee.getTax());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public List<Employee> listAllEmployees() throws SQLException {
        List<Employee> listEmployee = new ArrayList<>();

        String sql = "SELECT * FROM employee ORDER BY id ASC";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String position = resultSet.getString("position");
            double tax = resultSet.getDouble("tax");

            Employee employee = new Employee(id, name, position, tax);
            listEmployee.add(employee);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return listEmployee;
    }

    public boolean deleteEmployee(Employee employee) throws SQLException {
        String sql = "DELETE FROM employee where id = ?;" +
                "ALTER TABLE employee DROP id;" +
                "ALTER TABLE employee " +
                "ADD id SERIAL NOT NULL," +
                "ADD PRIMARY KEY (id)";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, employee.getId());

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET name = ?, position = ?, tax = ?";
        sql += " WHERE id = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, employee.getName());
        statement.setString(2, employee.getPosition());
        statement.setDouble(3, employee.getTax());
        statement.setInt(4, employee.getId());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public Employee getEmployee(int  id) throws SQLException {
        Employee employee = null;
        String sql = "SELECT * FROM employee WHERE id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String position = resultSet.getString("position");
            double tax = resultSet.getDouble("tax");

            employee = new Employee(id, name, position, tax);
        }

        resultSet.close();
        statement.close();

        return employee;
    }

}

