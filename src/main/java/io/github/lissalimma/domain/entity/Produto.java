package io.github.lissalimma.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    @NotEmpty(message = "Campo descrição é obrigatório")
    private String descricao;

    @Column(name = "preco_unitario")
    @NotNull(message = "Campo preço é obrigatório")
    private BigDecimal preco;

}
