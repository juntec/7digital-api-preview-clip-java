import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import Decoder.BASE64Encoder;

public class RequestTokenGetter {

	public static void main(String[] args) throws Exception {

		// clip number 楽曲のID
		String clip = "52105287";
		// 使用するAPIのURL
		String tagetURL = "https://previews.7digital.com/clip/";

		// OAuthにおいて利用する変数宣言
		String consumerkey = "7dafk5tnpnw2";
		String consumerSecret = "a9hbe8rknw6wrdn4";
		String oauthToken = ""; // リクエストトークン取得時は利用しない
		String oauthTokenSecret = ""; // リクエストトークン取得時は利用しない
		String method = "GET";
		String urlStr = tagetURL + clip ;
		String shopID = "496";

		// OAuthにおいて利用する共通パラメーター
		// パラメーターはソートする必要があるためSortedMapを利用
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("oauth_consumer_key", consumerkey);
		params.put("oauth_signature_method", "HMAC-SHA1");
		params.put("oauth_timestamp", String.valueOf(getUnixTime()));

		// 本当はランダムに生成されるほうがよい　→　params.put("oauth_nonce", String.valueOf(Math.random()));
		params.put("oauth_nonce", "252525");

		params.put("oauth_version", "1.0");
		params.put("shopID", shopID);
		// params.put("oauth_token", oauthToken); // リクエストトークン取得時は利用しない

		{
			/*
			 * 署名（oauth_signature）の生成
			 */
			// パラメーターを連結する
			String paramStr = "";
			for (Entry<String, String> param : params.entrySet()) {
				paramStr += "&" + param.getKey() + "=" + param.getValue();
			}
			paramStr = paramStr.substring(1);

			// 署名対象テキスト（signature base string）の作成
			String text = method + "&" + urlEncode(urlStr) + "&"
					+ urlEncode(paramStr);

			// 署名キーの作成
			String key = urlEncode(consumerSecret) + "&"
					+ urlEncode(oauthTokenSecret);

			// HMAC-SHA1で署名を生成
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
					"HmacSHA1");
			Mac mac = Mac.getInstance(signingKey.getAlgorithm());
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(text.getBytes());
			String signature = new BASE64Encoder().encode(rawHmac);

			// 署名をパラメータに追加
			params.put("oauth_signature", signature);
			

		}

		// Authorizationヘッダの作成
		String paramStr = urlStr + "?";

		int count = 0;
		for (Entry<String, String> param : params.entrySet()) {
			if(count==0){
				paramStr += "&" + param.getKey() + "=" + urlEncode(param.getValue());
			}else{
				paramStr += param.getKey() + "=" + urlEncode(param.getValue());
			}
		}
		System.out.println(paramStr);
		

	}

	private static int getUnixTime() {
		return (int) (System.currentTimeMillis() / 1000L);
	}

	private static String urlEncode(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}