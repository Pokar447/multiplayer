package multiplayer.server.security;

/**
 * Security Constants for authentication via JWT
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 86_400_000; // 24 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}