package com.example.demo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/updateEmployeeNameServlet")
public class UpdateEmployeeNameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sid = request.getParameter("id");
        String name = request.getParameter("name");
        int id = Integer.parseInt(sid);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        int status;
        try {
            status = EmployeeRepository.updateName(name, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (status > 0) {
            out.print("Record saved successfully!");
        } else {
            out.println("Sorry! unable to save record");
        }
        out.close();
    }
}
