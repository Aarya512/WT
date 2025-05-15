<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>eBookShop Management</title>
</head>
<body>
    <h2>Welcome to Sanjivani eBookShop</h2>

    <!-- Form Section -->
    <h3>Insert / Update / Delete Book</h3>
    <form method="post">
        Book ID: <input type="text" name="id" required><br>
        Title: <input type="text" name="title"><br>
        Quantity: <input type="number" name="qty"><br>
        Price: <input type="number" step="0.01" name="price"><br>
        Author: <input type="text" name="author"><br><br>
        <input type="submit" name="action" value="Insert">
        <input type="submit" name="action" value="Update">
        <input type="submit" name="action" value="Delete">
    </form>
    <hr>

<%
    String action = request.getParameter("action");
    String idStr = request.getParameter("id");

    if (action != null && idStr != null && !idStr.isEmpty()) {
        try {
            int id = Integer.parseInt(idStr);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjivani", "root", "");

            if ("Insert".equals(action)) {
                String title = request.getParameter("title");
                int qty = Integer.parseInt(request.getParameter("qty"));
                double price = Double.parseDouble(request.getParameter("price"));
                String author = request.getParameter("author");

                PreparedStatement ps = con.prepareStatement("INSERT INTO ebookshop (id, title, qty, price, author_name) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, title);
                ps.setInt(3, qty);
                ps.setDouble(4, price);
                ps.setString(5, author);
                ps.executeUpdate();
                ps.close();
                out.println("<p style='color:green;'>Inserted successfully!</p>");

            } else if ("Update".equals(action)) {
                String title = request.getParameter("title");
                int qty = Integer.parseInt(request.getParameter("qty"));
                double price = Double.parseDouble(request.getParameter("price"));
                String author = request.getParameter("author");

                PreparedStatement ps = con.prepareStatement("UPDATE ebookshop SET title=?, qty=?, price=?, author_name=? WHERE id=?");
                ps.setString(1, title);
                ps.setInt(2, qty);
                ps.setDouble(3, price);
                ps.setString(4, author);
                ps.setInt(5, id);
                ps.executeUpdate();
                ps.close();
                out.println("<p style='color:blue;'>Updated successfully!</p>");

            } else if ("Delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM ebookshop WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                ps.close();
                out.println("<p style='color:red;'>Deleted successfully!</p>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
%>

    <!-- Display Table Section -->
    <h3>Available Books</h3>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr><th>ID</th><th>Title</th><th>Qty</th><th>Price</th><th>Author</th></tr>
<%
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjivani", "root", "");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ebookshop");

        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getInt("id") + "</td>");
            out.println("<td>" + rs.getString("title") + "</td>");
            out.println("<td>" + rs.getInt("qty") + "</td>");
            out.println("<td>" + rs.getDouble("price") + "</td>");
            out.println("<td>" + rs.getString("author_name") + "</td>");
            out.println("</tr>");
        }

        rs.close();
        stmt.close();
        con.close();
    } catch (Exception e) {
        out.println("<p style='color:red;'>Error fetching books: " + e.getMessage() + "</p>");
    }
%>
    </table>
</body>
</html>
