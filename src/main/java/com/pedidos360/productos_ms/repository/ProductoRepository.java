package com.pedidos360.productos_ms.repository;

import com.pedidos360.productos_ms.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
