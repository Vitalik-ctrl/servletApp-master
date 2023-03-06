package com.example.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    public static final String queryByCountry = "select * from users where country=?";
    public static final String queryAll = "select * from users";
    public static final String queryUpdateName = "update users set name=? where id=?";


    public static void main(String[] args) {
        getConnection();

        Employee employee = new Employee();

        employee.setName("Van Kalashnikov");
        employee.setEmail("kalash@gmail.com");
        employee.setCountry("Ukraine");
        save(employee);
    }

    public static Connection getConnection() {

        Connection connection = null;
        String url = "jdbc:postgresql://localhost:5432/employee";
        String user = "postgres";
        String password = "postgres";

        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
        return connection;
    }

    public static ResultSet getSQLQuery(String sql, String params) throws SQLException {
        Connection connection = EmployeeRepository.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        if (!params.equals("")) {
            ps.setString(1, params);
        }
        ResultSet resultSet = ps.executeQuery();
        connection.close();
        return resultSet;
    }

    public static int getSQL(String sqlQuery, String name, int id) throws SQLException {
        int status = 0;
        Connection connection = EmployeeRepository.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setString(1, name);
        ps.setInt(2, id);
        status = ps.executeUpdate();
        connection.close();
        return status;
    }

    public static int save(Employee employee) {
        int status = 0;
        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("insert into users(name,email,country) values (?,?,?)");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getCountry());

            status = ps.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static int update(Employee employee) {

        int status = 0;

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("update users set name=?,email=?,country=? where id=?");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getCountry());
            ps.setInt(4, employee.getId());

            status = ps.executeUpdate();
            connection.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return status;
    }

    public static int delete(int id) {

        int status = 0;

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("delete from users where id=?");
            ps.setInt(1, id);
            status = ps.executeUpdate();

            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public static Employee getEmployeeById(int id) {

        Employee employee = new Employee();

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from users where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setEmail(rs.getString(3));
                employee.setCountry(rs.getString(4));
            }
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return employee;
    }

    public static List<Employee> getEmployeesByCountry(String country) throws SQLException {
        ResultSet rs = getSQLQuery(queryByCountry, country);
        return employeeList(rs);
    }

    public static List<Employee> getAllEmployees() throws SQLException {
        String params = "";
        ResultSet rs = getSQLQuery(queryAll, params);
        return employeeList(rs);
    }

    public static List<Employee> employeeList(ResultSet rs) throws SQLException {
        List<Employee> listEmployees = new ArrayList<>();
        while (rs.next()) {
            listEmployees.add(new Employee(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return listEmployees;
    }

    public static int updateName(String name, int id) throws SQLException {
        int status = 0;
        status = getSQL(queryUpdateName, name, id);
        return status;
    }
}
