/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2017 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 17-11-3 上午9:56  	     zenglc           	    Create/Add/Modify/Delete
 * ============================================================================
 */

package com.pax.order.logger;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.pax.order.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppLog {

  /**
   * log日志级别
   */
  public enum EDebugLevel {
    DEBUG_V,
    DEBUG_D,
    DEBUG_I,
    DEBUG_W,
    DEBUG_E,
  }

  /**
   * 同时控制V/D/I/W/E 5种输出开关
   */
  public static boolean DEBUG_V = false;
  public static boolean DEBUG_D = false;
  public static boolean DEBUG_I = false;
  public static boolean DEBUG_W = false;
  public static boolean DEBUG_E = false;

  private static final String TAG = "LOG";
  private static boolean isLogToFile = false;// 控制是否记录文件
  private static long fileSize = 1 * 1024 * 1024;  // 1M
  private static String defaultLogFilePath = "/sdcard/LogUtil.txt";//默认文件
  private static final String LOG_DATE = "yyyy-MM-dd HH:mm:ss";
  private static RandomAccessFile randomAccessFile;

  /**
   * 初始化 AppLog
   *
   * @param tag
   * @param debugFlag     是否开启 log
   * @param logToFileFlag 是否输出 log 到 /sdcard/LogUtil.txt
   * @param logfilepath   设置 log 文件路径
   * @param context
   */
  public static void init(String tag, boolean debugFlag, boolean logToFileFlag, String logfilepath, Context context) {
    PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
//            .logStrategy(new LogCatStrategy())
            .methodCount(2)
            .tag(tag)
            .build();
    Logger.addLogAdapter(new AndroidLogAdapter(strategy) {
      @Override
      public boolean isLoggable(int priority, String tag) {
        return BuildConfig.DEBUG;
      }
    });

    debug(debugFlag);
    isLogToFile = logToFileFlag;
    defaultLogFilePath = logfilepath;
    if (isLogToFile) {
      File file = new File(defaultLogFilePath);
      // 如果日志文件不存在则创建文件
      try {
        if (!file.exists()) {
          boolean createfile = file.createNewFile();
          if (createfile) {
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
          }
        }

        randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.skipBytes((int) randomAccessFile.length());

      } catch (IOException e) {
        e.printStackTrace();
        Log.w(TAG, " logfile create failed");
      }
    }
  }

  /**
   * 同时控制V/D/I/W/E 5种输出开关
   *
   * @param debugFlag 开关, true打开, false关闭
   */
  public static void debug(boolean debugFlag) {
    DEBUG_V = debugFlag;
    DEBUG_D = debugFlag;
    DEBUG_I = debugFlag;
    DEBUG_W = debugFlag;
    DEBUG_E = debugFlag;
  }

  /**
   * 分别控制V/D/I/W/E 5种输出开关
   *
   * @param debugFlag 开关, true打开, false关闭
   */
  public static void debug(EDebugLevel debugLevel, boolean debugFlag) {
    if (debugLevel == EDebugLevel.DEBUG_V) {
      DEBUG_V = debugFlag;
    } else if (debugLevel == EDebugLevel.DEBUG_D) {
      DEBUG_D = debugFlag;
    } else if (debugLevel == EDebugLevel.DEBUG_I) {
      DEBUG_I = debugFlag;
    } else if (debugLevel == EDebugLevel.DEBUG_W) {
      DEBUG_W = debugFlag;
    } else if (debugLevel == EDebugLevel.DEBUG_E) {
      DEBUG_E = debugFlag;
    }
  }

  /**
   * 输出v级别log, 内部根据设置的开关决定是否真正输出log
   *
   * @param tag 同系统android.util.log的tag定义
   * @param msg 待输出的信息
   */
  public static void v(String tag, String msg) {
    if (DEBUG_V) {
      Logger.t(tag).v(msg);
      setLogToFile(tag, msg);
    }
  }

  public static void v(String msg) {
    if (DEBUG_V) {
      Logger.v(msg);
      setLogToFile(TAG, msg);
    }
  }

  /**
   * 输出d级别log, 内部根据设置的开关决定是否真正输出log
   *
   * @param tag 同系统android.util.log的tag定义
   * @param msg 待输出的信息
   */
  public static void d(String tag, String msg) {
    if (DEBUG_D) {
      Logger.t(tag).d(msg);
      setLogToFile(tag, msg);
    }
  }

  public static void d(String msg) {
    if (DEBUG_D) {
      Logger.d(msg);
      setLogToFile(TAG, msg);
    }
  }

  /**
   * 输出i级别log, 内部根据设置的开关决定是否真正输出log
   *
   * @param tag 同系统android.util.log的tag定义
   * @param msg 待输出的信息
   */
  public static void i(String tag, String msg) {
    if (DEBUG_I) {
      Logger.t(tag).i(msg);
      setLogToFile(tag, msg);
    }
  }

  public static void i(String msg) {
    if (DEBUG_I) {
      Logger.i(msg);
      setLogToFile(TAG, msg);
    }
  }

  /**
   * 输出w级别log, 内部根据设置的开关决定是否真正输出log
   *
   * @param tag 同系统android.util.log的tag定义
   * @param msg 待输出的信息
   */
  public static void w(String tag, String msg) {
    if (DEBUG_W) {
      Logger.t(tag).w(msg);
      setLogToFile(tag, msg);
    }
  }

  public static void w(String msg) {
    if (DEBUG_W) {
      Logger.w(msg);
      setLogToFile(TAG, msg);
    }
  }

  public static void w(String msg, Throwable tr) {
    if (DEBUG_W) {
      Log.w(TAG, tr);
      setLogToFile(TAG, tr.getMessage());
    }
  }

  public static void w(String tag, String msg, Throwable tr) {
    if (DEBUG_W) {
      Log.w(tag, msg, tr);
      setLogToFile(tag, tr.getMessage());
    }
  }

  /**
   * 输出e级别log, 内部根据设置的开关决定是否真正输出log
   *
   * @param tag 同系统android.util.log的tag定义
   * @param msg 待输出的信息
   */
  public static void e(String tag, String msg) {
    if (DEBUG_E) {
      Logger.t(tag).e(msg);
      setLogToFile(tag, msg);
    }
  }

  public static void e(String msg) {
    if (DEBUG_E) {
      Logger.e(msg);
      setLogToFile(TAG, msg);
    }
  }

  public static void e(String msg, Exception e) {
    if (DEBUG_W) {
      Log.e(TAG, msg, e);
      setLogToFile(TAG, e.getMessage());
    }
  }

  public static void e(String tag, String msg, Exception e) {
    if (DEBUG_W) {
      Log.e(tag, msg, e);
      setLogToFile(tag, e.getMessage());
    }
  }

  /**
   * 根据记录标识，保存日志到文件
   *
   * @param tag
   * @param args
   */
  public static void setLogToFile(String tag, String args) {

    if (isLogToFile) {
      StringBuilder sb = new StringBuilder();
      sb.append(tag);
      sb.append("\t\t");
      sb.append(args);
      writeLogToFileByRandomFile(randomAccessFile, formateLog(sb.toString()));
    }
  }

  /**
   * 格式化 log
   *
   * @param mesg
   * @return
   */
  private static String formateLog(String mesg) {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LOG_DATE);
    sb.append(simpleDateFormat.format(new Date()));
    sb.append("\t");
    sb.append(mesg);
    sb.append("\r\n");
    //        Log.d("formateLog",sb.toString());
    return sb.toString();
  }

  /**
   * 写 log 到随机存储文件
   *
   * @param raf
   * @param msg
   */
  private static void writeLogToFileByRandomFile(RandomAccessFile raf, String msg) {
    if (!TextUtils.isEmpty(msg) && randomAccessFile != null) {
      try {
        long pointer = raf.getFilePointer();
        long nextp = pointer + msg.length();
        // 文件已经满了
        if (nextp >= fileSize) {
          // Log.d(TAG,"seek....");
          //指针回到文件开头
          raf.setLength(0);
          raf.seek(0);
        }
        raf.writeBytes(new String(msg.getBytes(), "UTF-8"));

      } catch (IOException e) {

      }
    }
  }
}