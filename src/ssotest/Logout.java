package ssotest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet(description = "Logout Servlet", urlPatterns = { "/logout" })
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session != null ) {
			session.invalidate();
		}
		String dbName = "jdbc:postgresql://localhost/sessiondb";
		String dbDriver = "org.postgresql.Driver";
		try {
			Class.forName(dbDriver);
			Connection con = DriverManager.getConnection(dbName, "sessiondb", null);
			System.out.println("Got Connected..");
    		String dsql = "delete from django_session where session_key like ?";
    		PreparedStatement statement = con.prepareStatement(dsql);
        	statement.setString(1, session.getId());
        	int num = statement.executeUpdate();
        	System.out.println("Executedddd ====>"+num);
        }catch(Exception e){
           e.printStackTrace();
        }
		request.setAttribute("source", "logout");
		request.getServletContext().getRequestDispatcher("/login").forward(request, response);
		// response.sendRedirect("/OsintIDATest/login");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
