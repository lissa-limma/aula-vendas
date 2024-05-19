package io.github.lissalimma.domain.repository;

import io.github.lissalimma.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Clientes extends JpaRepository<Cliente, Integer> {

    @Query(value = "select * from cliente c where c.nome like '%:nome%'", nativeQuery = true)
    List<Cliente> encontrarPorNome( @Param("nome") String cli);

}
