package NIOExample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NIOClientSimple {
    private final static int PORT = 3000;

    public static void main(String[] args) throws IOException, InterruptedException {

        // Подключение к серверу
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("localhost", PORT));
        System.out.println("Connecting to Server on port " + PORT);

        // Создаем список, который будем передавать на сервер
        ArrayList <String> fruits = new ArrayList<String>();
        fruits.add("Banana");
        fruits.add("Apple");
        fruits.add("Strawberry");
        fruits.add("Orange");

        for (String fruit: fruits) {
            byte [] message = new String(fruit).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            clientChannel.write(buffer);
            System.out.println("Sending... " + fruit);
            buffer.clear();
            Thread.sleep(1000);
        }
        clientChannel.close();
    }
}
