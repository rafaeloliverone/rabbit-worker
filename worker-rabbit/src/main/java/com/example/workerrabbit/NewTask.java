package com.example.workerrabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
    private static final String FILA_NAME = "fila_worker";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");


        try (Connection connection = connectionFactory.newConnection ();
             Channel channel = connection.createChannel ()) {
            Boolean durable = true;
            channel.queueDeclare (FILA_NAME, durable, false, false, null);
            String mensagem = "Rafael Oliveira Batista";

            channel.basicPublish ("", FILA_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    mensagem.getBytes("UTF-8"));
            System.out.println ("[x] Enviado '" + mensagem + "'");
        }
    }
}
