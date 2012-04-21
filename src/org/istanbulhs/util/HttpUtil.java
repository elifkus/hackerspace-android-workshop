package org.istanbulhs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;


public class HttpUtil {
    public static String getResponseStringForHttpRequest(String url) {
        return HttpUtil.getResponseStringForHttpRequest(url, "", null);
    }
    
    public static String getResponseStringForHttpRequest(String url, String query, Map<String, String> parameters) {
        return HttpUtil.convertStreamToString(HttpUtil.getResponseInputStreamForHttpRequest(url, query, parameters));
    }

    public static InputStream getResponseInputStreamForHttpRequest(String url, String query, Map<String, String> parameters) {
        if (query != null) {
            url += query;
        }
        
        if (parameters != null && parameters.size() > 0) {
            String parameterString = URLEncodedUtils.format(convertMapToNameValuePairList(parameters), "UTF-8");

            url += "?" + parameterString;
        }

        HttpGet method = new HttpGet(url);
        HttpResponse response;
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();

        try {
            response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                inputStream = entity.getContent();
            }
        } catch (ConnectTimeoutException e) {
            Log.e("HttpUtil", "There was a timeout error", e);
        } catch (SocketException e) {
            Log.e("HttpUtil", "There was a socket based error", e);
        } catch (ClientProtocolException e) {
            Log.e("HttpUtil", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("HttpUtil", "There was an IO Stream related error", e);
            Log.e("HttpUtil", "instream:" + inputStream);
        }

        return inputStream;
    }

    private static List<NameValuePair> convertMapToNameValuePairList(Map<String, String> parameters) {
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();

        for (Entry<String, String> entry : parameters.entrySet()) {
            requestParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return requestParams;
    }
    
    private static String convertStreamToString(InputStream inputStream) {

        String output = null;

        if (inputStream != null) {
            StringWriter stringWriter = new StringWriter();

            char[] chars = new char[1024];

            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                int n;

                while ((n = reader.read(chars)) != -1) {
                    stringWriter.write(chars, 0, n);
                }
            } catch (IOException e) {
                Log.e("IOUtil", "Error in reading from stream to string.", e);
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Log.e("IOUtil", "Error while trying to close stream");
                }
            }

            output = stringWriter.toString();
        }

        return output;
    }
}
