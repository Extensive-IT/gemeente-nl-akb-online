package gemeente.nlakbonline.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.scope}")
    private String scope;

    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${security.oauth2.client.userInfoUri}")
    private String userInfoUri;

    @Value("${security.oauth2.client.clientAuthenticationScheme}")
    private String clientAuthenticationScheme;

    @Bean
    public AuthorizationCodeResourceDetails authorizationCodeResourceDetails() {
        final AuthorizationCodeResourceDetails result = new AuthorizationCodeResourceDetails();
        result.setAccessTokenUri(this.accessTokenUri);
        result.setClientId(this.clientId);
        result.setClientSecret(this.clientSecret);
        result.setClientAuthenticationScheme(AuthenticationScheme.valueOf(this.clientAuthenticationScheme));
        result.setUserAuthorizationUri(this.userAuthorizationUri);
        result.setScope(Arrays.asList(this.scope.split(",")));
        return result;
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/favicon.ico");
        // Spring Security should completely ignore URLs starting with /resources/
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
