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
		String predicate = question.getPredicate();
		String[] predToks = predicate.split(" ");
		
		int startIndex = src.indexOf(keyword);
		while(startIndex != -1)
		{
			//The certainty that we have a correct answer.
			int certainty = 0;
			
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
				certainty += 25;
			else
				certainty += 50;
			
			int hits = 0;
			int total = predToks.length;
			int[] distances = new int[total];
			for(int j = 0; j < total; j++)
			{
				int i = sentence.indexOf(predToks[j]);
				if(i != -1)
					hits++;
				
				distances[j] = i;
			}
			
			float percentageHit = ((float)hits / (float)total);
			int frac = (int) (percentageHit*25);
			certainty += frac;
			
			result.add(new ImmutablePair<String, Integer>(sentence, certainty));
			
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
	
	private List<ImmutablePair<String, Integer>> whereQuestion(Question question, String source)  
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
	
	private List<ImmutablePair<String, Integer>> whichQuestion(Question question, String source) 
	{
		return null;
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
