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
	/*
	 * Subject ending Location
	 */
	public static int subjectEnd;
	public static void parseQuestion(Question question, String q_text)
	{
		//TODO Invalid handling
		question_text = q_text;
		QuestionType type = getType(q_text, question);
		question.setType(type);
		findSubject(question);
		findKeyWords(question);
		
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
	
	public static void findSubject(Question q)
	{
		String text = question_text;
		int previous_index = 0;
		int current_index = text.indexOf(' ', previous_index);
		String word = text.substring(previous_index, current_index);
		String subject = "";
		boolean upper = false;
		int lastUpper = 0;
		while(current_index > 0)
		{
			//get the next word
			//skips the question word
			previous_index = current_index + 1;
			current_index = text.indexOf(' ', previous_index);
			if(current_index < 0)
			{
				word = text.substring(previous_index, text.length());
				word = word.replace('?', ' ');
			}
			else
			{
				word = text.substring(previous_index, current_index);
				word = word.replace('?', ' ');
			}
			lastUpper++;
			if(!foundIn(word, uselessWords))
			{
				if(Character.isUpperCase(word.charAt(0)))
				{
					upper = true;
					subject = subject.concat(word);
					subject = subject.concat(" ");
					q.setSubject(subject);
					lastUpper = 0;
				}
				else
				{
					if(upper && lastUpper < 3)
					{
						//only concat to subject if upper case is within the next two words
						//hold onto current values 
						int x = current_index;
						int y = previous_index;
						String temp = word;
						previous_index = current_index + 1;
						current_index = text.indexOf(' ', previous_index);
						if(current_index < 0)
						{
							current_index = x;
							previous_index = y;
							word = temp;
							q.setSubject(subject);
							subjectEnd = current_index;
							System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
							return;
						}
						else
						{
							System.out.println("here");
							word = text.substring(previous_index, current_index);
							word = word.replace('?', ' ');
						}
						
						if(!(Character.isUpperCase(word.charAt(0))))
						{
							previous_index = current_index + 1;
							current_index = text.indexOf(' ', previous_index);
							if(current_index < 0)
							{
								current_index = x;
								previous_index = y;
								word = temp;
								q.setSubject(subject);
								subjectEnd = previous_index;
								System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
								return;
							}
							else
							{
								word = text.substring(previous_index, current_index);
								word = word.replace('?', ' ');
							}
							
							if(Character.isUpperCase(word.charAt(0)))
							{
								//restore previous values
								current_index = x;
								previous_index = y;
								word = temp;
								subject = subject.concat(word);
								subject = subject.concat(" ");
								q.setSubject(subject);
							}
							else
							{
								//end case of proper subject
								current_index = x;
								previous_index = y;
								word = temp;
								q.setSubject(subject);
								subjectEnd = previous_index;
								System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
								return;
							}
						}
						else
						{
							//restore previous values
							current_index = x;
							previous_index = y;
							word = temp;
							subject = subject.concat(word);
							subject = subject.concat(" ");
							q.setSubject(subject);
						}
					}
					else if(upper && lastUpper >= 3)
					{
						//end case of proper subject
						q.setSubject(subject);
						subjectEnd = previous_index;
						System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
						return;
					}
					else if(!upper)
					{
						subject = subject.concat(word);
						subject = subject.concat(" ");
						q.setSubject(subject);
						
					}
				}
			}
			else
			{
				if(subject.length() > 1)
				{
					subjectEnd = previous_index;
					System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
					return;
				}
			}
		}
		q.setSubject(subject);
		subjectEnd = previous_index;
		System.out.println("Subject: " + subject + " Pos: " + subjectEnd);
	}
	
	public static void findKeyWords(Question q)
	{
		String text = question_text;
		int previous_index = subjectEnd;
		int current_index = text.indexOf(' ', previous_index);
		String word;
		String predicate = "";
		if(current_index < 0)
		{
			word = text.substring(previous_index, text.length());
			word = word.replace('?', ' ');
		}
		else
		{
			word = text.substring(previous_index, current_index);
			word = word.replace('?', ' ');
		}
		if(!foundIn(word, uselessWords))
		{
			predicate = predicate.concat(word);
			predicate = predicate.concat(" ");
		}
		while(current_index > 0)
		{
			//get the next word starting where the subject ended
			previous_index = current_index + 1;
			current_index = text.indexOf(' ', previous_index);
			if(current_index < 0)
			{
				word = text.substring(previous_index, text.length());
				word = word.replace('?', ' ');
			}
			else
			{
				word = text.substring(previous_index, current_index);
				word = word.replace('?', ' ');
			}
			if(!foundIn(word, uselessWords))
			{
				predicate = predicate.concat(word);
				predicate = predicate.concat(" ");
			}
		}
		q.setPredicate(predicate);
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
	
}