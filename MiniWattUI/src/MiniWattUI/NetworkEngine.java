package MiniWattUI;

import com.lowagie.text.pdf.codec.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.pdfbox.io.IOUtils;
import org.json.*;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;


enum QuestionType
{
    TEXT,
    PDF,
    JPG
};

enum SourceType
{
    TEXT,
    PDF,
    JPG,
    NULL
};

enum ResponseType
{
    TEXT,
    PDF
};


/**
 * Created by cdwil on 10/15/2015.
 */
public class NetworkEngine
{
    String requestUrl = "This is a filler";
    HttpGet httpget = new HttpGet("http://www.google.com/search?hl=en&q=httpclient&btnG=Google+Search&aq=f&oq=");

    public String Base64Encode(FileInputStream fs) {
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(fs);
        }
        catch(IOException e)
        {
            System.out.println(e.toString());
        }

        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(bytes);
    }

    public void post_question(Object question, QuestionType questionType, Object source, SourceType sourceType, ResponseType responseType)
    {
        JSONObject questionRequest = new JSONObject();

        questionRequest.append("Question", question);
        questionRequest.append("QuestionType", questionType);
        questionRequest.append("Source", source);
        questionRequest.append("SourceType", sourceType);
        questionRequest.append("ResponseType", responseType);

        // command to Post task
    }

    public void recieve_answer() {
        //get task (requestUrl, client);

        JSONObject answerResponse = null;

       /* try {
            //answerResponse =
        } catch (ArgumentNullException) {
        }

        if (answerResponse == null) {
            //Server response error
        }

        try {
            // send results to be shown
        } catch (ArgumentNullException) {
        }*/

    }

}
