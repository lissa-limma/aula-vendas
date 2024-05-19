package io.github.lissalimma.domain.repository;

import io.github.lissalimma.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
