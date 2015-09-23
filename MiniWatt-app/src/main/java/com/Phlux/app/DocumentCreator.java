import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public final class DocumentCreator 
{
	private DocumentCreator()
	{
		throw new AssertionError();
	}
	
	public static PDDocument createAnswerDoc(String[] questions, String[] answers) throws IOException, COSVisitorException
	{
		PDDocument pdfAnswers = new PDDocument();
		PDFont questionFont = PDType1Font.TIMES_ITALIC;
		PDFont answerFont = PDType1Font.TIMES_ROMAN;
		
		PDPage page = new PDPage(PDPage.PAGE_SIZE_LETTER);
		pdfAnswers.addPage(page);
		
		PDRectangle mediabox = page.findCropBox();
		
	    float margin = 50;
	    float width = mediabox.getWidth() - 2*margin;
	    float startX = mediabox.getLowerLeftX() + margin;
	    float startY = mediabox.getUpperRightY() - margin;
	    float height = 600;
	    
	    float fontSize = 12;
	    float leading = 1.5f * fontSize;
		
		int lastSpace = -1;
		float curWidth = 0;
		float curHeight = 0;
		PDPageContentStream contentStream = new PDPageContentStream(pdfAnswers, page);
		contentStream.beginText();
		contentStream.moveTextPositionByAmount(startX, startY);
		for(int i = 0; i < questions.length; i++)
		{
			contentStream.setFont(questionFont, fontSize);
			while(questions[i].length() > 0)
			{
				int space = questions[i].indexOf(' ', lastSpace + 1);
				if(space < 0)
					space = questions[i].length();
				
				String subString = questions[i].substring(0, space);
				float textWidth = fontSize * questionFont.getStringWidth(subString) / 1000;
				if(textWidth + curWidth > width)
				{
					if(lastSpace < 0)
						lastSpace = space;
					subString = questions[i].substring(0, lastSpace);
					contentStream.drawString(subString);
					questions[i] = questions[i].substring(lastSpace).trim();
					lastSpace = -1;
					
					contentStream.moveTextPositionByAmount(0, -leading);
					curWidth = 0;
				}
				else if(space == questions[i].length())
				{
					contentStream.drawString(questions[i]);
					questions[i] = "";
					curWidth += textWidth;
				}
				else
					lastSpace = space;
			}
			
			contentStream.drawString(" ");
			curWidth += fontSize * answerFont.getStringWidth(" ") / 1000;
			contentStream.setFont(answerFont, fontSize);
			lastSpace = -1;
			while(answers[i].length() > 0)
			{
				int space = answers[i].indexOf(' ', lastSpace + 1);
				if(space < 0)
					space = answers[i].length();
				
				String subString = answers[i].substring(0, space);
				float textWidth = fontSize * answerFont.getStringWidth(subString) / 1000;
				if(textWidth + curWidth > width)
				{
					if(lastSpace < 0)
						lastSpace = space;
					subString = answers[i].substring(0, lastSpace);
					contentStream.drawString(subString);
					answers[i] = answers[i].substring(lastSpace).trim();
					lastSpace = -1;
					
					contentStream.moveTextPositionByAmount(0, -leading);
					curWidth = 0;
				}
				else if(space == answers[i].length())
				{
					contentStream.drawString(answers[i]);
					answers[i] = "";
					curWidth += textWidth;
				}
				else
					lastSpace = space;
			}
			
			curWidth = 0;
			contentStream.moveTextPositionByAmount(0, -leading);
			
			curHeight += leading;
			if(curHeight > height)
			{
				contentStream.endText();
				contentStream.close();
				
				PDPage newPage = new PDPage(PDPage.PAGE_SIZE_LETTER);
				pdfAnswers.addPage(newPage);
				
				contentStream = new PDPageContentStream(pdfAnswers, newPage);
				contentStream.beginText();
				contentStream.moveTextPositionByAmount(startX, startY);
				curHeight = 0;
			}
		}
		contentStream.endText();
		contentStream.close();
		
		/*To create the filename*/
		Calendar curTime = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("h'hrs',m'mins',s'sec'");
		String filName = "";
		filName += format.format(curTime.getTime());
		filName += ".pdf";
		
		pdfAnswers.save(filName);
		pdfAnswers.close();
		
		return pdfAnswers;
	}
}
