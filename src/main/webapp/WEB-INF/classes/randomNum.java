import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebServlet("/randomNum")
public class randomNum extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String name,pass;
    public void service(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
    	//res.setContentType("text/html;charset=UTF-8"); 
		name=req.getParameter("name");
		pass=req.getParameter("password");
		Connection connection = null;
        Statement stmt=null;
        int login=1;
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/guessNum", "postgres", "Sairam@1215");
            stmt = connection.createStatement();
            String sql1= "select * from signup where name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql1); 
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("1");
            if(rs.next()) {
            	if(rs.getString("password").trim().equals(pass)) {
            		System.out.println("True");
            		login=1;
            	}
            	else {
            		login = 0;
            	}
            }
            else {
				stmt = connection.createStatement();
				String sql = "INSERT INTO signup(name,password) VALUES('"+name+"','"+pass+"');";
	            stmt.executeUpdate(sql);
            	login=1;
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
    	int max=9999;
		int min=1000;
		int ran =0;
		HttpSession session = req.getSession(true);
		char[] result;
		if (session.isNew()) {
			Random r = new Random();
			ran=r.nextInt(min, max+1);
			session.setAttribute("random", ran);
			result= new char[4];
			//result = new String[4]; 
			session.setAttribute("result", result);
			System.out.println(ran);

		}
		else {
			ran = (int)session.getAttribute("random");
			result=(char[])session.getAttribute("result");
			System.out.println(ran);
		}
		System.out.println(login);
		if(login!=0)
		{
		res.sendRedirect("game.html");
		}
		else {
			res.sendRedirect("index.html");
		}
		
    }
}
    

