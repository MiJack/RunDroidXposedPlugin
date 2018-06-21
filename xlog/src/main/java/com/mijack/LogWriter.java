package com.mijack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * The log file writer
 *
 * @author Mi&Jack
 */
public class LogWriter {
    public static final int BUFFER_SIZE = 1024 * 400; //400k

    private static final String TAG = "XLog";


    public static synchronized void d(int hookId, int pid, int threadId, String msg) {

        Writer writer = checkFileWriter(hookId);
        if (writer == null) {
            return;
        }
        try {
            writer.write(msg);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static FileWriter checkFileWriter(int hookId) {
        File logFile = obtainLogFile(hookId);
        if (logFile == null || !logFile.exists()) {
            return null;
        }
        try {
            return new FileWriter(logFile, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File obtainLogFile(int hookId) {
        int processId = XlogUtils.getProcessId();
        String processName = XlogUtils.getProcessName();
        String fileName = null;
        if (hookId > 0) {
            fileName = "/data/data/" + processName + "/files/"
                    + processName + "_system_" + processId + ".log";
        } else {
            fileName = "/data/data/" + processName + "/files/"
                    + processName + "_" + processId + ".log";
        }
        File file = new File(fileName);
        if (!file.exists()) {
            // check parent folder
            if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
                return null;
            }
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
