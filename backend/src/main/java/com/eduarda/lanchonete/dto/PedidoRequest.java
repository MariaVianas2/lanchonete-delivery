package com.eduarda.lanchonete.dto;

import java.util.List;

public class PedidoRequest {

    private Long clienteId;
    private List<ItemRequest> itens;

    public Long getClienteId() { 
        return clienteId; 
    }

    public void setClienteId(Long clienteId) { 
        this.clienteId = clienteId; 
    }

    public List<ItemRequest> getItens() { 
        return itens; 
    }

    public void setItens(List<ItemRequest> itens) { 
        this.itens = itens; 
    }

    public static class ItemRequest {
        private Long produtoId;
        private int quantidade;

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    }
}
