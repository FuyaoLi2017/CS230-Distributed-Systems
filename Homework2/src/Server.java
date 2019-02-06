import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) throws IOException {
        int port = 8888;
        // specify a socket listening at port 8899
        ServerSocket server = new ServerSocket(port);
        while (true) {
            // try to get connection with other sockets, accept the message in a blocking queue
            Socket socket = server.accept();
            // every time, create a new thread to deal with a new socket
            new Thread(new Task(socket)).start();
        }
    }

    /**
     * class used to handle requests
     */
    static class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                handleSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * communicates with client socket
         * @throws Exception
         */
        private void handleSocket() throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String temp;
            int index;
            while ((temp=br.readLine()) != null) {
                System.out.println(temp);
                if ((index = temp.indexOf("eof")) != -1) {// stop to accept new information when receiving eof
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
            System.out.println("Client: " + sb);
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            writer.write("Hello Client, this is a message from server.");
            writer.write("eof\n");
            writer.flush();
            writer.close();
            br.close();
            socket.close();
        }
    }
}
