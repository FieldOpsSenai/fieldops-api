package br.com.fieldops.api.presentation.controller;

import br.com.fieldops.api.domain.entity.RefreshToken;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.service.RefreshTokenService;
import br.com.fieldops.api.infrastructure.security.TokenService;
import br.com.fieldops.api.presentation.dto.LoginRequestDTO;
import br.com.fieldops.api.presentation.dto.RefreshRequestDTO;
import br.com.fieldops.api.presentation.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gestão de sessão de usuários")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(
        summary = "Realizar login",
        description = "Autentica um usuário e retorna tokens de acesso (JWT) e renovação (Refresh Token)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados da requisição inválidos",
            content = @Content
        )
    })
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = 
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha());

            Authentication auth = authenticationManager.authenticate(usernamePassword);
            Usuario usuario = (Usuario) auth.getPrincipal();

            String token = tokenService.gerarToken(usuario);
            RefreshToken refreshToken = refreshTokenService.criarRefreshToken(usuario);

            TokenResponseDTO response = new TokenResponseDTO(
                token,
                usuario.getPerfil().getNome(),
                usuario.getNome(),
                usuario.getEmail(),
                86400L
            );

            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Renovar sessão",
        description = "Gera um novo token JWT de acesso e um novo Refresh Token utilizando um Refresh Token válido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sessão renovada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido, expirado ou revogado",
            content = @Content
        )
    })
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO request) {
        try {
            Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(request.refreshToken());

            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            RefreshToken refreshToken = refreshTokenService.verificarExpiracaoERevogacao(tokenOpt.get());
            Usuario usuario = refreshToken.getUsuario();

            String novoAccessToken = tokenService.gerarToken(usuario);
            RefreshToken novoRefreshToken = refreshTokenService.criarRefreshToken(usuario);

            TokenResponseDTO response = new TokenResponseDTO(
                novoAccessToken,
                novoRefreshToken.getToken(),
                usuario.getPerfil().name(),
                usuario.getNome(),
                usuario.getEmail(),
                86400L
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Captura o erro de token expirado/revogado e devolve 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Encerrar sessão",
        description = "Invalida o Refresh Token do usuário autenticado no sistema",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        refreshTokenService.revogarTokenPorUsuario(usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obter dados do usuário logado",
        description = "Retorna os dados do usuário com base no Token JWT enviado no Header Authorization",
        security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário autenticado e token válido"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token ausente, inválido ou expirado"
        )
    })
    public ResponseEntity<TokenResponseDTO> me(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TokenResponseDTO response = new TokenResponseDTO(
            null,
            usuario.getPerfil().getNome(),
            usuario.getNome(),
            usuario.getEmail(),
            null
        );
        return ResponseEntity.ok(response);
    }
}