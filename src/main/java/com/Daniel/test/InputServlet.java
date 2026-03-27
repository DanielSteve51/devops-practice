package com.Daniel.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calculate")
public class InputServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int a = Integer.parseInt(request.getParameter("a"));
            int b = Integer.parseInt(request.getParameter("b"));
            String op = request.getParameter("op");
            int result;

            if (op.equals("Add")) {
                result = a + b;
                out.println("<h2>Result: " + result + "</h2>");
            } else if (op.equals("Subtract")) {
                result = a - b;
                out.println("<h2>Result: " + result + "</h2>");
            } else if (op.equals("Multiply")) {
                result = a * b;
                out.println("<h2>Result: " + result + "</h2>");
            } else if (op.equals("Divide")) {
                if (b == 0) {
                    out.println("<h3>Cannot divide by zero</h3>");
                } else {
                    out.println("<h2>Result: " + (double) a / b + "</h2>");
                }
            } else if (op.equals("Modulus")) {
                if (b == 0) {
                    out.println("<h3>Cannot mod by zero</h3>");
                } else {
                    result = a % b;
                    out.println("<h2>Result: " + result + "</h2>");
                }
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Not a number</h3>");
        } catch (Exception e) {
            out.println("<h3>Unknown exception</h3>");
        }
    }
}