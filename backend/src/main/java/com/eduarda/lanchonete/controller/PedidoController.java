package com.eduarda.lanchonete.controller;

import com.eduarda.lanchonete.dto.PedidoRequest;
import com.eduarda.lanchonete.model.Cliente;
import com.eduarda.lanchonete.model.Pedido;
import com.eduarda.lanchonete.model.PedidoItem;
import com.eduarda.lanchonete.model.Produto;
import com.eduarda.lanchonete.repository.ClienteRepository;
import com.eduarda.lanchonete.repository.PedidoRepository;
import com.eduarda.lanchonete.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoRepository pedidoRepo;
    private final ClienteRepository clienteRepo;
    private final ProdutoRepository produtoRepo;

    public PedidoController(PedidoRepository pedidoRepo, ClienteRepository clienteRepo, ProdutoRepository produtoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
        this.produtoRepo = produtoRepo;
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest req) {

        Cliente cliente = clienteRepo.findById(req.getClienteId()).orElse(null);
        if (cliente == null) return ResponseEntity.badRequest().body("Cliente não encontrado");

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        if (req.getItens() == null || req.getItens().isEmpty()) {
            return ResponseEntity.badRequest().body("Pedido precisa ter pelo menos 1 item");
        }

        for (PedidoRequest.ItemRequest itemReq : req.getItens()) {
            Produto produto = produtoRepo.findById(itemReq.getProdutoId()).orElse(null);
            if (produto == null) return ResponseEntity.badRequest().body("Produto não encontrado: " + itemReq.getProdutoId());

            if (itemReq.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Quantidade inválida para produto: " + itemReq.getProdutoId());
            }

            PedidoItem item = new PedidoItem();
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            pedido.adicionarItem(item);
        }

        Pedido salvo = pedidoRepo.save(pedido);
        return ResponseEntity.ok(salvo);
    }
}
