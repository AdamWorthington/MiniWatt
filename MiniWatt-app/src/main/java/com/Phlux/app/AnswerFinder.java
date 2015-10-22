package com.Phlux.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class AnswerFinder 
{
	private List<String> sources;
	
	public AnswerFinder(List<String> sources)
	{
		this.sources = sources;
	}
	
	private List<ImmutablePair<String, Integer>> searchOnKeyword(String keyword, String source, Question question)
	{
		List<ImmutablePair<String, Integer>> result = new ArrayList<ImmutablePair<String, Integer>>();
		String src = source;
		
		String subject = question.getSubject();
		
		int startIndex = src.indexOf(keyword);
		while(startIndex != -1)
		{
			//Get the start of the sentence.
			int startOfSentence = src.substring(0, startIndex).indexOf(".");
			if(startOfSentence == -1)
				startOfSentence = 0;
			
			//Get the end of the sentence.
			int endOfSentence = src.indexOf(".", startIndex);
			if(endOfSentence == -1)
				endOfSentence = src.length() - 1;
			
			String sentence = src.substring(startOfSentence, endOfSentence);
			//Try to find the subject in the sentence.
			int subjectIndex = sentence.indexOf(subject);
			if(subjectIndex == -1)
				result.add(new ImmutablePair<String, Integer>(sentence, 25));
			else
				result.add(new ImmutablePair<String, Integer>(sentence, 75));
			
			src.substring(endOfSentence);
			startIndex = src.indexOf(keyword);
		}
		
		return result;
	}
	
	private List<ImmutablePair<String, Integer>> whatQuestion(Question question, String source)  
	{
		return searchOnKeyword("is", source, question);
	}
	
	private List<ImmutablePair<String, Integer>> whyQuestion(Question question, String source)  
	{
		return searchOnKeyword("because", source, question);
	}
	
	private List<ImmutablePair<String, Integer>> whereQuestions(Question question, String source)  
	{
		return searchOnKeyword("in", source, question);
	}
	
	private List<ImmutablePair<String, Integer>> whenQuestion(Question question, String source) 
	{
		return searchOnKeyword("in", source, question);
	}
	
	private List<ImmutablePair<String, Integer>> whoQuestion(Question question, String source)  
	{
		return searchOnKeyword("is", source, question);
	}
	
	private List<ImmutablePair<String, Integer>> howQuestion(Question question, String source)  
	{
		return searchOnKeyword("by", source, question);
	}
	
	private ImmutablePair<String, Integer> whichQuestion(Question question, String source) 
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	public List<List<ImmutablePair<String, Integer>>> findAnswer(Question question)
	{
		List<List<ImmutablePair<String, Integer>>> answers = new ArrayList<List<ImmutablePair<String, Integer>>>();
		
		switch(question.getType())
		{
			case WHAT:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = whatQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHY:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>>result = whyQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHEN:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = whenQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHERE:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = whereQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHO:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = whoQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case HOW:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = howQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHICH:
				for(String source : sources)
				{
					List<ImmutablePair<String, Integer>> result = whichQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case INVALID:
				break;
		}
		
		if(answers.size() == 0)
		{
			List<ImmutablePair<String, Integer>> result = new ArrayList<ImmutablePair<String, Integer>>();
			result.add(new ImmutablePair<String, Integer>("No Answer", 100));
			answers.add(result);
		}

		return answers;
	}
	
	public void dispose()
	{
		sources = null;
	}
	
	
}
