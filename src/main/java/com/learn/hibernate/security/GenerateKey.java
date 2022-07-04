package com.learn.hibernate.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Date;

public class GenerateKey {
    public static Key getSigningKeyBytes(){
        String jksPassword = "1234test1234";

        KeyStore ks  = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            ks.load(new FileInputStream("F:\\fanap-projects\\maven\\hibernate1\\hanbotest.jks"), jksPassword.toCharArray());
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Key key = null;
        try {
            key = ks.getKey("hanbotest", jksPassword.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return key;
    }
    public static PublicKey loadPublicKey(){
        PublicKey retValue  = null;
        try{
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new FileInputStream("F:\\fanap-projects\\maven\\hibernate1\\hanbotest.cer"));
            retValue = cert.getPublicKey();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retValue;
    }
//    public static Date getExpiration(){
//        Date date =  new Date(System.currentTimeMillis() + 36000)
//    }
}
