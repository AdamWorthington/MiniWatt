package com.Phlux.app;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

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
		}*/
				
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
		}
    }
}
