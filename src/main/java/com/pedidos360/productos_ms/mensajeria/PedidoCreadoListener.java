package com.pedidos360.productos_ms.mensajeria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pedidos360.productos_ms.repository.ProductoRepository;

// Listener que recibe eventos de tipo PedidoCreadoEvent desde la cola de RabbitMQ y actualiza el stock de los productos involucrados en el pedido.
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCreadoListener {

    private final ProductoRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DESCONTAR_STOCK)
    @Transactional
    public void descontarStock(PedidoCreadoEvent evento) {
        log.info("Evento pedido.creado recibido (pedido {}), descontando stock...", evento.getPedidoId());

        for (PedidoCreadoEvent.ItemEvento item : evento.getItems()) {
            int filasActualizadas = repository.descontarStock(item.getProductoId(), item.getCantidad());
            if (filasActualizadas == 0) {
                // No se revierte el pedido (ya esta confirmado en pedidos-ms) - solo
                // se deja registrado como advertencia para revision manual. En un
                // sistema real, aqui se podria publicar un evento de "stock
                // insuficiente" para que otro proceso lo maneje.
                log.warn("No se pudo descontar stock del producto {} (cantidad solicitada: {}) - stock insuficiente o producto inexistente",
                        item.getProductoId(), item.getCantidad());
            } else {
                log.info("Stock descontado: producto {} -{} unidades", item.getProductoId(), item.getCantidad());
            }
        }
    }
}