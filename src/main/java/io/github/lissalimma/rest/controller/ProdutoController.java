package io.github.lissalimma.rest.controller;

import io.github.lissalimma.domain.entity.Cliente;
import io.github.lissalimma.domain.entity.Produto;
import io.github.lissalimma.domain.repository.Clientes;
import io.github.lissalimma.domain.repository.Produtos;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable Integer id ) {
        return produtos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save( @RequestBody @Valid Produto produto) {
        return produtos.save(produto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtos.findById(id)
                .map( p -> {
                    produtos.delete(p);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update( @PathVariable Integer id, @RequestBody @Valid Produto produto) {
        produtos.findById(id)
                .map( p -> {
                    produto.setId(p.getId());
                    produtos.save(produto);
                    return produto;
                }).orElseThrow( () ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado") );
    }

    @GetMapping
    public List<Produtos> find( Produto filtro ) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING );

        Example example = Example.of(filtro, matcher);
        return produtos.findAll(example);
    }
}
