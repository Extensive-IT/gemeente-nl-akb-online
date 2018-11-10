package gemeente.nlakbonline;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    /**
     * Get current logged-in {@link Authentication}
     * @return
     */
    Authentication getAuthentication();
}
