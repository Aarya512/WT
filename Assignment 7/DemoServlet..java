

import jakarta.servlet.http.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*; 
import java.sql.*;

@WebServlet("/DemoServlet") // ✅ This is required to map the servlet
public class DemoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        pw.println("<html><head><title>eBookShop</title></head><body>");
        pw.println("<h2>Welcome to Sanjivani eBookShop</h2>");

        // Form for inserting, updating, deleting
        pw.println("<h3>Insert / Update / Delete Book</h3>");
        pw.println("<form method='post' action='DemoServlet'>");
        pw.println("Book ID: <input type='text' name='id' required><br>");
        pw.println("Title: <input type='text' name='title'><br>");
        pw.println("Quantity: <input type='number' name='qty'><br>");
        pw.println("Price: <input type='number' step='0.01' name='price'><br>");
        pw.println("Author: <input type='text' name='author'><br><br>");
        pw.println("<input type='submit' name='action' value='Insert'>");
        pw.println("<input type='submit' name='action' value='Update'>");
        pw.println("<input type='submit' name='action' value='Delete'>");
        pw.println("</form><br><hr>");

        // Display all books
        pw.println("<h3>Available Books</h3>");
        pw.println("<table border='1' cellpadding='5' cellspacing='0'>");
        pw.println("<tr><th>Book ID</th><th>Title</th><th>Quantity</th><th>Price</th><th>Author</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjivani", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ebookshop");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int qty = rs.getInt("qty");
                double price = rs.getDouble("price");
                String author = rs.getString("author_name");

                pw.println("<tr>");
                pw.println("<td>" + id + "</td>");
                pw.println("<td>" + title + "</td>");
                pw.println("<td>" + qty + "</td>");
                pw.println("<td>" + price + "</td>");
                pw.println("<td>" + author + "</td>");
                pw.println("</tr>");
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            pw.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }

        pw.println("</table>");
        pw.println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjivani", "root", "");

            if ("Insert".equals(action)) {
                String title = req.getParameter("title");
                int qty = Integer.parseInt(req.getParameter("qty"));
                double price = Double.parseDouble(req.getParameter("price"));
                String author = req.getParameter("author");

                PreparedStatement ps = con.prepareStatement("INSERT INTO ebookshop (id, title, qty, price, author_name) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, title);
                ps.setInt(3, qty);
                ps.setDouble(4, price);
                ps.setString(5, author);
                ps.executeUpdate();
                ps.close();

            } else if ("Update".equals(action)) {
                String title = req.getParameter("title");
                int qty = Integer.parseInt(req.getParameter("qty"));
                double price = Double.parseDouble(req.getParameter("price"));
                String author = req.getParameter("author");

                PreparedStatement ps = con.prepareStatement("UPDATE ebookshop SET title=?, qty=?, price=?, author_name=? WHERE id=?");
                ps.setString(1, title);
                ps.setInt(2, qty);
                ps.setDouble(3, price);
                ps.setString(4, author);
                ps.setInt(5, id);
                ps.executeUpdate();
                ps.close();

            } else if ("Delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM ebookshop WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                ps.close();
            }

            con.close();

        } catch (Exception e) {
            res.getWriter().println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }

        // Refresh the page after action
        res.sendRedirect("DemoServlet");
    }
}
