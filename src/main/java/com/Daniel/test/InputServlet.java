package com.Daniel.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/divide")
public class InputServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int a = Integer.parseInt(request.getParameter("a"));
            int b = Integer.parseInt(request.getParameter("b"));
            out.println("<h2>Result: " + (a / b) + "</h2>");
        } catch (ArithmeticException e) {
            out.println("<h3>Cannot divide by zero</h3>");
        } catch (NumberFormatException e) {
            out.println("<h3>Not a number</h3>");
        } catch (Exception e) {
            out.println("<h3>Unknown exception</h3>");
        }
    }
}