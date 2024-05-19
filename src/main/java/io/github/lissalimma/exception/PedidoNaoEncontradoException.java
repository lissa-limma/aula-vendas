package io.github.lissalimma.exception;

public class PedidoNaoEncontradoException extends  RuntimeException {
    public PedidoNaoEncontradoException() {
        super("Pedido não encontrado");
    }
}
