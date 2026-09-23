package com.pedidos360.productos_ms.mensajeria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

//Este evento es publicado por pedidos-ms cuando se confirma un pedido. productos-ms lo consume para descontar stock de los productos involucrados.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreadoEvent {
    private Long pedidoId;
    private String usuarioId;
    private String emailUsuario;
    private BigDecimal total;
    private List<ItemEvento> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvento {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}