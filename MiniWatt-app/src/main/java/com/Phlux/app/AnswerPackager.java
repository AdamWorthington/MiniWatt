package com.Phlux.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class AnswerPackager 
{
	private List<Question> questions;
	private List<List<String>> sources;
	private List<MiniWattResult> results;
	
	private boolean isSingleSource = false;
	
	public AnswerPackager(List<Question> questions, List<List<String>> sources, boolean isSingleSource)
	{
		this.questions = questions;
		this.isSingleSource = isSingleSource;
	}
	
	public List<MiniWattResult> getAnswers()
	{
		results = new ArrayList<MiniWattResult>();
		
		if(isSingleSource)
		{
			AnswerFinder finder = new AnswerFinder(sources.get(0));
			for(Question q : questions)
			{
				List<ImmutablePair<String, Integer>> res = finder.findAnswer(q);
				Collections.sort(res, new ResultComparator());
				//We only want to keep the top 3 results.
				while(res.size() > 3)
					res.remove(res.size() - 1);
				MiniWattResult r = new MiniWattResult(q, res);
			}
		}
		
		for(int i = 0; i < questions.size(); i++)
		{
			AnswerFinder finder = new AnswerFinder(sources.get(i));
			List<ImmutablePair<String, Integer>> res = finder.findAnswer(questions.get(i));
			Collections.sort(res, new ResultComparator());
			//We only want to keep the top 3 results.
			while(res.size() > 3)
				res.remove(res.size() - 1);
			MiniWattResult r = new MiniWattResult(questions.get(i), res);
		}
		
		return results;
	}
}

class ResultComparator implements Comparator<ImmutablePair<String, Integer>>
{
	public int compare(ImmutablePair<String, Integer> a, ImmutablePair<String, Integer> b)
	{
		return a.right.compareTo(b.right);
	}
}
