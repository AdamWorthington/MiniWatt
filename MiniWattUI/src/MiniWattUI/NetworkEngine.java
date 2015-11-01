package MiniWattUI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.pdfbox.io.IOUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.List;



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

    public static String post_question(Queue<String> questions, String[] source) throws Exception
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
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        try
        {
            HttpPost postRequest = new HttpPost(postUrl);
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(pairs);
            postRequest.setEntity(requestEntity);
            response = client.execute(postRequest, (org.apache.http.protocol.HttpContext)null);

            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            //System.out.println(out.toString());   //Prints the string content read from input stream
            return out.toString();

        }
        finally
        {
            client.close();
        }
    }
}
