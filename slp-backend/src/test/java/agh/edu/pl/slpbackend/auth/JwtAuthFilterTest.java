package agh.edu.pl.slpbackend.auth;

import agh.edu.pl.slpbackend.auth.JwtAuthFilter;
import agh.edu.pl.slpbackend.auth.JwtUtil;
import agh.edu.pl.slpbackend.auth.MyUserDetailsService;
import agh.edu.pl.slpbackend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class JwtAuthFilterTest {

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;


    private final JwtUtil jwtUtil = new JwtUtil();

    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    void setUp() {
        myUserDetailsService = new MyUserDetailsService(userRepository);
    }

    @Test
    void should_not_filter() {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, myUserDetailsService);

        when(request.getServletPath()).thenReturn("/users/login");

        assertThat(jwtAuthFilter.shouldNotFilter(request)).isTrue();
    }

    @Test
    void should_filter() {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, myUserDetailsService);

        when(request.getServletPath()).thenReturn("/sample");

        assertThat(jwtAuthFilter.shouldNotFilter(request)).isFalse();
    }

    @Test
    void do_filter_internal() throws ServletException, IOException {
        String email = "worker@gmail.com";
        String token = jwtUtil.generateToken(email);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, myUserDetailsService);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void token_with_unauthorized_email() throws IOException, ServletException {
        String email = "unauthorized";
        String token = jwtUtil.generateToken(email);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, myUserDetailsService);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(401, "Invalid token");
    }

    @Test
    void token_expired() throws IOException, ServletException {
        String email = "worker@gmail.com";
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, "SecretKey")
                .compact();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtil, myUserDetailsService);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(401, "Token expired");
    }
}
