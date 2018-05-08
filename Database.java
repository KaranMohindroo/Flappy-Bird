package flappybird;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Database 
{
	FlappyBird fb;
	String player,p;
	int score,highs;
	Database() throws Exception
	{}
	
	public Database(String player, int score)throws Exception
	{
		super();
		this.player = player;
		this.score = score;
		insert();
		highScore();
	}

	public void insert()throws Exception
	{
		try
		{
			System.out.println("Build Connection");
			String url="jdbc:mysql://localhost:3306/flappybird";        
	        	Class.forName("com.mysql.cj.jdbc.Driver");     // checked exception
	        	Connection con = DriverManager.getConnection(url,"root","");
	        	System.out.println("Connected");
			String query="SELECT Score From player where Name='"+player+"'";
			Statement st = con.createStatement();            
           		 ResultSet rs = st.executeQuery(query);
	       if(rs.next())
	        {
	        	System.out.println(rs.getInt("Score"));
	        	String query1="UPDATE player SET Score ="+score+" WHERE Name='"+player+"'";
	        	PreparedStatement pst= con.prepareStatement(query1);    
				pst.executeUpdate(); 
	        }
	        else
	        {
				PreparedStatement post= con.prepareStatement("INSERT INTO player VALUES ('"+player+"','"+score+"')");
				//System.out.println(player+" "+score);
				post.execute();
	        }
	        con.close();
	        rs.close();
	        st.close();
		}
		catch(Exception e)
		{
			System.out.println(" Insert ERROR");
			e.printStackTrace();
		}
	}
	public void highScore()throws Exception
	{
		try
		{
			System.out.println("Build Connection");
			String url="jdbc:mysql://localhost:3306/flappybird";        
	        	Class.forName("com.mysql.cj.jdbc.Driver");     // checked exception
	        	Connection con = DriverManager.getConnection(url,"root","");
	        	System.out.println("Connected");
	        
			String query="select*from player a where(select count(distinct (Score)) from player where Score>=a.Score)<=3 order by Score desc;";
			Statement st = con.createStatement();            
            ResultSet rs = st.executeQuery(query);
            ArrayList<String> nm= new ArrayList<>();
            ArrayList<Integer> sc=new ArrayList<>();
            int c=0;
            if(rs != null)
            {
            	while(rs.next())
            	{
	            	System.out.println("ENTER");
			nm.add(rs.getString("Name"));
			sc.add(rs.getInt("Score"));
            	}
            }
            JOptionPane.showMessageDialog(null, "You lose!\n"+"Your score is: "+score+".\n\n Top Scorers \n"+nm.get(0)+" : "+sc.get(0)+"\n"+nm.get(1)+" : "+sc.get(1)+"\n"+nm.get(2)+" : "+sc.get(2));
			
            con.close();
	        rs.close();
	        st.close();
		}
		catch(Exception e)
		{
			System.out.println(" High Score ERROR");
			e.printStackTrace();
		}
	}
}
