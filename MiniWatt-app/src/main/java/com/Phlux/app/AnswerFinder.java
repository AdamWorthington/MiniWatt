package com.Phlux.app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class AnswerFinder 
{
	private List<String> sources;
	
	public AnswerFinder(List<String> sources)
	{
		this.sources = sources;
	}
	
	public AnswerFinder(String source)
	{
		this.sources = new ArrayList<String>();
		this.sources.add(source);
	}
	
	private List<ImmutablePair<String, Integer>> searchOnKeyword(String keyword, String source, Question question)
	{
		List<ImmutablePair<String, Integer>> result = new ArrayList<ImmutablePair<String, Integer>>();
		String src = source;
		
		Pattern re = Pattern.compile(
	            "# Match a sentence ending in punctuation or EOS.\n" +
	            "[^.!?\\s]    # First char is non-punct, non-ws\n" +
	            "[^.!?]*      # Greedily consume up to punctuation.\n" +
	            "(?:          # Group for unrolling the loop.\n" +
	            "  [.!?]      # (special) inner punctuation ok if\n" +
	            "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n" +
	            "  [^.!?]*    # Greedily consume up to punctuation.\n" +
	            ")*           # Zero or more (special normal*)\n" +
	            "[.!?]?       # Optional ending punctuation.\n" +
	            "['\"]?       # Optional closing quote.\n" +
	            "(?=\\s|$)", 
	            Pattern.MULTILINE | Pattern.COMMENTS);

		
		String subject = question.getSubject();
		String predicate = question.getPredicate();
		if(predicate.indexOf('?') != -1)
			predicate = predicate.substring(0, predicate.length()-2);
		String[] predToks = predicate.split(" ");
		
		ArrayList<String> sentences = new ArrayList<String>();
		Matcher sentenceMatcher = re.matcher(source);
		while(sentenceMatcher.find())
			sentences.add(sentenceMatcher.group());
		
		for(String sentence : sentences)
		{
			boolean predWordsPresent = true;
			boolean subjectPresent = true;
			//The certainty that we have a correct answer.
			int certainty = 0;
			
			//Try to find the subject in the sentence.
			boolean keywordPresent = sentence.contains(" " + keyword + " ");
			if(!keywordPresent)
				continue;
			
			//It contains the keyword
			certainty += 25;

			//Try to find the subject in the sentence.
			int subjectIndex = sentence.indexOf(subject);
			if(subjectIndex == -1)
				continue;
			
			//It contains the subject
			certainty += 25;
			
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
			
			if(hits == 0)
				predWordsPresent = false;
			
			if(predWordsPresent)
				result.add(new ImmutablePair<String, Integer>(sentence, certainty));
		}
		
		return result;
	}
	
	private List<ImmutablePair<String, Integer>> whatQuestion(Question question, String source)  
	{
		List<ImmutablePair<String, Integer>> compiled = new ArrayList<ImmutablePair<String, Integer>>();
		compiled.addAll(searchOnKeyword("is", source, question));
		compiled.addAll(searchOnKeyword("be", source, question));
		compiled.addAll(searchOnKeyword("are", source, question));
		
		return compiled;
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
