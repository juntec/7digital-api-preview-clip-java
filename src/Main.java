import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Main {

	private static void prn(String st){
		System.out.println(st);
	}

	private static void pr(String st){
		System.out.print(st);
	}

	public static void main(String[] args){
		String consumerkey = "7dafk5tnpnw2";
		String consumerSecret = "a9hbe8rknw6wrdn4";
		String clipNum = "52105287";
		String shopid = "496";
		String country = "ww";


		try {
			RequestTokenGetter getClip = new RequestTokenGetter(consumerkey, consumerSecret, country, clipNum, shopid);
			prn(getClip.getClipUrl());
			executeHttpGet(getClip.getClipUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public static void executeHttpGet(String st){
		String urlString = st;
		URL url;
		try {
			url = new URL(urlString);
			URLConnection conn = null;
			try {
				conn = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				InputStream is = conn.getInputStream();
				prn(String.valueOf(is.read()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
