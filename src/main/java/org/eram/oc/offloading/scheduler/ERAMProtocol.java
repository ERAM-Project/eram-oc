package org.eram.oc.offloading.scheduler;

import android.util.Log;

import org.eram.common.Utils;
import org.eram.common.Messages;
import org.eram.common.settings.Constants;
import org.eram.core.app.Task;
import org.eram.oc.offloading.Executor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Future;

public class ERAMProtocol implements Protocol {

    private  final String TAG ="Offloading Protocol";

    @Override
    public void sendApk(String apkName, String mAppName, ObjectInputStream input, ObjectOutputStream output) {

        try {
            Log.d(TAG, "Getting apk data");
            File apkFile = new File(apkName);
            Log.d(TAG, "Apk name - " + apkName);

            output.write(Messages.DO_YOU_NEED_SOURCE_CODE);

            output.writeObject(mAppName);
            output.writeInt((int) apkFile.length());
            output.flush();

            int response = input.read();

            if (response == Messages.I_NEED_SOURCE_CODE) {

                FileInputStream fin = new FileInputStream(apkFile);
                BufferedInputStream bis = new BufferedInputStream(fin);

                Log.d(TAG, "Sending apk");
                byte[] tempArray = new byte[Constants.BUFFER_SIZE_APK];
                int read;
                int cmp = 0;
                while ((read = bis.read(tempArray, 0, tempArray.length)) > -1) {
                    output.write(tempArray, 0, read);
                    output.flush();
                    cmp += read;
                    Log.d(TAG, "Sent " + cmp+ " of " + apkFile.length() + " bytes");
                }
                Utils.close(bis);
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    @Override
    public Future<Object> execute(TaskExecutor taskExecutor) {
        return Executor.execute(taskExecutor);
    }
}
