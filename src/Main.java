import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	private static void prn(String st){
		System.out.println(st);
	}
	private static void pr(String st){
		System.out.print(st);
	}

	public static void main(String[] args) throws MalformedInputException,ProtocolException, IOException {
		
		//API 初期設定
		String consumerkey = "7dafk5tnpnw2";
		String consumerSecret = "a9hbe8rknw6wrdn4";
		String shopid = "496";
		String country = "ww";
				
		prn("-------------------------");
		//############################################
		//7digital track search でtrack IDの検索
		//楽曲名で検索し完全一致した楽曲名に対して
		//アーティスト名で完全一致した楽曲のIDを取得.
		//example http://api.7digital.com/1.2/track/search?q=Marc%20Mac%20Freeze&oauth_consumer_key=7dafk5tnpnw2&country=ww&pagesize=10
		//############################################
		String artist = "Marc Mac";
		String titile = "Jazz Flares";
		String fileName = artist + " " + titile;

		//preview clip ID の取得
		String searchCatalogue = "http://api.7digital.com/1.2/track/search?q=" + URLEncoder(fileName) + "&oauth_consumer_key=7dafk5tnpnw2&country=ww&pagesize=10";
		GetCatalogueFile(searchCatalogue, fileName);

		//xml の読み込み
		XmlReader ds=new XmlReader(fileName+".xml");
		//完全一致したtitle, artist のtrackIDを取得		
		//preview clip を取得しmp3形式で保存する
		String clipNum = ds.walkThrough(artist, titile);;
		prn("get mp3 of " + clipNum);
		
		try {
			//clipNum に track ID を指定
			RequestTokenGetter getClip = new RequestTokenGetter(consumerkey, consumerSecret, country, clipNum, shopid);
			GetFile(getClip.getClipUrl(), fileName);
			
			prn("finish save mp3");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static String URLEncoder(String setArtist){
		String enc = "utf-8";
		String encArtist = "";
		try {
			encArtist = (URLEncoder.encode(setArtist, enc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		encArtist = encArtist.replace("+", "%20");
		encArtist = encArtist.replace("*", "%2a");
		encArtist = encArtist.replace("-", "%2d");
		
		return encArtist;
	} 
	
	  
	public static void GetCatalogueFile(String st, String saveName){
		try {
			URI uri =new URI(st);
			URL url=uri.toURL();
			URLConnection urlcon =url.openConnection();
			InputStream fileIS =urlcon.getInputStream();
			File saveFile = new File(saveName+".xml");
			FileOutputStream fileOS = new FileOutputStream(saveFile);
			int c;
			while((c =fileIS.read()) != -1) fileOS.write((byte) c);
			fileOS.close();
			fileIS.close();
		} catch (URISyntaxException e) {
			System.err.println(e);
		} catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}


	public static void GetFile(String st, String filename){
		try {
			//API の URL
			URI uri =new URI(st);
			URL url=uri.toURL();
			URLConnection urlcon =url.openConnection();
			InputStream fileIS =urlcon.getInputStream();
			File saveFile = new File(filename+".mp3");
			FileOutputStream fileOS = new FileOutputStream(saveFile);
			int c;
			while((c =fileIS.read()) != -1) fileOS.write((byte) c);
			fileOS.close();
			fileIS.close();

		} catch (URISyntaxException e) {
			System.err.println(e);
		} catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}



}
