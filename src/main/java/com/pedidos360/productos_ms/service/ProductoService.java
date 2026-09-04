package com.pedidos360.productos_ms.service;

import com.pedidos360.productos_ms.dto.ProductoDTO;
import com.pedidos360.productos_ms.exception.RecursoNoEncontradoException;
import com.pedidos360.productos_ms.model.Producto;
import com.pedidos360.productos_ms.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;

    public List<ProductoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public ProductoDTO obtener(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
        return toDTO(producto);
    }

    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = new Producto(null, dto.getNombre(), dto.getDescripcion(), dto.getPrecio(), dto.getStock(), dto.getCategoria(), null);
        return toDTO(repository.save(producto));
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setStock(dto.getStock());
        existente.setCategoria(dto.getCategoria());
        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getStock(), p.getCategoria());
    }
}
