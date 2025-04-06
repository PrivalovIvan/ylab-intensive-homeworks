package com.ylab.finance_tracker_spring_boot.security_canon;

import com.ylab.finance_tracker_spring_boot.dto.UserDTO;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


/*
	•	JwtService умеет генерировать токен с подписью и сроком жизни.
	•	Из него можно вытащить email (по которому потом найдем пользователя).
	•	Есть метод isTokenValid, чтобы проверить, что токен не устарел и корректен.
 */
@Service
public class JwtService {
    private final String SECRET_KEY = "my-super-secret-jwt-key-that-should-be-very-long";

    // Получаем ключ из строки
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Генерация токена
    public String generateToken(UserDTO user) {
        return Jwts.builder()
                .subject(user.getEmail())                             // Кто выпустил токен (пользователь)
                .claim("role", user.getRole().name())
                .issuedAt(new Date(System.currentTimeMillis())) // Когда выпущен
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Срок действия (1 день)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Алгоритм шифрования
                .compact();
    }

    // Извлечение username из токена
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    // Проверяем, не истёк ли токен
    public boolean isTokenValid(String token) {
        try {
            return !extractExpiration(token).before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Извлекаем время истечения токена
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
