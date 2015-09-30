package com.Phlux.app;

import java.sql.*;
import java.util.LinkedList;

//TABLE questions (QTYPE VARCHAR(6), QTEXT VARCHAR(140), QSTEXT VARCHAR(30), QSPREP VARCHAR(30), QPTEXT VARCHAR(30), QPPREP VARCHAR(30));
public class RemoteStorage 
{
	static Connection supbro = null;
	
	public static Connection connect() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		try {
		Connection retcon = DriverManager.getConnection("jdbc:mysql://66.253.159.66/feedback?" + "user=lysander&password=greekpasscord");
		return retcon;
		}
		catch (SQLException e)
		{
		
			return null;
		}
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
	
	public static void store(Question q) throws ClassNotFoundException, SQLException
	{
		supbro = connect();
		if(!supbro.isValid(5))
		{
			return;
		}
		Statement s = supbro.createStatement();
		
		//select proper database
		s.execute("use timhardy");
		
		//build the statement to execute
		//example statement : "INSERT INTO questions (QTYPE, QTEXT, QSTEXT, QSPREP, QPTEXT, QPPREP) VALUES('who', 'who was dickphillips', 'dickphillips', 'animalbones', 'ppoos');
		String exestate = "INSERT INTO questions (QTYPE, QTEXT, QSTEXT, QSPREP, QPTEXT, QPPREP) VALUES(";
		
		//break down the question object
		exestate += "\'" + q.getType().toString() + "\', "; //type to QTYPE
		exestate += "\'" + q.getQuestionText() + "\', "; //question_text to QTEXT
		exestate += "\'" + q.getSubject() + "\', "; //subject_text to QSTEXT
		exestate += "\'" + makeAString(q.getSubjectPrepositions()) + "\', "; //subject_prepositions to QSPREP
		exestate += "\'" + q.getPredicate() + "\', "; //predicate_text to QPTEXT
		exestate += "\'" + makeAString(q.getPredicatePrepositions()) + "\', "; //predicate_prepositions to QPREP
		exestate += ");";
		
		s.execute(exestate);
		supbro.close();
	}
}