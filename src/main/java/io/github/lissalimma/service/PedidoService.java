package io.github.lissalimma.service;

import io.github.lissalimma.domain.entity.Pedido;
import io.github.lissalimma.domain.enums.StatusPedido;
import io.github.lissalimma.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedido(Integer id);
    void atualizarStatus(Integer id, StatusPedido status);
}
