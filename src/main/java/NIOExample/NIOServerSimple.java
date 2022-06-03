package NIOExample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class NIOServerSimple {
    private final static int PORT = 3000;

    public static void main(String[] args) throws IOException{
        // Создание селектора
            Selector selector = Selector.open();

        // Открытие канала
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, serverSocketChannel.validOps(),null);

        // Создание буфера, который будет использоваться для записи и чтения
            ByteBuffer buffer = ByteBuffer.allocate(256);

            System.out.println("Server running on port " + PORT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();  //получения списка ключей от селектора

                // Просматриваем список ключей и выполняем необходимые действия
                Iterator <SelectionKey> iter = selectionKeySet.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // Проверяем готов ли канал выполнить подключение
                    if (key.isAcceptable()) {
                        // Выполняем соединение и меняем ключ на чтение
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection accepted: " + clientChannel.getLocalAddress());

                    //    Если канал готов к чтению
                    } else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        StringBuilder stringBuilder = new StringBuilder();
                        int read;
                        while ((read = clientChannel.read(buffer)) > 0) {  //считываем пока буфер не опустеет
                            buffer.flip();
                            stringBuilder.append(new String(Arrays.copyOf(buffer.array(), buffer.limit())));
                            buffer.clear();
                        }
                        if (read < 0) {
                            System.out.println("Connection stopped");
                            clientChannel.close(); // закрываем канал
                        } else {
                            System.out.println("Message received : " + stringBuilder);
                        }

                    }
                    iter.remove();
                }

            }

    }
}
