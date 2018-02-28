package com.blackumbrella;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;


public class Main {

    public static String encodeParameterMap(Map<String, String> paramMap) {
        String urlParams = "";
        try {
            List<String> parameterList = new ArrayList<>();
            if (paramMap != null) {
                Iterator<?> itr = paramMap.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr.next();
                    String value = URLEncoder.encode((String) pair.getValue(), "UTF-8");
                    parameterList.add(pair.getKey() + "=" + value);
                }
            }
            urlParams = String.join("&", parameterList);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlParams;
    }

    public static String readArticle(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                stringBuilder.append(line + "<br>");
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void callSummryAPI(String text) {
        /*
        SM_API_KEY=N       // Required, N represents your registered API key.
        SM_URL=X           // Optional, X represents the webpage to summarize.
        SM_LENGTH=N        // Optional, N represents the number of sentences returned, default is 7
        SM_KEYWORD_COUNT=N // Optional, N represents how many of the top keywords to return
        SM_WITH_BREAK      // Optional, inserts string [BREAK] between sentences
        SM_WITH_ENCODE      // Optional, converts all HTML entities in the string to their applicable characters
        SM_IGNORE_LENGTH      // Optional, returns summary regardless of quality or length
        SM_QUOTE_AVOID     // Optional, sentences containing quotation marks will not be included in the summary
        SM_QUESTION_AVOID     // Optional, sentences containing question marks will not be included in the summary
        SM_EXCLAMATION_AVOID     // Optional, sentences containing exclamation marks will not be included in the summary

        Here are the possible indexes of the array returned in a JSON array.

        $result = json_decode($response, true);
        $result['sm_api_message'];         // Contains notices, warnings, and error messages.
        $result['sm_api_character_count']; // Contains the amount of characters returned
        $result['sm_api_title'];           // Contains the title when available
        $result['sm_api_content'];         // Contains the summary
        $result['sm_api_keyword_array'];   // Contains top ranked keywords in descending order
        $result['sm_api_error'];           // Contains error code
        */
        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("SM_API_KEY", "DA76225C4C");
        urlParameters.put("SM_LENGTH", "10");
        urlParameters.put("SM_KEYWORD_COUNT", "3");
        //urlParameters.put("SM_BREAK", "");

        Map<String, String> postParameters = new HashMap<>();
        /*String encodedText = Base64.getEncoder().encodeToString(
                text.getBytes(StandardCharsets.UTF_8));*/
        System.out.println(text);
        postParameters.put("sm_api_input", text);
        String output;

        String path = "http://api.smmry.com/" + encodeParameterMap(urlParameters);
        URL url = null;
        try {

            url = new URL(path);
            System.out.println("Path: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            for(Map.Entry<String, String> keyValuePair: postParameters.entrySet()) {
                System.out.println("Key is: " + keyValuePair.getKey());
                System.out.println("Value is: " + keyValuePair.getValue());
                conn.setRequestProperty(keyValuePair.getKey(), keyValuePair.getValue());
            }
            conn.setUseCaches(false);
            /*conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(text);
            writer.flush();
            writer.close();
            os.close();*/

            //String response = new String(conn.getResponseMessage());
            BufferedInputStream inputStream = new BufferedInputStream(
                    conn.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write((char) c);
            }
            output = new String(outputStream.toByteArray(), "UTF-8");

            System.out.println(output);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void callDatumboxAPI(String text) {
        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("api_key", "aec186189bd3df3fe34f0d91eae79b6e");
        urlParameters.put("n", "3");
        urlParameters.put("text", text);
        //urlParameters.put("SM_BREAK", "");

        /*Map<String, String> postParameters = new HashMap<>();
        System.out.println(text);
        postParameters.put("sm_api_input", text);*/
        String output;

        String path = "http://api.datumbox.com/1.0/KeywordExtraction.json?" + encodeParameterMap(urlParameters);
        URL url = null;
        try {

            url = new URL(path);
            System.out.println("Path: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            //String response = new String(conn.getResponseMessage());
            BufferedInputStream inputStream = new BufferedInputStream(
                    conn.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write((char) c);
            }
            output = new String(outputStream.toByteArray(), "UTF-8");
            //System.out.println(output);

            JSONObject obj = new JSONObject(output);
            JSONObject keywordObjs = obj.getJSONObject("output").getJSONObject("result");

            for(int i = 1; i <= 3; i++) {
                JSONObject keywordObj = keywordObjs.getJSONObject(Integer.toString(i));
                System.out.println(keywordObj);
            }
            //System.out.println(obj);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void summarizeArticles() {
        String text = readArticle("cnn_article.txt");
        callDatumboxAPI(text);
    }

    public static void main(String[] args) {
        summarizeArticles();
    }
}
