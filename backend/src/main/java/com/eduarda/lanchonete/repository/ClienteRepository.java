package com.eduarda.lanchonete.repository;

import com.eduarda.lanchonete.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
