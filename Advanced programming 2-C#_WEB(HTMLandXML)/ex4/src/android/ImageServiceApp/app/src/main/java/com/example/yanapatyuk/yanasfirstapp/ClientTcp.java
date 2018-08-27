package com.example.yanapatyuk.yanasfirstapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClientTcp  {
    private InetAddress serverAddr;
    private Socket socket;

    private static boolean running = false;


    public ClientTcp() {}

    /**
     * Connect to the server
     */
    public boolean ConnectToServer() {
        if (running) {//prevent two processes to connect.
            return false;
        }
        try {
            //here you must put your computer's IP address.
            this.serverAddr = InetAddress.getByName("192.168.1.11");
            //create a socket to make the connection with the server
            this.socket = new Socket(serverAddr, 8005);
            running = true;
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
            this.serverAddr = null;
            this.socket = null;
            return false;
        }
        return true;
    }

    /**
     * send one picture to server
     * first the name and then the image.
     * @param pic file.
     */
    public void SendPicture(File pic) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            FileInputStream fis = new FileInputStream(pic);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            byte[] imgbyte = getBytesFromBitmap(bm);
            byte[] b = new byte[1];
            outputStream.write(pic.getName().getBytes());

            inputStream.read(b,0,1); // wait for finished reading img byte

            // convert length to byte array
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putLong(imgbyte.length);
            outputStream.write(buffer.array(),0,8); // length

            if (inputStream.read(b,0,1) == 1) { // wait for finish reading length bytes
                outputStream.write(imgbyte);
            }
            inputStream.read(b,0,1); // wait for finished reading img byte
            outputStream.flush();

        } catch (Exception e) {//if there was problem in sending.
            Log.e("TCP", "S: Error", e);
        }
    }


    /**
     * close the socket.
     */
    public void close() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("close_connection".getBytes());
            //output.write(-1);
            outputStream.flush();
            outputStream.close();
            this.socket.close();
            running = false;
        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    /**
     * convart picture to image
     * @param bitmap
     * @return
     */
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

}
