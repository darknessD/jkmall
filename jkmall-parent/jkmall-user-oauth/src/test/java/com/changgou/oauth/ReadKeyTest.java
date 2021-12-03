package com.changgou.oauth;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class ReadKeyTest {

    @Test
    public void readKey() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        String jksPath = "E:\\IdeaWorkspace\\jkmall\\jkmall-parent\\jkmall-user-oauth\\src\\main\\resources\\jkmall.jks"; //jks file path
        String jksPassword = "jkmall"; // jks keyStore password
        String certAlias = "jkmall"; // cert alias
        String certPassword = "jkmall"; // cert password
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(jksPath), jksPassword.toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(certAlias, certPassword.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(certAlias).getPublicKey();
        System.out.println(privateKey.getEncoded());
        System.out.println(publicKey.getEncoded());
    }
}
