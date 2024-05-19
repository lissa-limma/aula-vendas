package io.github.lissalimma.service.impl;

import io.github.lissalimma.domain.entity.Cliente;
import io.github.lissalimma.domain.entity.ItemPedido;
import io.github.lissalimma.domain.entity.Pedido;
import io.github.lissalimma.domain.entity.Produto;
import io.github.lissalimma.domain.enums.StatusPedido;
import io.github.lissalimma.domain.repository.Clientes;
import io.github.lissalimma.domain.repository.ItemsPedido;
import io.github.lissalimma.domain.repository.Pedidos;
import io.github.lissalimma.domain.repository.Produtos;
import io.github.lissalimma.exception.PedidoNaoEncontradoException;
import io.github.lissalimma.exception.RegraNegocioException;
import io.github.lissalimma.rest.dto.ItemPedidoDTO;
import io.github.lissalimma.rest.dto.PedidoDTO;
import io.github.lissalimma.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidos;
    private final Clientes clientes;
    private final Produtos produtos;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientes.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        pedidos.save((pedido));
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if(items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem items");
        }

        return items.stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtos.findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setQuantidade(dto.getQuantidade());
                itemPedido.setPedido(pedido);
                itemPedido.setProduto(produto);
                return itemPedido;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obterPedido(Integer id) {
        return pedidos.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido status) {
        pedidos.findById(id)
                .map( pedido -> {
                    pedido.setStatus(status);
                    return pedidos.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }
}
