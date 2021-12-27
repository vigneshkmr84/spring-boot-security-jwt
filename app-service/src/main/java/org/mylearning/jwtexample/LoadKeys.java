package org.mylearning.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
@Slf4j
public class LoadKeys {

    @Value("${private.key.file}")
    private String privateKeyFile;

    @Value("${public.cert.file}")
    private String publicCertFile;


    @Bean
    public PublicKey readPublicKey() throws Exception {
        log.info("Loading Public Key : " + publicCertFile);
        File file = ResourceUtils.getFile(publicCertFile);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String publicKeyPEM = key
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END CERTIFICATE-----", "");

        byte[] certder = Base64.getDecoder().decode(publicKeyPEM);
        InputStream certstream = new ByteArrayInputStream(certder);
        Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
        return cert.getPublicKey();

    }


    @Bean
    public RSAPrivateKey readPrivateKey() throws Exception {
        log.info("Loading Private Key : " + privateKeyFile);
        File file = ResourceUtils.getFile(privateKeyFile);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
