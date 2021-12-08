package nl._42.apikeyauthentication.autoconfigure.authentication;

public class ApiKeyPrincipal {

    private final String apiKey;

    public ApiKeyPrincipal(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
