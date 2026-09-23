package com.pedidos360.productos_ms.repository;

import com.pedidos360.productos_ms.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Descuenta stock de forma atomica (UPDATE directo, no lee+escribe), para
     * que dos pedidos simultaneos del mismo producto no se pisen entre si.
     * La condicion "stock >= cantidad" evita que el stock quede negativo.
     * Devuelve cuantas filas se actualizaron: 0 significa que no habia stock
     * suficiente (o el producto no existe).
     */
    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);
}