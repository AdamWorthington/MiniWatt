package com.Phlux.app;

import java.sql.*;
import java.util.LinkedList;
import java.net.*;

//TABLE questions (QTYPE VARCHAR(6), QTEXT VARCHAR(140), QSTEXT VARCHAR(30), QSPREP VARCHAR(30), QPTEXT VARCHAR(30), QPPREP VARCHAR(30));
public class RemoteStorage 
{
	static Connection supbro = null;
	
	public static Connection connect() throws ClassNotFoundException, SQLException
	{
		//this code connects from your app engine bullshit
		 Class.forName("com.mysql.jdbc.GoogleDriver");
		try {
		Connection retcon = DriverManager.getConnection("jdbc:google:mysql://civic-vigil-109921:timhardy?user=root");
		return retcon;
		}
		catch (SQLException e)
		{
			System.out.println("Connectin Failed.");
			throw e;
		}
		
		/*
		 * String url = null;
			if (SystemProperty.environment.value() ==
    			SystemProperty.Environment.Value.Production) {
  				// Connecting from App Engine.
  				// Load the class that provides the "jdbc:google:mysql://"
  				// prefix.
  				Class.forName("com.mysql.jdbc.GoogleDriver");
  				url =
    			"jdbc:google:mysql://civic-vigil-109921:tomhardy?user=root";
			} else {
 				// Connecting from an external network.
  				Class.forName("com.mysql.jdbc.Driver");
  				url = "jdbc:mysql://173.194.80.81:3306?user=root";
			}
			Connection conn = DriverManager.getConnection(url);
			ResultSet rs = conn.createStatement().executeQuery(
    		"SELECT 1 + 1");
		 */
	}
	
	public static String makeAString(LinkedList<String> l)
	{
		String retval = "";
		for(int i = 0; i < l.size(); i++)
		{
			retval += l.get(i) + " ";
		}
		return retval;
	}
	
	public static void store(Question q, String answer) throws ClassNotFoundException, SQLException
	{
		try {
		supbro = connect();
		Statement s = supbro.createStatement();
		s.execute("use jimhardy;");
		}
		catch (SQLException e)
		{
			System.out.println("Connection failed.");
		}
		
		//build the statement to execute
		//example statement : "INSERT INTO questions (question, answer) VALUES('who', 'who was dickphillips');
		String exestate = "INSERT INTO qna VALUES(?, ?)";
		PreparedStatement st;
		try {
			st = supbro.prepareStatement(exestate);
		}
		catch (SQLException e) //why?
		{
			System.out.println("Statement failed.");
			supbro.close();
			return;
		}
		
		//break down the question object
		try {
		st.setString(1, q.getQuestionText()); //question_text to QTEXT
		st.setString(2, answer); //this doesn't yet exist
		}
		catch(Exception e)
		{
			System.out.println("Failed to add to statement.");
		}
		
		try{
			st.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Failed to execute statement.");
		}
		supbro.close();
	}
}