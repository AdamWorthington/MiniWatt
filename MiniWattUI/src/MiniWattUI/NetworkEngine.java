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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.pdfbox.io.IOUtils;
import org.json.*;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;



/**
 * Created by cdwil on 10/15/2015.
 * A class meant to handle all of the interfacing with the MiniWATT server.
 */
public class NetworkEngine
{
    private final static String postUrl = "http://1-dot-miniwatt-1099.appspot.com/work";

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

    public static void post_question(Queue<String> questions, String[] source) throws Exception
    {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        for(int i = 0; i < questions.size(); i++)
            pairs.add(new BasicNameValuePair("Question" + Integer.toString(i), questions.remove()));
        pairs.add(new BasicNameValuePair("NumQuestions", Integer.toString(questions.size())));

        if (source != null) {
            for (int i = 0; i < source.length; i++)
                pairs.add(new BasicNameValuePair("Source" + Integer.toString(i), source[i]));
            pairs.add(new BasicNameValuePair("SourceSize", Integer.toString(source.length)));
        }

        pairs.add(new BasicNameValuePair("SourceSize", Integer.toString(0)));

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            System.out.println(out.toString());   //Prints the string content read from input stream

        }
        finally
        {
            client.close();
        }
    }
}
