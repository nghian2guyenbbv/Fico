package vn.com.tpf.microservices.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;


public class CustomTokenServices extends DefaultTokenServices {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    @Retryable(include = Exception.class, exclude = AuthenticationException.class, backoff = @Backoff(multiplier = 1.1, delay = 500))
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication)
            throws AuthenticationException {
        try {
            return super.createAccessToken(authentication);
        }
        catch (DuplicateKeyException ex) {
//            log.info(String
//                    .format("DuplicateKeyException while creating access token %s", ex));
//            throw ex;

            return super.getAccessToken(authentication);
        }
        catch (Exception ex) {
            log.error("Exception while creating access token", ex);
            throw new RuntimeException(ex);
        }
    }



//    @Autowired
//    private TokenStore tokenStore;
//
//    @Override
//    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) {
//        try {
//            return super.createAccessToken(authentication);
//        }catch (DuplicateKeyException dke) {
//            log.info(String.format("Duplicate user found for %s",authentication.getUserAuthentication().getPrincipal()));
//            return super.getAccessToken(authentication);
//        }catch (Exception ex) {
//            log.info(String.format("Exception while creating access token %s",ex));
//        }
//        return null;
//    }


//    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
//
//    @Override
//    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
//            Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
//        try {
//            if (!(principal instanceof Authentication)) {
//                throw new InsufficientAuthenticationException(
//                        "There is no client authentication. Try adding an appropriate authentication filter.");
//            }
//
//            String clientId = getClientId(principal);
//            ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);
//
//            TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
//
//            if (clientId != null && !clientId.equals("")) {
//                // Only validate the client details if a client authenticated during this
//                // request.
//                if (!clientId.equals(tokenRequest.getClientId())) {
//                    // double check to make sure that the client ID in the token request is the same as that in the
//                    // authenticated client
//                    throw new InvalidClientException("Given client ID does not match authenticated client");
//                }
//            }
//            if (authenticatedClient != null) {
//                oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
//            }
//            if (!StringUtils.hasText(tokenRequest.getGrantType())) {
//                throw new InvalidRequestException("Missing grant type");
//            }
//            if (tokenRequest.getGrantType().equals("implicit")) {
//                throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
//            }
//
//            if (isAuthCodeRequest(parameters)) {
//                // The scope was requested or determined during the authorization step
//                if (!tokenRequest.getScope().isEmpty()) {
//                    logger.debug("Clearing scope of incoming token request");
//                    tokenRequest.setScope(Collections.<String>emptySet());
//                }
//            }
//
//            if (isRefreshTokenRequest(parameters)) {
//                // A refresh token has its own default scopes, so we should ignore any added by the factory here.
//                tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
//            }
//
//            OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
//            if (token == null) {
//                throw new UnsupportedGrantTypeException("Unsupported grant type");
//            }
//
//            return getResponse(token);
//        } catch (DuplicateKeyException dke) {
//            return postAccessToken(principal, parameters);
//        } catch (Exception ex) {
//            log.info(String.format("Exception while creating access token %s", ex));
//            return postAccessToken(principal, parameters);
//        }
//    }
//
//    private boolean isAuthCodeRequest(Map<String, String> parameters) {
//        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
//    }
//    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
//        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
//    }
//
//    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Cache-Control", "no-store");
//        headers.set("Pragma", "no-cache");
//        headers.set("Content-Type", "application/json;charset=UTF-8");
//        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
//    }

}
