package com.Phlux.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class WorkServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /* report an error */
		}
		System.err.println("1: " + jb);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jb.toString());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IOException("Error parsing JSON request string");
		}

		ArrayList<Question> parameterQuestions = new ArrayList<Question>();
		ArrayList<String> questions = null;
		ArrayList<ArrayList<String>> sources = new ArrayList<ArrayList<String>>();
		int pageNum = 2;
		GoogleScraper gs = new GoogleScraper(pageNum);

		questions = getQuestions(jsonObject);
		System.err.println("a: " + questions);
		try {
			if (jsonObject.getInt("hasSourceDoc") == 1) {
				for (String question : questions) {
					Question temp = new Question(question);
					parameterQuestions.add(temp);
					String subject = temp.getSubject();
					String[] input = subject.split(" ");
					sources.add(gs.getInfo(gs.getLinks(input), input));
				}
				ArrayList<String> singleSource = new ArrayList<String>();
				singleSource.add(jsonObject.get("sourceDoc").toString());
				sources.add(singleSource);
			} else {
				for (String question : questions) {
					Question temp = new Question(question);
					parameterQuestions.add(temp);
					String subject = temp.getSubject();
					System.err.println("b: " + subject);
					String[] input = subject.split(" ");
					System.err.println("c: " + Arrays.toString(input));
					ArrayList<String> links = gs.getLinks(input);
					System.err.println("d: " + links);
					sources.add(gs.getInfo(links, input));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.err.println("2: " + sources.toString());
		AnswerPackager AP = new AnswerPackager(parameterQuestions, sources);
		ArrayList<MiniWattResult> answers = AP.getAnswers();

		/*
		 * String temp = jb.toString(); String[] input = temp.split(" ");
		 * ArrayList<String> info = gs.getInfo(gs.getLinks(input), input);
		 */
		JSONObject ret = new JSONObject();
		try {
			ret.put("data", answers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("3: " + ret);
		res.getWriter().println(ret);

	}

	public static ArrayList<String> getQuestions(JSONObject jo) {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			JSONArray jsonArray = (JSONArray) jo.get("questions");
			for (int i = 0; i < jsonArray.length(); i++) {
				ret.add(jsonArray.getString(i));
			}
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
