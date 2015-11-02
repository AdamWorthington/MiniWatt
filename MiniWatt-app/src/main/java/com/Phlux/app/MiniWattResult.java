package com.Phlux.app;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class MiniWattResult 
{
	private Question question;
	private List<ImmutablePair<String, Integer>> results;
	
	public Question getQuestion()
	{
		return question;
	}
	
	public List<ImmutablePair<String,Integer>> getResults()
	{
		return results;
	}
	
	public MiniWattResult(Question question, List<ImmutablePair<String, Integer>> results)
	{
		this.question = question;
		this.results = results;
	}
}
