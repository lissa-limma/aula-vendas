package io.github.lissalimma.rest.controller;

import io.github.lissalimma.domain.entity.Usuario;
import io.github.lissalimma.exception.SenhaInvalidaException;
import io.github.lissalimma.rest.dto.CredenciaisDTO;
import io.github.lissalimma.rest.dto.TokenDTO;
import io.github.lissalimma.security.jwt.JwtService;
import io.github.lissalimma.service.impl.UsuarioServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);

    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario user = Usuario.builder().login(credenciais.getLogin()).senha(credenciais.getSenha()).build();
            usuarioService.autenticar(user);
            String token = jwtService.gerarToken(user);
            return new TokenDTO(user.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
}
