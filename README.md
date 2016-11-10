# 7digital-api-preview-clip-java
7digital API preview clip を用いて試聴用サンプルトラックを自動取得するプログラム
7digital > userdata > 1.tsv に記入されている楽曲を全て自動取得するプログラム

main class - API 初期設定

	String consumerkey = "YOUR_CONSUMER_KEY";
	String consumerSecret = "YOUR_CONSUMER_SECRET";

//example -> shop ID 496

		String shopid = "YOUR_SHOP_ID";

//example -> country = "ww";

		String country = "END_USER_COUNTRY"
 
  
//Get and save mp3 - track ID を指定して直接ダウンロード保存するには, 事前にtrackIDを取得しておく必要がある
//clipNum = "trackID"
//example -> clipNum = "4893756";

			RequestTokenGetter getClip = new RequestTokenGetter(consumerkey, consumerSecret, country, clipNum, shopid);
			GetFile(getClip.getClipUrl(), fileName);
    
