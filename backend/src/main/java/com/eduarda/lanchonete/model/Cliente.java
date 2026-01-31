package com.eduarda.lanchonete.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="clientes")
public class Cliente {

    @Id
    private Long id;
    private String nome;
    private String telefone;
    private String endereco;

    public Cliente(){
   
    }

    public Cliente(Long id, String nome, String telefone, String endereco){
        this.id=id;
        this.nome=nome;
        this.telefone=telefone;
        this.endereco=endereco;
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

    public String getTelefone(){
        return telefone;
    }

    public void setTelefone(String telefone){
        this.telefone=telefone;
    }

    public String getEndereco(){
        return endereco;
    }

    public void setEndereco(String endereco){
        this.endereco=endereco;
    }

    public String resumo(){
        return nome + " - " + telefone; 
    }

}
