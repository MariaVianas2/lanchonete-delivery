package com.eduarda.lanchonete.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime criadoEm = LocalDateTime.now();

    private String status = "ABERTO";

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PedidoItem> itens = new ArrayList<>();

    public Pedido() {}

    public Long getId() { 
        return id; 
    }

    public Cliente getCliente() { 
        return cliente; 
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente; 
    }

    public LocalDateTime getCriadoEm() { 
        return criadoEm; 
    }

    public void setCriadoEm(LocalDateTime criadoEm) { 
        this.criadoEm = criadoEm; 
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public List<PedidoItem> getItens() { 
        return itens; 
    }

    public void setItens(List<PedidoItem> itens) { 
        this.itens = itens; 
    }

    public double calcularTotal() {
        double total = 0.0;
        for (PedidoItem item : itens) {
            total += item.getPrecoUnitario() * item.getQuantidade();
        }
        return total;
    }


    public void adicionarItem(PedidoItem item) {
        item.setPedido(this);
        this.itens.add(item);
    }
}
