import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/validate")
public class validate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int trial=0;
	int points=100;
	//StringBuffer result;
	char[] result = new char[4];  
	char[] color = new char[4];
	int redirect=0;
	PrintWriter out;
	public void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException { 
		String userGuessNumber = request.getParameter("gnum");
		System.out.println(userGuessNumber);
		int a=Integer.parseInt(userGuessNumber);
		Connection connection = null;
        Statement stmt=null;
        String un=randomNum.name;
        String up=randomNum.pass;
        //System.out.println(un);
        HttpSession session = request.getSession(true);
        result=(char[])session.getAttribute("result");
		int random = (int)session.getAttribute("random");
		if(a==random){
			trial+=1;
			out=response.getWriter();
			session.setAttribute("result", result);
			session.setAttribute("points",points );
			session.setAttribute("trial",trial );
			try{
	            Class.forName("org.postgresql.Driver");
	            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/guessNum", "postgres", "Sairam@1215");
	            if (connection != null) {
	                System.out.println("Connection OK");
	            } else {
	                System.out.println("Connection Failed");
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }
			try{
				if (un!=null) {
						stmt = connection.createStatement();
						String sql = "INSERT INTO login(name,score,guess) VALUES('"+un+"',"+points+","+trial+");";
	                    stmt.executeUpdate(sql);
				}
				else {
					System.out.println("Hi," + un + " user does not exist");
		        }
			}catch(Exception e){
				System.out.println(e);
			}finally {
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {};
			    try { if (connection != null) connection.close(); } catch (Exception e) {};
			}
			redirect=1;
			out.println("OOHOO!YOU WON THE GAME\n"+"NO:OF:TRIALS:  "+trial+"\n"+"POINTS:  "+points);
			session.invalidate();
			
		}
		else{
			trial+=1;
			String k=String.valueOf(random);
			String s = Integer.toString(a);
			for(int ind=0;ind<k.length();ind++) {
				if(k.charAt(ind)==s.charAt(ind)) {
	                result[ind]=(s.charAt(ind));
					color[ind]='G';
				}
				else if(k.contains(Character.toString(s.charAt(ind)))) {
					points-=2;
					result[ind]=(s.charAt(ind));
					color[ind]='B';
				}
				else {
					points-=5;
					result[ind]=(s.charAt(ind));
					color[ind]='R';
				}
			}

			session.setAttribute("result", result);
			System.out.println(result);
            doPost(request, response);
			//response.sendRedirect("start.html");
		}
	}
     
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		if(redirect!=1) {
		response.setContentType("text/plain");
		String res=request.getParameter("gnum");
		PrintWriter out=response.getWriter();
		String a=new String(result);
		String b=new String(color);
		String x=a +"*"+ b;
		out.print(x);
		// out.print(color);
		}
	}

}
