package com.eduarda.lanchonete.repository;

import com.eduarda.lanchonete.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
