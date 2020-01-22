	package gameClient;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
import java.util.HashMap;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	

	/**
	 * a query that return the number of games you played 
	 */
	
	public static int numOfGame()
	{
		try {
			int num=0;
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT COUNT(*) AS num FROM Logs WHERE userID = 209373208 ";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			
			while(resultSet.next())
			{
				num = resultSet.getInt("num");
				//System.out.println("Level: "+resultSet.getInt("levelID")+","+"Score: "+resultSet.getInt("max_score"));
			}
			resultSet.close();
			statement.close();		
			connection.close();	
			return num;
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	
	}
	/** return a string that represents the best score each level
	 * 
	 */
		public static String getBestScore() {
			try {
				String s= "";
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT Logs.levelID ,Logs.userID , MAX(Logs.score) AS max_score FROM Logs WHERE Logs.userID =209373208 GROUP BY Logs.levelID ";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				
				while(resultSet.next())
				{
					s+= ("Level: "+resultSet.getInt("levelID")+ "Score: "+resultSet.getInt("max_score")+"\n");
					//System.out.println("Level: "+resultSet.getInt("levelID")+","+"Score: "+resultSet.getInt("max_score"));
				}
				resultSet.close();
				statement.close();		
				connection.close();	
				return s;
			}
			
			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
			public static String getKML(int id, int level) {
				String ans = null;
				String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
					Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(allCustomersQuery);
					if(resultSet!=null && resultSet.next()) {
						ans = resultSet.getString("kml_"+level);
					}
				}
				catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle.getMessage());
					System.out.println("Vendor Error: " + sqle.getErrorCode());
				}
				
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return ans;
			}
			
			/**
			 *  print all users in data base.
			 * @return
			 */
			
		public static int allUsers() {
			int ans = 0;
			String allCustomersQuery = "SELECT * FROM Users;";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				while(resultSet.next()) {
					System.out.println("Id: " + resultSet.getInt("UserID"));
					ans++;
				}
				resultSet.close();
				statement.close();		
				connection.close();
			}
			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return ans;
		}
		
		/**
		 *  return the number of the games in a certain stage
		 * @param stage
		 * @return
		 */
		
		public static int numOfGameInStage(int stage)
		{
			try {
				int num=0;
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT COUNT(*) AS num FROM Logs WHERE userID = 209373208 AND levelID = "+stage;
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				
				while(resultSet.next())
				{
					num = resultSet.getInt("num");
					//System.out.println("Level: "+resultSet.getInt("levelID")+","+"Score: "+resultSet.getInt("max_score"));
				}
				resultSet.close();
				statement.close();		
				connection.close();	
				return num;
			}
			
			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return 0;
		
		}
		
		/**
		 * return your rank in a certain stage, do no include distinct, for example
		 *Dani 209 (score)
		 *Ofek 209 (score)
		 *
		 * If your score is 200 your rank is 3.
		 *
		 * @param stage
		 * @param score
		 * @return
		 */
		
		public static int rankByStage(int stage,int score)
		{
			try {
				int num=0;
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT COUNT(*) AS num "+
						"FROM "+
						"(SELECT DISTINCT userID, levelID, score FROM "+
						 "(SELECT userID, levelID, score "+
						      "FROM Logs "+
						     " WHERE levelID = "+stage+ " AND score > "+score +" AND userID != 209373208 )"+
						      " AS T) AS T2";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				System.out.println();
				while(resultSet.next())
				{
					/*System.out.println("level "+resultSet.getInt("levelID"));
					System.out.print(" ,userID "+resultSet.getInt("userID"));
					System.out.print(" ,score "+resultSet.getInt("score"));*/
					num = resultSet.getInt("num");
					//System.out.println("Level: "+resultSet.getInt("levelID")+","+"Score: "+resultSet.getInt("max_score"));
				}
				resultSet.close();
				statement.close();		
				connection.close();	
				return num;
			}
			
			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return 0;
		
		}
		
		/**
		 * return your rank in a certain stage, include distinct, for example
		 *Dani 209 (score)
		 *Ofek 209 (score) 
		 *
		 * If your score is 200 your rank is 2.
		 *
		 * @param stage 
		 * @param score - your score
		 * @return allow_move - the max moves allow 
		 */
		
		
		public static int rankByStage_2(int stage,int score,int allow_moves)
		{
			try {
				int num=0;
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT COUNT(DISTINCT score) AS num "+
						"FROM "+
						"(SELECT DISTINCT userID, levelID, score FROM "+
						 "(SELECT userID, levelID, score "+
						      "FROM Logs "+
						     " WHERE levelID = "+stage+ " AND score > "+score +" AND userID != 209373208 AND moves <= "+allow_moves+" )"+
						      " AS T) AS T2";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				System.out.println();
				while(resultSet.next())
				{
					/*System.out.println("level "+resultSet.getInt("levelID"));
					System.out.print(" ,userID "+resultSet.getInt("userID"));
					System.out.print(" ,score "+resultSet.getInt("score"));*/
					num = resultSet.getInt("num");
					//System.out.println("Level: "+resultSet.getInt("levelID")+","+"Score: "+resultSet.getInt("max_score"));
				}
				resultSet.close();
				statement.close();		
				connection.close();	
				return num;
			}
			
			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return 0;
		
		}
		
	}
		
