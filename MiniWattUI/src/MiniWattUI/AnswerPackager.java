package MiniWattUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class AnswerPackager 
{
	private ArrayList<Question> questions;
	private ArrayList<ArrayList<String>> sources;
	private ArrayList<MiniWattResult> results;
		
	public AnswerPackager(ArrayList<Question> questions, ArrayList<ArrayList<String>> sources)
	{
		this.questions = questions;
		this.sources = sources;
	}
	
	public ArrayList<MiniWattResult> getAnswers()
	{
		results = new ArrayList<MiniWattResult>();

		for(int i = 0; i < questions.size(); i++)
		{
			AnswerFinder finder = new AnswerFinder(sources.get(i));
			List<ImmutablePair<String, Integer>> res = finder.findAnswer(questions.get(i));
			MiniWattResult r = new MiniWattResult(questions.get(i), res);
			results.add(r);
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
