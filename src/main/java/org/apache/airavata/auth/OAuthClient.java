package org.apache.airavata.auth;


import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.codehaus.jackson.map.ObjectMapper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class OAuthClient {

    private String hostName;
    private String clientId;
    private String clientSecret;

    public OAuthClient(String hostName){
        this.hostName = hostName;
    }

    public OAuthClient(String hostName,String clientId,String clientSecret){
        this.hostName = hostName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public AuthResponse authenticate(String username,String password) throws OAuthAuthorisationException {
        registerSSL();
        try {
            OAuthClientRequest request = OAuthClientRequest.tokenLocation(hostName).
                    setClientId(clientId).setClientSecret(clientSecret).
                    setGrantType(GrantType.PASSWORD).
                    setRedirectURI("http://example.com").
                    setUsername(username).
                    setPassword(password).
                    setScope("openid").
                    buildBodyMessage();


            URLConnectionClient ucc = new URLConnectionClient();

            org.apache.oltu.oauth2.client.OAuthClient oAuthClient = new org.apache.oltu.oauth2.client.OAuthClient(ucc);
            OAuthResourceResponse resp = oAuthClient.resource(request, OAuth.HttpMethod.POST, OAuthResourceResponse.class);
            System.out.println(resp.getBody());
            //converting JSON to object
            ObjectMapper mapper = new ObjectMapper();
            AuthResponse authResponse = mapper.readValue(resp.getBody(), AuthResponse.class);

            System.out.println(authResponse.getAccess_token());
            return authResponse;
        }catch (Exception ex){
            throw new OAuthAuthorisationException(ex);
        }
    }



    static {
        //for localhost testing only
        //registerSSL();

    }

    public static class AuthResponse{
        private String token_type;
        private int expires_in;
        private String refresh_token;
        private String access_token;
        public String id_token;
        private String scope;

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getId_token() {
            return id_token;
        }

        public void setId_token(String id_token) {
            this.id_token = id_token;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    public static void registerSSL(){
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier(){

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
