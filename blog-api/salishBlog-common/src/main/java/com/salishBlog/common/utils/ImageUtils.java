package com.salishBlog.common.utils;

import com.salishBlog.common.config.SalishConfig;
import com.salishBlog.common.utils.uuid.Seq;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ImageUtils {

    public static File downloadImage(String uri) throws Exception {
        URL url = new URL(uri);
        InputStream is;

        if (url.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
            https.setSSLSocketFactory(createSSL());
            https.setConnectTimeout(5000);
            https.setReadTimeout(5000);
            https.setDoOutput(true);
            https.setRequestMethod("GET");
            https.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0");
            https.connect();
            System.out.println(https.getResponseCode() + " " + https.getResponseMessage());
            is = https.getInputStream();
        }else {
            //打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //通过输入流获取图片数据
            is = conn.getInputStream();
        }

        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(is);
        //创建一个文件对象用来保存图片
        String filePath;
        String osName = System.getProperties().getProperty("os.name");
        if (osName.equals("Linux")) {
            filePath = SalishConfig.getUploadPath();
        } else {
            filePath = SalishConfig.getWinProfile();
        }
        File imageFile = new File(filePath + "/download/" + DateUtils.datePath() + "/" + Seq.getId(Seq.uploadSeqType) + "Copy.jpg");
        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        //写入数据
        outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();
        return imageFile;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[6024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier(){

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };

    public static SSLSocketFactory createSSL() throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException{
        TrustManager[] tm =new TrustManager[]{
                myTrustManager
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, tm, null);
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        return ssf;
    }
    public static TrustManager myTrustManager = new X509TrustManager()
    {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1){}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

}
