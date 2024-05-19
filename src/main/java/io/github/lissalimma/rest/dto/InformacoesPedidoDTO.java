package io.github.lissalimma.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesPedidoDTO {
    private Integer id;
    private String cpf;
    private String nome;
    private BigDecimal total;
    private String data;
    private String status;
    List<InformacoesItemPedidoDTO> items;
}
