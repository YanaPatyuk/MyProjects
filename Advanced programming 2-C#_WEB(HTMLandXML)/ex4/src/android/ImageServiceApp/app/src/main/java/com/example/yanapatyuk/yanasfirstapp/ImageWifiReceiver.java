package com.example.yanapatyuk.yanasfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageWifiReceiver extends BroadcastReceiver {
    private ClientTcp clientTcp;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    startTransfer(context);
                }
            }
        }

    }

    /**
     * Connect to the server and send the pictures
     */
    private void startTransfer(Context context) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Picture Transfer").setContentText("Transfer in progress").setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(1, builder.build());
        Toast.makeText(context, "Pictures started transfering", Toast.LENGTH_SHORT).show();

        // Getting the Camera Folder
        final File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        final List<File> pics = new ArrayList<File>();

        if (dcim == null) {//if no dcim folder return
            return;
        }
        //get list of images
        FindImages(dcim, pics);
        //get pictures from file.
        if (pics.isEmpty()) return;
        //create a thread to run in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                //create a client
                clientTcp = new ClientTcp();
                //check if we already connected-this check prevent two threads to run
                if (!clientTcp.ConnectToServer()) {
                    return;
                }

                int numberOfPics = pics.size();
                //send each image.
                for (int i = 0; i < numberOfPics; i++) {
                    clientTcp.SendPicture(pics.get(i));
                    builder.setProgress(numberOfPics, i, false);
                    //update progress bar.
                    notificationManager.notify(1, builder.build());
                }
                // At the End-update the bar
                builder.setContentText("Sending complete").setProgress(0, 0, false);
                notificationManager.notify(1, builder.build());
                clientTcp.close();
            }
        }).start();
    }

    /**
     * Find in recursion way all subfolders in direcory folder and adf to list.
     * @param directory
     * @param files
     */
    public void FindImages(File directory, List<File> files) {
        // Get all the files from a directory.
        File[] fList = directory.listFiles();
        //if no such file.
        if(fList == null) return;
        //check each file if its a file or folder.
        for (File file : fList) {
            if (file.isFile()) {//add to imagelist
                files.add(file);
            } else if (file.isDirectory()) {//find images in folder
                FindImages(file, files);
            }
        }
    }

}
