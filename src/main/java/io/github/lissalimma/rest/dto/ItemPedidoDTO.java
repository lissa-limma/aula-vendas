package io.github.lissalimma.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    private Integer produto;
    private Integer quantidade;
}
