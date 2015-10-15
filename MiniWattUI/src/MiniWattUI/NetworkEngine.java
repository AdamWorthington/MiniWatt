package MiniWattUI;

import com.lowagie.text.pdf.codec.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.pdfbox.io.IOUtils;
import org.json.*;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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
    private final String postUrl = "";
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

    public void post_question(String question, QuestionType questionType, Object source, SourceType sourceType, ResponseType responseType)
            throws UnsupportedEncodingException, ClientProtocolException, IOException
    {
        JSONObject questionRequest = new JSONObject();

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("Question", question));
        pairs.add(new BasicNameValuePair("QuestionType", questionType.toString()));
        pairs.add(new BasicNameValuePair("Source", source.toString()));
        pairs.add(new BasicNameValuePair("SourceType", sourceType.toString()));
        pairs.add(new BasicNameValuePair("ResponseType", responseType.toString()));

        // command to Post task
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(postUrl);
        UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(pairs);
        postRequest.setEntity(requestEntity);
        CloseableHttpResponse response = client.execute(postRequest);

        try
        {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }

    public void recieve_answer()
    {
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
