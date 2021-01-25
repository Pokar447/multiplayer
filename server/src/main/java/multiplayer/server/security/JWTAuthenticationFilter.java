package multiplayer.server.security;

import com.auth0.jwt.JWT;
import multiplayer.server.model.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static multiplayer.server.security.SecurityConstants.*;

/**
 * JWT Authentication Filter extending Spring Boots Username Password Authentication Filter
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    /**
     * JWT Authentication Filter constructor
     *
     * @param authenticationManager Instance of Spring Boots Authentication Manager
     */
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticate a user based on the user information
     *
     * @param req Instance of Spring Boots HTTP Servlet Request
     * @param res Instance of Spring Boots HTTP Servlet Response
     *
     * @return Authentication Authenticate user if credentials are valid
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res) throws AuthenticationException {
        try {
            ApplicationUser creds = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),creds.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a JWT and sends adds it to the response header
     *
     * @param req Instance of Spring Boots HTTP Servlet Request
     * @param res Instance of Spring Boots HTTP Servlet Response
     * @param chain Instance of Spring Boots Filter Chain
     * @param auth Instance of Spring Boots Authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        String token = JWT.create().withSubject(((User) auth.getPrincipal()).getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}