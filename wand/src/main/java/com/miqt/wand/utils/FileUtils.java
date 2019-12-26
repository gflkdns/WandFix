package com.miqt.wand.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
        BufferedInputStream bin = null;
        FileOutputStream out = null;
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
                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }

                    // 　　该方法检查服务端的证书，若不信任该证书则抛出异常
                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }

                    // 　返回受信任的X509证书数组。
                    @Override
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
            bin = new BufferedInputStream(httpURLConnection.getInputStream());
            String path = new File(downloadDir).getParent()
                    + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file);
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

        } catch (Throwable e) {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Throwable ex) {
            }
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (Throwable ex) {
            }
            return null;
        }
        return file;
    }

    public static boolean copyFileFromAssets(Context context, String assetName, String path) {
        boolean result = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists() || !file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            is = context.getAssets().open(assetName);
            fos = new FileOutputStream(file);
            byte[] temp = new byte[64];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean copyFile(Context context, String from, String to) {
        boolean bRet = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(from);
            File file = new File(to);
            if (!file.getParentFile().exists() || !file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            byte[] temp = new byte[64];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bRet;
    }

    public static void deleteFile(File file) {
        try {
            if (file == null || !file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File item : files) {
                        deleteFile(item);
                    }
                }
            }
            boolean b = file.delete();
            if (!b) {
                file.deleteOnExit();
            }
        } catch (Throwable e) {
        }
    }
}
