package MiniWattUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class DocumentUtils 
{
	private DocumentUtils()
	{
		throw new AssertionError();
	}
	
	public static PDDocument convertFromBase64(String strBase64) throws IOException
	{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = decoder.decodeBuffer(strBase64);
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		PDDocument doc = new PDDocument();
		doc = PDDocument.load(stream);
		
		return doc;
	}
	
	public static String covertDocToBase64(PDDocument doc) throws COSVisitorException, IOException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		doc.save(stream);
		byte[] bytes = stream.toByteArray();

		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(bytes);
	}

	public static PDDocument createAnswerDoc(ArrayList<MiniWattResult> results) throws IOException, COSVisitorException
	{
		PDDocument pdfAnswers = new PDDocument();
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

		StringBuilder builder = new StringBuilder();

		for(MiniWattResult mwr : results)
		{
			builder.append("Question: ");
			builder.append(mwr.getQuestion().getQuestionText());
			builder.append("\n");
			List<ImmutablePair<String, Integer>> data = mwr.getResults();
			for(ImmutablePair<String, Integer> ip : data)
			{
				builder.append("Answer: ");
				builder.append(ip.getLeft());
				builder.append("\n");
				builder.append("Confidence: ");
				builder.append(ip.getRight());
				builder.append("\n");
			}
			builder.append("\n");
		}

		String bigString = builder.toString();
		String[] broken = bigString.split("\n");

		contentStream.setFont(answerFont, fontSize);
		for(String toPrint : broken)
		{
			while (toPrint.length() > 0) {
				int space = toPrint.indexOf(' ', lastSpace + 1);
				if (space < 0)
					space = toPrint.length();

				String subString = toPrint.substring(0, space);
				float textWidth = fontSize * answerFont.getStringWidth(subString) / 1000;
				if (textWidth + curWidth > width) {
					if (lastSpace < 0)
						lastSpace = space;
					subString = toPrint.substring(0, lastSpace);
					contentStream.drawString(subString);
					toPrint = toPrint.substring(lastSpace).trim();
					lastSpace = -1;

					contentStream.moveTextPositionByAmount(0, -leading);
					curWidth = 0;
				} else if (space == toPrint.length()) {
					contentStream.drawString(toPrint);
					toPrint = "";
					curWidth += textWidth;
				} else
					lastSpace = space;
			}

			curWidth = 0;
			contentStream.moveTextPositionByAmount(0, -leading);

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

		return pdfAnswers;
	}



	public static PDDocument createAnswerDoc(String[] questions, List<List<String>> answers) throws IOException, COSVisitorException
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
			for(String answer : answers.get(i))
			{
				while(answer.length() > 0)
				{
					int space = answer.indexOf(' ', lastSpace + 1);
					if(space < 0)
						space = answer.length();

					String subString = answer.substring(0, space);
					float textWidth = fontSize * answerFont.getStringWidth(subString) / 1000;
					if(textWidth + curWidth > width)
					{
						if(lastSpace < 0)
							lastSpace = space;
						subString = answer.substring(0, lastSpace);
						contentStream.drawString(subString);
						answer = answer.substring(lastSpace).trim();
						lastSpace = -1;

						contentStream.moveTextPositionByAmount(0, -leading);
						curWidth = 0;
					}
					else if(space == answer.length())
					{
						contentStream.drawString(answer);
						answer = "";
					}
					else
						lastSpace = space;
				}
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
