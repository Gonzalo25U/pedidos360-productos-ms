package com.pedidos360.productos_ms.mensajeria;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuracion de RabbitMQ para productos-ms. Define la cola, el exchange y el binding para recibir eventos de pedidos-ms.
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_PEDIDOS = "pedidos.eventos";
    public static final String ROUTING_KEY_PEDIDO_CREADO = "pedido.creado";
    public static final String QUEUE_DESCONTAR_STOCK = "productos.descontar-stock";

    @Bean
    public TopicExchange exchangePedidos() {
        return new TopicExchange(EXCHANGE_PEDIDOS, true, false);
    }

    @Bean
    public Queue colaDescontarStock() {
        return new Queue(QUEUE_DESCONTAR_STOCK, true);
    }

    @Bean
    public Binding bindingDescontarStock(Queue colaDescontarStock, TopicExchange exchangePedidos) {
        return BindingBuilder.bind(colaDescontarStock).to(exchangePedidos).with(ROUTING_KEY_PEDIDO_CREADO);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}