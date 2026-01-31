package com.eduarda.lanchonete.repository;

import com.eduarda.lanchonete.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
