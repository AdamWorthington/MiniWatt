package MiniWattUI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



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

    public static ArrayList<MiniWattResult> post_question(ArrayList<String> questions, String source) throws Exception
    {
        ArrayList<Question> parameterQuestions = new ArrayList<Question>();
        ArrayList<ArrayList<String>> sources = new ArrayList<ArrayList<String>>();
        int pageNum = 2;
        GoogleScraper gs = new GoogleScraper(pageNum);

        for (String question : questions)
        {
            ArrayList<String> curSourcesForQ = new ArrayList<String>();
            //Make the question object. If it is invalid, ignore it.
            Question temp = new Question(question);
            if(temp.getType() == Question.QuestionType.INVALID)
                continue;
            //Add it to the list of running questions
            parameterQuestions.add(temp);

            String subject = temp.getSubject();
            System.err.println("b: " + subject);
            String[] input = subject.split(" ");
            System.err.println("c: " + Arrays.toString(input));
            ArrayList<String> links = gs.getLinks(input);
            System.err.println("d: " + links);

            //Get the links, given the input
            ArrayList<String> gsInfo = gs.getInfo(links, input);
            //If we get some content back , add it to our current sources.
            if(gsInfo != null)
                curSourcesForQ.addAll(gsInfo);

            //If JSoup links failed to generate content, do a direct check of Wikipedia
            if(sources.isEmpty())
            {
                //public getInfo(String URL, String SUBJECT, String QUESTION)
                String info = "";
                try {
                    String link = "http://en.wikipedia.org/wiki/" + subject.replace(' ', '_');
                    Document doc = Jsoup.connect(link).get();
                    Elements paragraphs = doc.select("p");
                    for (Element p : paragraphs) {
                        info += "\n" + p.text();
                    }
                }
                catch (Exception e)
                {
                    System.err.println(e.toString());
                }

                //If we got some wikipedia content, add that.
                if(info.isEmpty() == false)
                    curSourcesForQ.add(info);
            }

            //If we are passed a reference source, then add that to the list of sources.
            if(source != null && source.isEmpty() == false)
                curSourcesForQ.add(source);

            sources.add(curSourcesForQ);
        }

        System.err.println("2: " + sources.toString());
        AnswerPackager AP = new AnswerPackager(parameterQuestions, sources);
        ArrayList<MiniWattResult> answers = AP.getAnswers();

        return answers;

        /*JSONObject jobj = new JSONObject();

        if(source != null)
        {
            jobj.put("hasSourceDoc", 1);
            jobj.put("sourceDoc", "");
        }
        else
        {
            jobj.put("hasSourceDoc", 0);
            jobj.put("sourceDoc", source);
        }

        jobj.put("questions", questions);

        // command to Post task
        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        try
        {
            HttpPost postRequest = new HttpPost(postUrl);
            StringEntity se = new StringEntity(jobj.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            postRequest.setEntity(se);
            response = client.execute(postRequest, (org.apache.http.protocol.HttpContext)null);

            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();

            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            System.out.println(out.toString());   //Prints the string content read from input stream
            return out.toString();
        }
        finally
        {
            client.close();
        }*/
    }
}
