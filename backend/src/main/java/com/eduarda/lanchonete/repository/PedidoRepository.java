package com.eduarda.lanchonete.repository;

import com.eduarda.lanchonete.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
