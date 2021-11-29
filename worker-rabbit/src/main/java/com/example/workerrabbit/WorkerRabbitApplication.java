package com.example.workerrabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkerRabbitApplication {

    private static final String NAME_FILA = "fila_worker";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(NAME_FILA, true, false, false, null);
        System.out.println("[*] Aguardando mensagens.");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), "UTF-8");

            System.out.println("[x] Recebido '" + mensagem + "'");
            try {
                doWork(mensagem);
            } finally {
                System.out.println("[x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(NAME_FILA, false, deliverCallback, consumerTag -> {
        });

    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}
