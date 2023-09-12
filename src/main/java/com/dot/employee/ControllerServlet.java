package com.dot.employee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmployeeDAO employeeDAO;

    public ControllerServlet() {

    }

    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUser = getServletContext().getInitParameter("jdbcUser");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        employeeDAO = new EmployeeDAO(jdbcURL, jdbcUser, jdbcPassword);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertEmployee(request, response);
                    break;
                case "/delete":
                    deleteEmployee(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateEmployee(request, response);
                    break;
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Employee> listEmployees = employeeDAO.listAllEmployees();
        request.setAttribute("listEmployees", listEmployees);
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("employee-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee existingEmployee = employeeDAO.getEmployee(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("employee-form.jsp");
        request.setAttribute("employee", existingEmployee);
        dispatcher.forward(request, response);

    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        double tax = Double.parseDouble(request.getParameter("tax"));

        Employee newEmployee = new Employee(name, position, tax);
        employeeDAO.insertEmployee(newEmployee);
        response.sendRedirect("/list");
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        double tax = Double.parseDouble(request.getParameter("tax"));

        Employee employee = new Employee(id, name, position, tax);
        employeeDAO.updateEmployee(employee);
        response.sendRedirect("/list");
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = new Employee(id);
        employeeDAO.deleteEmployee(employee);
        response.sendRedirect("/list");

    }

}
