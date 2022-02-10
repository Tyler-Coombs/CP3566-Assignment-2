package com.example.assignment2jan27;
import com.example.assignment2jan27.DBConnection;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "LibraryData", value = "/LibraryData")
public class LibraryData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Connection conn;
        PreparedStatement pstmt;
        String requestType = request.getParameter("type");

        out.println("<html><body>");
        out.println("<h1>" + request.getParameter("type") + "</h1>");

        if (requestType.equals("ViewBooks")) {
            try {
                conn = DBConnection.initDatabase();
                assert conn != null;
                pstmt = conn.prepareStatement("SELECT * FROM titles");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Book book = new Book(rs.getString("title"), rs.getString("copyright"), rs.getInt("editionNumber"), rs.getString("isbn"));
                    pstmt = conn.prepareStatement("SELECT * FROM authors a INNER JOIN authorISBN i " +
                            "ON a.authorID = i.authorID INNER JOIN titles t " +
                            "ON i.isbn = t.isbn WHERE t.title = ?");
                    pstmt.setString(1, book.getTitle());
                    ResultSet rs2 = pstmt.executeQuery();
                    List<Author> authorListTemp = new ArrayList<>();
                    while (rs2.next()) {
                        authorListTemp.add(new Author(rs2.getString("lastName"), rs2.getString("firstName"), rs2.getInt("authorID")));
                    }
                    book.setAuthorList(authorListTemp);
                    books.add(book);
                }
                conn.close();
            } catch (SQLException | ClassNotFoundException e) {
                out.println(e);
            }

            out.println("<table><tr><th>Title</th><th>Authors</th><th>Edition</th><th>Copyright</th><th>ISBN</th></tr>");
            for (Book book : books) {
                String authorList = "";
                int authorTracker = 0;
                for (Author author : book.getAuthorList()) {
                    if (authorTracker == 0) {
                        authorList += author.getFirstName() + " " + author.getLastName();
                    } else {
                        authorList += ", " + author.getFirstName() + " " + author.getLastName();
                    }
                    authorTracker++;
                }
                out.println("<tr><td>" + book.getTitle() + "</td><td>" + authorList + "</td><td>" + book.getEdition()
                        + "</td><td>" + book.getCopyright() + "</td><td>" + book.getISBN() + "</td><tr>");
            }

        }
        else if (request.getParameter("type").equals("ViewAuthors")) {
            try {
                conn = DBConnection.initDatabase();
                assert conn != null;
                pstmt = conn.prepareStatement("SELECT * FROM authors");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Author author = new Author(rs.getString("lastName"), rs.getString("firstName"), rs.getInt("authorID"));
                    pstmt = conn.prepareStatement("SELECT * FROM titles t INNER JOIN authorISBN i " +
                            "ON i.isbn = t.isbn INNER JOIN authors a " +
                            "ON a.authorID = i.authorID WHERE a.authorID = ?");
                    pstmt.setInt(1, author.getId());
                    ResultSet rs2 = pstmt.executeQuery();
                    List<Book> bookListTemp = new ArrayList<>();
                    while (rs2.next()) {
                        bookListTemp.add(new Book(rs2.getString("title"), rs2.getString("copyright"), rs2.getInt("editionNumber"), rs2.getString("isbn")));
                    }
                    author.setBookList(bookListTemp);
                    authors.add(author);
                }
                conn.close();
            } catch (SQLException | ClassNotFoundException e) {
                out.println(e);
            }

            out.println("<table><tr><th>Author ID</th><th>First Name</th><th>Last Name</th><th>Books</th></tr>");
            for(Author author : authors) {
                String bookList = "";
                int bookTracker = 0;
                for (Book book : author.getBookList()) {
                    if (bookTracker == 0) {
                        bookList += book.getTitle();
                    } else {
                        bookList += ", " + book.getTitle();
                    }
                    bookTracker ++;
                }
                out.println("<tr><td>" + author.getId() + "</td><td>" + author.getFirstName() + "</td><td>" + author.getLastName()
                        + "</td><td>" + bookList + "</td></tr>");
            }


        }
        out.println("<a href=\"index.jsp\">Home</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection conn;
        
        if (request.getParameter("type").equals("book")) {
            try {
                String title = request.getParameter("title");
                String authorFirstName = request.getParameter("firstName");
                String authorLastName = request.getParameter("lastName");
                String copyright = request.getParameter("copyright");
                int edition = Integer.parseInt(request.getParameter("edition"));
                String isbn = request.getParameter("isbn");

                conn = DBConnection.initDatabase();
                PreparedStatement pstm = conn.prepareStatement("INSERT into titles (isbn, title, editionNumber, copyright)" +
                        "VALUES (?, ?, ?, ?)");

                pstm.setString(1, isbn);
                pstm.setString(2, title);
                pstm.setInt(3, edition);
                pstm.setString(4, copyright);
                pstm.execute();

                pstm = conn.prepareStatement("INSERT INTO authors (firstName, lastName)" +
                        "VALUES (?, ?)");
                pstm.setString(1, authorFirstName);
                pstm.setString(2, authorLastName);
                pstm.execute();

                pstm = conn.prepareStatement("SELECT authorID FROM authors WHERE " +
                        "firstName = ? AND lastName = ?");
                pstm.setString(1, authorFirstName);
                pstm.setString(2, authorLastName);
                ResultSet rs = pstm.executeQuery();
                int authorID = 0;
                while (rs.next()) {
                    authorID = rs.getInt("authorID");
                }

                pstm = conn.prepareStatement("INSERT INTO authorISBN (authorID, isbn) " +
                        "VALUES (?, ?)");
                pstm.setInt(1, authorID);
                pstm.setString(2, isbn);
                pstm.execute();

                out.println("<html><body>");
                out.println("<h3>" + title + " has been added to the database.</h3>");
                out.println("<br />");
                out.println("<a href=\"index.jsp\">Home</a>");
                out.println("</body></html>");

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (request.getParameter("type").equals("author")) {
            try {
                String authorFirstName = request.getParameter("firstName");
                String authorLastName = request.getParameter("lastName");


                conn = DBConnection.initDatabase();
                PreparedStatement pstm = conn.prepareStatement("INSERT INTO authors (firstName, lastName)" +
                        "VALUES (?, ?)");
                pstm.setString(1, authorFirstName);
                pstm.setString(2, authorLastName);
                pstm.execute();

                out.println("<html><body>");
                out.println("<h3>" + authorFirstName + " " + authorLastName + " has been added to the database.</h3>");
                out.println("<br />");
                out.println("<a href=\"index.jsp\">Home</a>");
                out.println("</body></html>");

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
