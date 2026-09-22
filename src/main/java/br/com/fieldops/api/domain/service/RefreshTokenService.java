package br.com.fieldops.api.domain.service;

import br.com.fieldops.api.domain.entity.RefreshToken;
import br.com.fieldops.api.domain.entity.Usuario;
import br.com.fieldops.api.domain.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${api.security.refresh-token.expiration-ms:86400000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken criarRefreshToken(Usuario usuario) {
        // Remove token antigo para não estourar a constraint UNIQUE de usuario_id
        refreshTokenRepository.deleteByUsuario(usuario);
        refreshTokenRepository.flush();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataExpiracao(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevogado(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verificarExpiracaoERevogacao(RefreshToken token) {
        // Chamada corrigida para o método correto da entidade: isRevogado()
        if (token.isRevogado() || token.getDataExpiracao().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expirado ou revogado");
        }
        return token;
    }

    @Transactional
    public void revogarTokenPorUsuario(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
    }
}