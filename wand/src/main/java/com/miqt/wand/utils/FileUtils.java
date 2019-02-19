package com.miqt.wand.utils;

import android.content.Context;

import com.miqt.wand.Encrypter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FileUtils {

    public interface DownLoadListener {
        void downLoadProgress(long curr, long total);
    }

    public static File downloadFile(String urlPath, String downloadDir, DownLoadListener listener) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            if (urlPath.startsWith("https")) {
                HttpsURLConnection con = (HttpsURLConnection) httpURLConnection;
                //信任所有
                X509TrustManager x509mgr = new X509TrustManager() {

                    //　　该方法检查客户端的证书，若不信任该证书则抛出异常
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }

                    // 　　该方法检查服务端的证书，若不信任该证书则抛出异常
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }

                    // 　返回受信任的X509证书数组。
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{x509mgr}, null);
                ////创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
                con.setSSLSocketFactory(sslContext.getSocketFactory());
                con.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);

            System.out.println("file length---->" + fileLength);

            URLConnection con = url.openConnection();

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            String path = new File(downloadDir).getParent()
                    + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                if (listener != null) {
                    listener.downLoadProgress(len, fileLength);
                }
            }
            out.flush();
            out.close();
            bin.close();
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean copyFileFromAssets(Encrypter encrypter, Context context, String assetName, String path) {
        boolean bRet = false;
        try {
            InputStream is = context.getAssets().open(assetName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            if (encrypter != null) {
                encrypter.decrypt(fos, is);
            } else {
                byte[] temp = new byte[64];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    public static boolean copyFile(Encrypter encrypter, Context context, String from, String to) {
        boolean bRet = false;
        try {
            FileInputStream is = new FileInputStream(from);
            File file = new File(to);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            if (encrypter != null) {
                encrypter.decrypt(fos, is);
            } else {
                byte[] temp = new byte[64];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }
}
