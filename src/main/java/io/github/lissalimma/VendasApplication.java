package io.github.lissalimma;

import io.github.lissalimma.domain.entity.Cliente;
import io.github.lissalimma.domain.entity.Produto;
import io.github.lissalimma.domain.repository.Clientes;
import io.github.lissalimma.domain.repository.Produtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@SpringBootApplication()
@RestController
public class VendasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
