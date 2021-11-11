package com.performanceengineering.resilientresttemplate;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class SampleHTTPSClient {
    /*
     * public static void main(String[] args) throws IOException,
     * NoSuchAlgorithmException, KeyManagementException {
     * System.out.println("welcome");
     * 
     * // configure the SSLContext with a TrustManager SSLContext ctx =
     * SSLContext.getInstance("TLS"); ctx.init(new KeyManager[0], new TrustManager[]
     * {new DefaultTrustManager()}, new SecureRandom()); SSLContext.setDefault(ctx);
     * 
     * URL url = new URL(
     * "https://dev-edai2-api-op.us.bank-dns.com/cstoreapi/personetics/v1/customer-details?lpidhash=6f5a88d3c4dea789cee08fb29ab53c731cca2cc72b7e742059ab926843011ff3"
     * ); HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
     * conn.setRequestMethod("GET"); conn.setRequestProperty("Content-Type",
     * "application/json"); conn.setRequestProperty("Correlation-ID", "123");
     * 
     * conn.setHostnameVerifier(new HostnameVerifier() {
     * 
     * @Override public boolean verify(String arg0, SSLSession arg1) { return true;
     * } }); System.out.println(conn.getResponseCode());
     * 
     * BufferedReader in = new BufferedReader(new InputStreamReader(
     * conn.getInputStream())); String inputLine; StringBuffer response = new
     * StringBuffer(); while ((inputLine = in .readLine()) != null) {
     * response.append(inputLine); } in .close();
     * 
     * System.out.println(response);
     * 
     * conn.disconnect(); }
     */

    public static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
