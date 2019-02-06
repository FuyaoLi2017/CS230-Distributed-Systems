package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {

    public static void main(String args[]) throws Exception {
        String host = "192.168.56.1";  // the server waiting to be connected
        int port = 8888;   // specify the port to be connected
        // set up a connection with server
        Socket client = new Socket(host, port);
        // write data the server after setting up the connection
        Writer writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
        writer.write("Hello, Server. this is a message from client.");
        writer.write("eof\n");
        writer.flush();
        // read after writing the message
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        // time out: 10 seconds
        client.setSoTimeout(10*1000);
        StringBuffer sb = new StringBuffer();
        String temp;
        int index;
        try {
            while ((temp=br.readLine()) != null) {
                if ((index = temp.indexOf("eof")) != -1) {
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("data reading timeout.");
        }
        System.out.println("Server: " + sb);
        writer.close();
        br.close();
        client.close();
    }
}
