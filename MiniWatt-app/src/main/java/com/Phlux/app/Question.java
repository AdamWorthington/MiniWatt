package com.Phlux.app;

import java.util.*;


public class Question
{
	/*
	 * text of the question
	 */
	private String question_text;
	/*
	 * The kind of question enumerated
	 * 1 = what
	 * 2 = when
	 * 3 = where
	 * 4 = who
	 * 5 = how.
	 * 6 = why
	 * 7 = which
	 */
	private int type;
	/*
	 * The subject of the question
	 */
	String subject_text;
	/*
	 * The prepositions linked to the subject
	 */
	LinkedList<String> subject_prepositions;
	/*
	 * The predicate of the question
	 */
	String predicate_text;
	/*
	 * The prepositions linked to the predicate
	 */
	LinkedList<String> predicate_prepositions;
	/*
	 * Object that holds all of the information in a question
	 */
	
	public Question(String question)
	{
		this.question_text = question;
	}

	/*
	 * Getter Methods
	 * ========================================================================
	 */
	public int getType()
	{
		return type;
	}

	public String getQuestionText()
	{
		return question_text;
	}
	
	public String getSubject()
	{
		return subject_text;
	}
	
	public String getPredicate()
	{
		return predicate_text;
	}
	
	public LinkedList<String> getSubjectPrepositions()
	{
		return subject_prepositions;
	}
	
	public LinkedList<String> getPredicatePrepositions()
	{
		return predicate_prepositions;
	}
	/*
	 * Setter Methods
	 * ========================================================================
	 */
	public void setType(int t)
	{
		type = t;
	}
	
	public void setSubject(String subject)
	{
		subject_text = subject;
	}
	
	public void setPredicate(String predicate)
	{
		predicate_text = predicate;
	}
	
	public void setSubjectPrepositions(LinkedList<String> preps)
	{
		subject_prepositions = preps;
	}
	
	public void setPredicatePrepositions(LinkedList<String> preps)
	{
		predicate_prepositions = preps;
	}
}