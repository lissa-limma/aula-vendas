package io.github.lissalimma.rest.controller;

import io.github.lissalimma.domain.entity.ItemPedido;
import io.github.lissalimma.domain.entity.Pedido;
import io.github.lissalimma.domain.entity.Produto;
import io.github.lissalimma.domain.enums.StatusPedido;
import io.github.lissalimma.domain.repository.Pedidos;
import io.github.lissalimma.domain.repository.Produtos;
import io.github.lissalimma.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.lissalimma.rest.dto.InformacoesItemPedidoDTO;
import io.github.lissalimma.rest.dto.InformacoesPedidoDTO;
import io.github.lissalimma.rest.dto.PedidoDTO;
import io.github.lissalimma.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return service.obterPedido(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
            @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO.builder().id(pedido.getId())
                .data(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")))
                .cpf(pedido.getCliente().getCpf())
                .nome(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .items(converter(pedido.getItens()))
                .status(pedido.getStatus().name())
                .build();
    }

    private List<InformacoesItemPedidoDTO> converter(List<ItemPedido> itens) {
        if(CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(
                item -> InformacoesItemPedidoDTO
                        .builder().descricao(item.getProduto().getDescricao())
                        .preco(item.getProduto().getPreco())
                        .qtd(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }


}
