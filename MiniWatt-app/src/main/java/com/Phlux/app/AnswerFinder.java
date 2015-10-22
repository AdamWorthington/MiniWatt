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
	
	private ImmutablePair<String, Integer> whatQuestion(Question question, String source) 
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	private ImmutablePair<String, Integer> whyQuestion(Question question, String source)  
	{
		ImmutablePair<String, Integer> result = null;
		
		String subject = question.getSubject();
		
		int startIndex = source.indexOf("because");
		if(startIndex == -1)
			return null;
		String substr = source.substring(0, startIndex);
		
		return result;
	}
	
	private ImmutablePair<String, Integer> whereQuestion(Question question, String source)  
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	private ImmutablePair<String, Integer> whenQuestion(Question question, String source) 
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	private ImmutablePair<String, Integer> whoQuestion(Question question, String source)  
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	private ImmutablePair<String, Integer> howQuestion(Question question, String source)  
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	private ImmutablePair<String, Integer> whichQuestion(Question question, String source) 
	{
		ImmutablePair<String, Integer> result = null;
		
		return result;
	}
	
	public List<ImmutablePair<String, Integer>> findAnswer(Question question)
	{
		List<ImmutablePair<String, Integer>> answers = new ArrayList<ImmutablePair<String, Integer>>();
		
		switch(question.getType())
		{
			case WHAT:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whatQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHY:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whyQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHEN:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whenQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHERE:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whereQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHO:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whoQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case HOW:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = howQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case WHICH:
				for(String source : sources)
				{
					ImmutablePair<String, Integer> result = whichQuestion(question, source);
					if(result != null)
						answers.add(result);
				}
				break;
			case INVALID:
				break;
		}
		
		if(answers.size() == 0)
			answers.add(new ImmutablePair<String, Integer>("No Answer", 100));

		return answers;
	}
	
	public void dispose()
	{
		sources = null;
	}
	
	
}
