package com.eduarda.lanchonete.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "pedido_itens")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private int quantidade;

    private double precoUnitario;

    public PedidoItem() {

    }

    public Long getId() { 
        return id; 
    }

    public Pedido getPedido() { 
        return pedido; 
    }
    public void setPedido(Pedido pedido) {
         this.pedido = pedido; 
    }

    public Produto getProduto() { 
        return produto; 
    }

    public void setProduto(Produto produto) {
         this.produto = produto; 
    }

    public int getQuantidade() { 
        return quantidade; 
    }

    public void setQuantidade(int quantidade) { 
        this.quantidade = quantidade; 
    }

    public double getPrecoUnitario() { 
        return precoUnitario; 
    }

    public void setPrecoUnitario(double precoUnitario) { 
        this.precoUnitario = precoUnitario; 
    }
}
