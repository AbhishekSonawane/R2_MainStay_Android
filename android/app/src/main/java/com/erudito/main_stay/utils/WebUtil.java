package com.erudito.main_stay.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtil {

    public static String callInBackground(Context context, String url, String jsonParam) {
        URL txtUrl;
        int CONN_TIMEOUT = 30000;
        int READ_TIMEOUT = 60000;

        try {
            txtUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) txtUrl.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("charset", "utf-8");
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setConnectTimeout(CONN_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);

            String urlParameters = jsonParam.replace("&", "%26").replace("+", "%2B");

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes("data=" + urlParameters);
            wr.flush();
            wr.close();


            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + txtUrl.toString());
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            String resultStr = response.toString();
            return resultStr;
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
