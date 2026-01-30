package com.eduarda.lanchonete.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private double preco;

    public Produto(){
    }

    public Produto(Long id, String nome, String descricao, double preco){
        this.id=id;
        this.nome=nome;
        this.descricao=descricao;
        this.preco=preco;
    }
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome=nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao=descricao;
    }

    public double getPreco(){
        return preco;
    }

    public void setPreco(double preco){
        this.preco=preco;
    }

    public String resumo(){
        return nome + " - R$ "+ preco;
    }
}

