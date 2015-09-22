package com.Phlux.app;

import java.util.*;

/*
 * The class that breaks down a question into a Question object
 */
public class QuestionParser
{
	/*
	 * question text
	 */
	public static String question_text;
	/*
	 * words to skip
	 */
	public String[] uselessWords = {"are", "the", "is", "do", "does", "was"};
	/*
	 * prepositions
	 */
	public String[] prepositions = {"aboard","about","above","across","after","after","against","along", "amid", "among","anti","around","as","at","before",
			"behind","below","beneath","beside","besides","between","beyond","but","by","concerning","considering","despite","down","during","except","excepting",
			"excluding", "following","for","from","in","inside","into", "like","minus","near","of","off","on","onto","opposite","outside","over","past","per","plus",
			"regearding", "round", "save", "since", "than", "through", "to", "toward", "under", "underneath", "unlike", "until", "up","upon","versus","via","with",
			"within","without"};
	
	public static void parseQuestion(Question question, String question_text)
	{
		int type = getType(question_text);
		switch(type) {
		case 1: question.setType(1);
				//call method of what questions
				break;
		case 2: question.setType(2);
				//call method of what questions
				break;
		case 3: question.setType(3);
				//call method of what questions
				break;
		case 4: question.setType(4);
				//call method of what questions
				break;
		case 5: question.setType(5);
				//call method of what questions
				break;
		case 6: question.setType(6);
				//call method of what questions
				break;
		case 7: question.setType(7);
				//call method of what questions
				break;
		default: question.setType(-1);
				//error 
				break;
		}
		
	}
	
	public static int getType(String question)
	{
		/*
		 * These may be needed later
		CharSequence what = "what";
		CharSequence What = "What";
		CharSequence when = "when";
		CharSequence When = "When";
		CharSequence where = "where";
		CharSequence Where = "Where";
		CharSequence who = "who";
		CharSequence Who = "Who";
		CharSequence how = "how";
		CharSequence How = "How";
		CharSequence why = "why";
		CharSequence Why = "Why";
		CharSequence which = "which";
		CharSequence Which = "Which";*/
		
		if(question.contains("What") || question.contains("what"))
		{
			return 1;
		}
		else if(question.contains("When") || question.contains("when"))
		{
			return 2;
		}
		else if(question.contains("Where") || question.contains("where"))
		{
			return 3;
		}
		else if(question.contains("Who") || question.contains("who"))
		{
			return 4;
		}
		else if(question.contains("How") || question.contains("how"))
		{
			return 5;
		}
		else if(question.contains("Why") || question.contains("why"))
		{
			return 6;
		}
		else if(question.contains("Which") || question.contains("which"))
		{
			return 7;
		}
		else
		{
			//no question word found
			return -1;
		}
	}
	
	/*
	 * makes all questions start with the question words
	 */
	public static void moveFrontLoad(Question question, String q_word)
	{
		int index = question.getQuestionText().indexOf(q_word);
		if(index == 0)
		{
			return;
		}
		else
		{
			String front = question_text.substring(0, index);
			String back = question_text.substring(index, question_text.length());
			String new_question = back.concat(front);
			question_text = new_question;
			return;
		}
	}
	/*
	 * find string a in array b
	 */
	public boolean findIn(String a, String[] b)
	{
		for(int i = 0; i < b.length; i++)
		{
			if(a.compareTo(b[i]) == 0)
			{
				return true;
			}
		}
		return false;
	}
	/*
	 * This method is for What questions
	 */
	public static void whatQuestion(Question q)
	{
		
	}
}