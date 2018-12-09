package four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujiatai on 2018/11/24.
 */
public class RabbitProducer4 {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routing_key_demo";
    private static final String QUEUE_NAME = "queue_demo_4";
    private static final String IP_ADDRESS = "192.168.25.133";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        Connection connection = factory.newConnection(); //创建连接
        Channel channel = connection.createChannel();   //创建信道
        //创建一个type="direct",持久化,非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        Map<String, Object> map = new HashMap<String, Object>();
        //设置消息
        map.put("x-message-ttl", 6000);
        //创建一个持久化,非排它的,非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, map);
        //将队列和交换机绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String msg = "hello";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        channel.close();
        connection.close();
    }
}
