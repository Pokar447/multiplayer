package multiplayer.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static multiplayer.server.security.SecurityConstants.*;

/**
 * JWT Authorization Filter extending Spring Boots Base Authentication Filter
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    /**
     * JWT Authorization Filter constructor
     *
     * @param authManager Instance of Spring Boots Authentication Manager
     */
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    /**
     * Extends Spring Boots Basic Authentication Filter with custom filters
     *
     * @param req Instance of Spring Boots HTTP Servlet Request
     * @param res Instance of Spring Boots HTTP Servlet Response
     * @param chain Instance of Spring Boots Filter Chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * Generates a token based on the user name and password
     *
     * @param request Instance of Spring Boots HTTP Servlet Request
     *
     * @return UsernamePasswordAuthenticationToken Generated Token by user name and password
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, "")).getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}