package ssotest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		String source = (String)request.getAttribute("source");
		PrintWriter writer = response.getWriter();
		if(source != null && source.equals("logout")) {
			writer.append("You have been logged out successfully. <br/>");
		}
		writer
			.append("<form action='/OsintIDATest/login' method='POST'>")
			.append("<input name='username' type='text'/>")
			.append("<button type='submit'>Login</button>")
			.append("</form>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		session.setAttribute("username", request.getParameter("username"));
		response.sendRedirect("/OsintIDATest/homepage");
		//doGet(request, response);
	}

}
