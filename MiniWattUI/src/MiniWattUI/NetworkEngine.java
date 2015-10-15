package MiniWattUI;

import com.lowagie.text.pdf.codec.Base64;
import com.sun.deploy.util.SessionState;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.params.HttpParams;
import org.apache.pdfbox.io.IOUtils;
import org.json.*;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


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

    public void post_question(String[] questions, QuestionType questionType, Object source, SourceType sourceType, ResponseType responseType, int numQuestions) throws Exception
    {
        JSONObject questionRequest = new JSONObject();

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        for(int i = 0; i < numQuestions; i++)
            pairs.add(new BasicNameValuePair("Question" + Integer.toString(i), questions[i]));

        pairs.add(new BasicNameValuePair("QuestionType", questionType.toString()));
        pairs.add(new BasicNameValuePair("Source", source.toString()));
        pairs.add(new BasicNameValuePair("SourceType", sourceType.toString()));
        pairs.add(new BasicNameValuePair("ResponseType", responseType.toString()));
        pairs.add(new BasicNameValuePair("NumQuestions", Integer.toString(numQuestions)));

        // command to Post task
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        HttpResponse response = null;
        try
        {
            client.start();
            HttpPost postRequest = new HttpPost(postUrl);
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(pairs);
            postRequest.setEntity(requestEntity);
            Future<HttpResponse> future = client.execute(postRequest, null);
            response = future.get();

            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity);
        }
        finally
        {
            client.close();
        }
    }
}
