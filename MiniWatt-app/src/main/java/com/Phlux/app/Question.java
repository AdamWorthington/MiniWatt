package com.Phlux.app;

import java.util.*;



public class Question
{
	public enum QuestionType
	{
		WHAT,
		WHEN,
		WHERE,
		WHO,
		HOW,
		WHY,
		WHICH,
		INVALID,
	};
	
	/*
	 * text of the question
	 */
	private String question_text;
	/*
	 * The kind of question enumerated
	 */
	private QuestionType type;
	/*
	 * The subject of the question
	 */
	String subject_text;
	/*
	 * The prepositions linked to the subject
	 */
	String[] subject_prepositions;
	/*
	 * The predicate of the question
	 */
	String predicate_text;
	/*
	 * The prepositions linked to the predicate
	 */
	String[] predicate_prepositions;
	/*
	 * Object that holds all of the information in a question
	 */
	
	public Question(String question)
	{
		String[] x = new String[2];
		subject_prepositions = x;
		predicate_prepositions = x;
		this.question_text = question;
		QuestionParser.parseQuestion(this, this.question_text);
	}

	/*
	 * Getter Methods
	 * ========================================================================
	 */
	public QuestionType getType()
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
	
	public String[] getSubjectPrepositions()
	{
		return subject_prepositions;
	}
	
	public String[] getPredicatePrepositions()
	{
		return predicate_prepositions;
	}
	/*
	 * Setter Methods
	 * ========================================================================
	 */
	public void setType(QuestionType t)
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
	
	public void setSubjectPrepositions(String preps)
	{
		subject_prepositions[0] = preps;
	}
	
	public void setPredicatePrepositions(String preps)
	{
		predicate_prepositions[0] = preps;
	}
}