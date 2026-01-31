package com.eduarda.lanchonete.controller;

import com.eduarda.lanchonete.model.Produto;
import com.eduarda.lanchonete.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoRepository repo;

    public ProdutoController(ProdutoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Produto> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return repo.save(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        return repo.findById(id).map(p -> {
            p.setNome(dados.getNome());
            p.setDescricao(dados.getDescricao());
            p.setPreco(dados.getPreco());
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
