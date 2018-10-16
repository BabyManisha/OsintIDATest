package ssotest;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

/**
 * Servlet Filter implementation class SessionFiliter
 */
@WebFilter("/homepage")
public class SessionFiliter implements Filter {

    /**
     * Default constructor. 
     */
    public SessionFiliter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpServletResponse hresponse = (HttpServletResponse) response;

		HttpSession session = hrequest.getSession();
		if(session != null && session.getAttribute("username") != null) {
			
			System.out.println("Session ID:" + session.getId());	
			
			//String dbName = "jdbc:postgresql://localhost/mysessions";
			String dbName = "jdbc:postgresql://localhost/sessiondb";
			String dbDriver = "org.postgresql.Driver";
			try {
				Class.forName(dbDriver);
				//Connection con = DriverManager.getConnection(dbName, "postgres", null);
				Connection con = DriverManager.getConnection(dbName, "sessiondb", null);
				System.out.println("Got Connected..");
	        	String sql = "select * from django_session where session_key= ? ";
	        	PreparedStatement statement = con.prepareStatement(sql);
	        	statement.setString(1, session.getId());
	        	ResultSet rs = statement.executeQuery();
	        	if(rs.next()) {
			        System.out.println(rs.getObject(1));
	        	}else {
	        		String isql = "insert into django_session(session_key, session_data, expire_date) values(?, ?, ?)";
		        	statement = con.prepareStatement(isql);
		        	statement.setString(1, session.getId());
		        	JSONObject obj = new JSONObject();
		        	JSONObject appsObj = new JSONObject();
		        	appsObj.put("app_name", "MyAppName");
		        	appsObj.put("apptoken", "MyAppToken");
		        	appsObj.put("schema_name", (String)session.getAttribute("username"));
		        	JSONObject appsTokenObj = new JSONObject();
		        	appsTokenObj.put((String)session.getId(), appsObj);
		        	JSONObject tenantObj = new JSONObject();
		        	tenantObj.put("username", (String)session.getAttribute("username"));
		        	tenantObj.put("usertoken", "UserTokenData");
		        	tenantObj.put("schema_name", (String)session.getAttribute("username"));
		        	tenantObj.put("tenant_config", "TenantConfigData");
		        	obj.put("apps", appsTokenObj);
		        	obj.put("username", (String)session.getAttribute("username"));
		        	obj.put("tenant_details", tenantObj);
		        	
		        	StringWriter out = new StringWriter();
		        	obj.writeJSONString(out);
		        	String jsonText = out.toString();
		        	
		        	String session_data = session.getId()+":"+jsonText;
		        	System.out.println("session_data is::==>>"+ session_data);
		        	
		        	String session_endata = new String(Base64.getEncoder().encode(session_data.getBytes()));
		        	statement.setString(2, session_endata);
		        	statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()+30*60*1000));
		        	int num = statement.executeUpdate();
		        	System.out.println("Executedddd ====>"+num);
	        	}
		        
	        }catch(Exception e){
	           e.printStackTrace();
	        }

			
			
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}else {
			hresponse.sendRedirect("/OsintIDATest/login");
		}
		

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
