package com.Phlux.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.pdfbox.pdmodel.PDDocument;

public class AnswerPackager 
{
	private List<Question> questions;
	private List<List<String>> sources;
	private List<ImmutableTriple<Question, String, Integer>> results;
	
	private boolean isSingleSource = false;
	
	public AnswerPackager(List<Question> questions, List<List<String>> sources, boolean isSingleSource)
	{
		this.questions = questions;
		this.isSingleSource = isSingleSource;
	}
	
	public List<ImmutableTriple<Question, String, Integer>> getAnswers()
	{
		results = new ArrayList<ImmutableTriple<Question, String, Integer>>();
		
		if(isSingleSource)
		{
			AnswerFinder finder = new AnswerFinder(sources.get(0));
			for(Question q : questions)
			{
				List<ImmutablePair<String, Integer>> res = finder.findAnswer(q);
				for(ImmutablePair<String, Integer> pair : res)
					results.add(new ImmutableTriple<Question, String, Integer>(q, pair.getLeft(), pair.getRight()));
			}
		}
		
		for(int i = 0; i < questions.size(); i++)
		{
			AnswerFinder finder = new AnswerFinder(sources.get(i));
			List<ImmutablePair<String, Integer>> res = finder.findAnswer(questions.get(i));
			for(ImmutablePair<String, Integer> pair : res)
				results.add(new ImmutableTriple<Question, String, Integer>(questions.get(i), pair.getLeft(), pair.getRight()));
		}
		
		return results;
	}
}
