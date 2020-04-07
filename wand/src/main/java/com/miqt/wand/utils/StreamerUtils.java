package com.miqt.wand.utils;

import android.database.Cursor;
import android.os.Build;

import java.io.Closeable;
import java.net.HttpURLConnection;
import java.nio.channels.FileLock;
import java.util.zip.ZipFile;

public class StreamerUtils {

    public static void safeClose(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                if (Build.VERSION.SDK_INT > 18) {
                    closeable.close();
                }
            } catch (Throwable e) {
            }
        }
    }

    public static void safeClose(FileLock closeable) {
        if (closeable != null) {
            try {
                if (Build.VERSION.SDK_INT > 18) {
                    closeable.close();
                } else {
                    closeable.release();
                }
            } catch (Throwable e) {
            }
        }
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void safeClose(Cursor closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void safeClose(ZipFile closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
            }
        }
    }

    public static void safeClose(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

    }

//    public static void safeClose(Process proc) {
//        if (proc != null) {
//            try {
//                proc.exitValue();
//            } catch (Throwable t) {
//                if (BuildConfig.ENABLE_BUGLY) {
//                    BuglyUtils.commitError(t);
//                }
////                proc.destroy();
//            }
//            proc = null;
//
//        }
//
//    }

    public static void safeClose(ProcessBuilder pb) {
        if (pb != null) {
            pb.directory();
            pb = null;
        }

    }

}
