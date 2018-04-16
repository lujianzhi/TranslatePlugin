package util;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ian.Lu on 2018/4/3.
 * Project : TranslatePlugin
 */

public class HttpManager {

    private static HttpManager ourInstance = new HttpManager();

    public static HttpManager getInstance() {
        return ourInstance;
    }

    private HttpManager() {

    }

    /**
     * POST请求
     */
    public String post(String url, Map<String, String> requestParams) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        /**HttpPost*/
        HttpPost httpPost = new HttpPost(url);
        List params = new ArrayList();
        Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            if (value != null) {
                params.add(new BasicNameValuePair(key, value));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        /**HttpResponse*/
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
