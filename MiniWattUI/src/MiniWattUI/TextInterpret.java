package MiniWattUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 * Class used by back-end to parse PDF documents and images. Extracts questions from text
 * pulled from these files. Do not instantiate. Simply call methods.
 *
 */
public final class TextInterpret 
{	
	private TextInterpret()
	{
		throw new AssertionError();
	}
	private static final String tessPath = "C:/Tess4J";
	
	/*
	 * Given a list of strings, the function extracts all questions from within the strings
	 * (even questions that are within a sentence--providing it matches the regex pattern)
	 */
	public static ArrayList<String> extractQuestions(String text)
	{
		String[] textBrokenOut = text.split("\n");
		ArrayList<String> questions = new ArrayList<String>();
		Pattern questionPattern = Pattern.compile("[A-Z][A-Za-z0-9\t ,:-;\"]*\\?");
		for(String s : textBrokenOut)
		{
			Matcher matcher = questionPattern.matcher(s);
			while(matcher.find())
				questions.add(matcher.group());
		}
		
		return questions;
	}
	
	/*
	 * Given a File that points to a valid image (.png, .bmp, .jpg), will extract
	 * all text from the image
	 */
	public static String parseImage(File file) throws IOException, TesseractException
	{
		Tesseract t = new Tesseract();
		t.setDatapath(tessPath);
		String rawText = t.doOCR(file);
		
		return rawText;
	}
	
	/*
	 * Given a File that points to a PDF with searchable text, will extract
	 * all text from the PDF file.
	 */
	public static String parseDocument(File file) throws IOException
	{
		String rawText;
		
		PDFParser parser = new PDFParser(new FileInputStream(file));
		parser.parse();
		PDDocument doc = new PDDocument(parser.getDocument());
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(1);
		stripper.setEndPage(doc.getNumberOfPages());
		
		rawText = stripper.getText(doc);
		doc.close();
		
		return rawText;
	}
	
	/*
	 * Given a File that points to a PDF with searchable text, will extract
	 * all text from the PDF file.
	 */
	public static String parseDocument(PDDocument doc) throws IOException
	{
		String rawText;

		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(1);
		stripper.setEndPage(doc.getNumberOfPages());
		
		rawText = stripper.getText(doc);
		doc.close();

		return rawText;
	}
}

