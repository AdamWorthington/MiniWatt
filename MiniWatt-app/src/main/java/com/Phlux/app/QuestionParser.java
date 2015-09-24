package com.Phlux.app;

import java.util.*;

import com.Phlux.app.Question.QuestionType;

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
	public static String[] uselessWords = {"are", "the", "is", "do", "does", "was", "did"};
	/*
	 * prepositions
	 */
	public static String[] prepositions = {"aboard","about","above","across","after","after","against","along", "amid", "among","anti","around","as","at","before",
			"behind","below","beneath","beside","besides","between","beyond","but","by","concerning","considering","despite","down","during","except","excepting",
			"excluding", "following","for","from","in","inside","into", "like","minus","near","of","off","on","onto","opposite","outside","over","past","per","plus",
			"regearding", "round", "save", "since", "than", "through", "to", "toward", "under", "underneath", "unlike", "until", "up","upon","versus","via","with",
			"within","without"};
	
	public static void parseQuestion(Question question, String q_text)
	{
		question_text = q_text;
		QuestionType type = getType(q_text, question);
		question.setType(type);
		if(question.getType() == QuestionType.WHAT)
		{
			whatQuestion(question);
		}
		else
		{
			whenQuestion(question);
		}
	}
	
	public static QuestionType getType(String question, Question q)
	{	
		if(question.contains("What"))
		{
			moveFrontLoad(q, "What");
			return QuestionType.WHAT;
		}
		else if(question.contains("what"))
		{
			moveFrontLoad(q, "what");
			return QuestionType.WHAT;
		}
		else if(question.contains("When"))
		{
			moveFrontLoad(q, "When");
			return QuestionType.WHEN;
		}
		else if(question.contains("when"))
		{
			moveFrontLoad(q, "when");
			return QuestionType.WHEN;
		}
		else if(question.contains("Where"))
		{
			moveFrontLoad(q, "Where");
			return QuestionType.WHERE;
		}
		else if(question.contains("where"))
		{
			moveFrontLoad(q, "where");
			return QuestionType.WHERE;
		}
		else if(question.contains("Who"))
		{
			moveFrontLoad(q, "Who");
			return QuestionType.WHO;
		}
		else if(question.contains("who"))
		{
			moveFrontLoad(q, "who");
			return QuestionType.WHO;
		}
		else if(question.contains("How"))
		{
			moveFrontLoad(q, "How");
			return QuestionType.HOW;
		}
		else if(question.contains("how"))
		{
			return QuestionType.HOW;
		}
		else if(question.contains("Why"))
		{
			moveFrontLoad(q, "Why");
			return QuestionType.WHY;
		}
		else if(question.contains("why"))
		{
			moveFrontLoad(q, "why");
			return QuestionType.WHY;
		}
		else if(question.contains("Which"))
		{
			moveFrontLoad(q, "Which");
			return QuestionType.WHICH;
		}
		else if(question.contains("which"))
		{
			moveFrontLoad(q, "which");
			return QuestionType.WHICH;
		}
		else
		{
			//no question word found
			return QuestionType.INVALID;
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
			back = back.replace('?', ' ');
			String new_question = back.concat(front);
			new_question = new_question.substring(0,new_question.length());
			System.out.println(">"+new_question+"<");
			question_text = new_question;
			return;
		}
	}
	/*
	 * find string a in array b
	 */
	public static boolean foundIn(String a, String[] b)
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
		//question text
		String text = question_text;
		int previous_index = 0;
		int current_index = text.indexOf(' ', previous_index);
		String word = text.substring(previous_index, current_index);
		String subject = "";
		String predicate = "";
		//has a subject been found, if so what position, default -1
		int sub = -1;
		//tracks word count
		int wordCount = 1;
		//currently in a prep phrase
		boolean prep_phrase = false;
		//is the current word a prep
		boolean isPrep = false;
		//if true, the preposition is for a subject
		boolean subOrPred = true;
		//linked list of prep phrases as strings
		LinkedList<String> subPrepList = new LinkedList<String>();
		//linked list of prep phrases for predicate
		LinkedList<String> predPrepList = new LinkedList<String>();
		//string of current prep phrase
		String prep = new String();
		
		while(current_index > 0)
		{
			wordCount++;
			previous_index = current_index + 1;
			current_index = text.indexOf(' ', previous_index);
			if(current_index < 0)
			{
				word = text.substring(previous_index, text.length());
			}
			else
			{
				word = text.substring(previous_index, current_index);
			}
			System.out.println("Word = " + word);
			isPrep = foundIn(word, prepositions);
			if(!foundIn(word, uselessWords))
			{
				word = word.concat(" ");
				if(sub < 0)
				{
					subject = subject.concat(word);
					sub = wordCount;
				}
				else if(sub + 1 == wordCount && !isPrep)
				{
					subject = subject.concat(word);
					sub = wordCount;
				}
				else if(isPrep)
				{
					if(prep_phrase)
					{
						if(subOrPred)
						{
							subPrepList.add(prep);
						}
						else
						{
							predPrepList.add(prep);
						}
						prep = "";
					}
					prep = prep.concat(word);
					prep_phrase = true;
				}
				else if(prep_phrase)
				{
					prep = prep.concat(word);
				}
				else
				{
					predicate = predicate.concat(word);
				}
			}
			else
			{
				if(prep_phrase)
				{
					if(subOrPred)
					{
						subPrepList.add(prep);
					}
					else
					{
						predPrepList.add(prep);
					}
					prep = "";
					prep_phrase = false;
				}
				if(sub > 0)
				{
					subOrPred = false;
				}
			}
		}
		//check for ending preposition
		if(prep_phrase)
		{
			if(subOrPred)
			{
				subPrepList.add(prep);
			}
			else
			{
				predPrepList.add(prep);
			}
		}
		//set Question object
		q.setSubject(subject);
		q.setSubjectPrepositions(subPrepList);
		q.setPredicate(predicate);
		q.setPredicatePrepositions(predPrepList);
	}
	/*
	 * method for when questions
	 */
	public static void whenQuestion(Question q)
	{
		//question text
		String text = question_text;
		int previous_index = 0;
		int current_index = text.indexOf(' ', previous_index);
		String word = text.substring(previous_index, current_index);
		String subject = "";
		String predicate = "";
		//has a subject been found, if so what position, default -1
		int sub = -1;
		//tracks word count
		int wordCount = 1;
		//currently in a prep phrase
		boolean prep_phrase = false;
		//is the current word a prep
		boolean isPrep = false;
		//if true, the preposition is for a subject
		boolean subOrPred = true;
		//linked list of prep phrases as strings
		LinkedList<String> subPrepList = new LinkedList<String>();
		//linked list of prep phrases for predicate
		LinkedList<String> predPrepList = new LinkedList<String>();
		//string of current prep phrase
		String prep = new String();
		boolean prepbool = false;
		while(current_index > 0)
		{
			wordCount++;
			previous_index = current_index + 1;
			current_index = text.indexOf(' ', previous_index);
			if(current_index < 0)
			{
				word = text.substring(previous_index, text.length());
			}
			else
			{
				word = text.substring(previous_index, current_index);
			}
			System.out.println("Word = " + word);
			isPrep = foundIn(word, prepositions);
			if(!foundIn(word, uselessWords))
			{
				word = word.concat(" ");
				if(sub < 0)
				{
					subject = subject.concat(word);
					sub = wordCount;
				}
				else if(isPrep)
				{
					prepbool = true;
					if(prep_phrase)
					{
						if(subOrPred)
						{
							subPrepList.add(prep);
						}
						else
						{
							predPrepList.add(prep);
						}
						prep = "";
					}
					prep = prep.concat(word);
					prep_phrase = true;
				}
				else if(prep_phrase && prepbool)
				{
					prepbool = false;
					prep = prep.concat(word);
					if(subOrPred)
					{
						subPrepList.add(prep);
					}
					else
					{
						predPrepList.add(prep);
					}
					prep = "";
					prep_phrase = false;
				}
				else
				{
					predicate = predicate.concat(word);
				}
			}
			else
			{
				if(prep_phrase)
				{
					if(subOrPred)
					{
						subPrepList.add(prep);
					}
					else
					{
						predPrepList.add(prep);
					}
					prep = "";
					prep_phrase = false;
				}
				if(sub > 0)
				{
					subOrPred = false;
				}
			}
		}
		//check for ending preposition
		if(prep_phrase)
		{
			if(subOrPred)
			{
				subPrepList.add(prep);
			}
			else
			{
				predPrepList.add(prep);
			}
		}
		//set Question object
		q.setSubject(subject);
		q.setSubjectPrepositions(subPrepList);
		q.setPredicate(predicate);
		q.setPredicatePrepositions(predPrepList);
	}
	//printer method
	public static void printer(Question q)
	{
		System.out.println("========================================================================");
		System.out.println("Queestion type =		" + q.getType());
		System.out.println("Queestion subject =		" + q.getSubject());
		System.out.println("Queestion preps for subject =	" + q.getSubjectPrepositions());
		System.out.println("Queestion predicate =	" + q.getPredicate());
		System.out.println("Queestion preps for predicate =	" + q.getPredicatePrepositions());
		System.out.println("========================================================================");
	}
	
	//test main
	
	public static void main(String[] args)
	{
		Question q = new Question("When did King Henry the third die?");
		printer(q);
	}
}