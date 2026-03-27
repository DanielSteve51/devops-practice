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

    private static final String RESULT_OPEN  = "<h2>Result: ";
    private static final String RESULT_CLOSE = "</h2>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int a = Integer.parseInt(request.getParameter("a"));
            int b = Integer.parseInt(request.getParameter("b"));
            String op = request.getParameter("op");

            if (op.equals("Add")) {
                out.println(RESULT_OPEN + (a + b) + RESULT_CLOSE);
            } else if (op.equals("Subtract")) {
                out.println(RESULT_OPEN + (a - b) + RESULT_CLOSE);
            } else if (op.equals("Multiply")) {
                out.println(RESULT_OPEN + (a * b) + RESULT_CLOSE);
            } else if (op.equals("Divide")) {
                if (b == 0) {
                    out.println("<h3>Cannot divide by zero</h3>");
                } else {
                    out.println(RESULT_OPEN + ((double) a / b) + RESULT_CLOSE);
                }
            } else if (op.equals("Modulus")) {
                if (b == 0) {
                    out.println("<h3>Cannot mod by zero</h3>");
                } else {
                    out.println(RESULT_OPEN + (a % b) + RESULT_CLOSE);
                }
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Not a number</h3>");
        } catch (Exception e) {
            out.println("<h3>Unknown exception</h3>");
        }
    }
}