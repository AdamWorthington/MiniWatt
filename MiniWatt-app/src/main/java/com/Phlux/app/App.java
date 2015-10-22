package com.Phlux.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.pdfbox.exceptions.COSVisitorException;

import net.sourceforge.tess4j.TesseractException;

/**
 * Hello world! Test
 *
 */
public class App 
{
	public static void main( String[] args )
    {
    	/*
    	 * This is for when the document creator java file comes online. Ignore for now....
    	   String[] qs = {
				"What is your age?", "What is your name?",
				"Where do you live?", "What car do you drive?",
				"Are you married?", "How do you find the sqrt of the number 24?"
		};
		
		String[] ans = {
				"18", "Craig", "West Lafayette", "Honda Accord 2003 EX-L",
				"No", "You divide it and then you take a really long answer to see how it handles a really really long answer. See if it prints it to a new line."
		};
		
		try {
			DocumentCreator.createAnswerDoc(qs, ans);
		} catch (COSVisitorException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		File fil1 = new File("C:/Users/cdwil/Desktop/test.pdf"); //Pointer to a pdf file with searchable text
		File fil2 = new File("C:/Users/cdwil/Desktop/testImage.jpg"); //Pointer to an image that contains text
		try 
		{
			String[] parsed = TextInterpret.parseDocument(fil1);
			Queue<String> questions = TextInterpret.extractQuestions(parsed);
			
			for(int i = 0; i < questions.size(); i++)
			{
				System.out.println(questions.remove());
			}
			
			parsed = TextInterpret.parseImage(fil2);
			questions = TextInterpret.extractQuestions(parsed);
			
			for(int i = 0; i < questions.size(); i++)
			{
				System.out.println(questions.remove());
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TesseractException e)
		{
			e.printStackTrace();
		}*/
		
		Question q1 = new Question("What is the age of the Earth?");
		Question q2 = new Question("What is the size of a basketball?");
		Question q3 = new Question("What is the speed of a cheetah?");
		QuestionParser.printer(q1);
		QuestionParser.printer(q2);
		QuestionParser.printer(q3);
		
		String source1 = "Scientific investigation has resulted in several culturally transformative shifts in" +
		" our view of the planet. In the West, belief in a flat Earth was displaced by the idea of spherical Earth," + 
		" credited to Pythagoras in the 6th century BC. Earth was further believed to be the center of the universe" +
		" until the 16th century, when scientists first theorized that it was a moving object, comparable to the other"
		+ " planets in the Solar System. Due to the efforts of influential Christian scholars and clerics such as "
		+ "James Ussher, who sought to determine the age of Earth through analysis of genealogies in Scripture, Westerners "
		+ "prior to the 19th century generally believed Earth to be a few thousand years old at most. It was only during the "
		+ "19th century that geologists realized Earth's age was at least many millions of years. Lord Kelvin used "
		+ "thermodynamics to estimate the age of Earth to be between 20 million and 400 million years in 1864, sparking a "
		+ "vigorous debate on the subject; it was only when radioactivity and radioactive dating were discovered in the late "
		+ "19th and early 20th centuries that a reliable mechanism for determining Earth's age was established, proving the "
		+ "planet to be billions of years old. The perception of Earth shifted again in the 20th century when humans "
		+ "first viewed it from orbit, and especially with photographs of Earth returned by the Apollo program.";
		
		String source2 = "At almost all levels of competition, the top of the rim is exactly 10 feet (3.05 meters) above "
				+ "the court and 4 feet (1.21 meters) inside the baseline. While variation is possible in the dimensions "
				+ "of the court and backboard, it is considered important for the basket to be of the correct height – a "
				+ "rim that is off by just a few inches can have an adverse effect on shooting. The size of the basketball "
				+ "is also regulated. For men, the official ball is 29.5 inches (74.93 cm) in circumference (size 7, or a "
				+ "295 ball) and weighs 22 oz (623.69 grams). If women are playing, the official basketball size is 28.5 "
				+ "inches (72.39 cm) in circumference (size 6, or a 285 ball) with a weight of 20 oz (567 grams). In 3x3, "
				+ "a formalized version of the halfcourt 3-on-3 game, a dedicated ball with the circumference of a size 6 "
				+ "ball but the weight of a size 7 ball is used in all competitions (men's, women's, and mixed teams).";
		
		String source3 = "The cheetah is famous for being the fastest land animal.[60] Its hunting success does not, "
				+ "however, depend on raw speed alone, but also on rapid acceleration and deceleration, and an ability "
				+ "to execute drastic changes in direction while moving at speed.[61] Cheetahs can accelerate to 75 "
				+ "km/h (47 mph) within two seconds. Over short distances, their estimated top speed ranges from 90 "
				+ "to 128 km/h (56 to 80 mph), covering up to 7 m (23 ft) with each stride.[citation needed] The highest "
				+ "reliably recorded top speed is 29ms–1, which is about 104 km/h (65 mph).[62] As this is an averaged "
				+ "value, a cheetah's maximum speed is presumably still higher.[63]";
		
		List<List<ImmutablePair<String, Integer>>> answers = new ArrayList<List<ImmutablePair<String, Integer>>>();
		AnswerFinder find = new AnswerFinder(source1);
		answers = find.findAnswer(q1);
		for(List<ImmutablePair<String, Integer>> l : answers)
		{
			for(ImmutablePair<String, Integer> ll : l)
			{
				System.out.printf("Answer: '%s', confidence '%d'\n", ll.left, ll.right);
			}
		}
		
		System.out.println("------------------------------------------------------------");
		
		find = new AnswerFinder(source2);
		answers = find.findAnswer(q2);
		for(List<ImmutablePair<String, Integer>> l : answers)
		{
			for(ImmutablePair<String, Integer> ll : l)
			{
				System.out.printf("Answer: '%s', confidence '%d'\n", ll.left, ll.right);
			}
		}
		
		System.out.println("------------------------------------------------------------");
		
		find = new AnswerFinder(source3);
		answers = find.findAnswer(q3);
		for(List<ImmutablePair<String, Integer>> l : answers)
		{
			for(ImmutablePair<String, Integer> ll : l)
			{
				System.out.printf("Answer: '%s', confidence '%d'\n", ll.left, ll.right);
			}
		}
    }
}
