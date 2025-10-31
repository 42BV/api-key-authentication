package nl._42.apikeyauthentication.autoconfigure.authentication;

/**
 * Security principal that holds the supplied Api Key.
 * @param apiKey Api Key
 */
public record ApiKeyPrincipal(String apiKey) {

}
