package br.com.cofredigital.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

public class CriptografiaUtils {

    public static byte[] gerarChaveSimetrica() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Chave de 128 bits
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] criptografarConteudo(byte[] conteudo, byte[] chaveSimetrica) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(chaveSimetrica, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(conteudo);
    }

    public static byte[] descriptografarConteudo(byte[] conteudoCriptografado, byte[] chaveSimetrica) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(chaveSimetrica, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(conteudoCriptografado);
    }

    public static byte[] assinarConteudo(byte[] conteudo, PrivateKey chavePrivada) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(chavePrivada);
        signature.update(conteudo);
        return signature.sign();
    }

    public static boolean verificarAssinatura(byte[] conteudo, byte[] assinatura, Certificate certificado) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(certificado);
        signature.update(conteudo);
        return signature.verify(assinatura);
    }

    public static byte[] criarEnvelopeDigital(byte[] chaveSimetrica, Certificate certificado) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, certificado);
        return cipher.doFinal(chaveSimetrica);
    }

    public static byte[] abrirEnvelopeDigital(byte[] envelopeDigital, PrivateKey chavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, chavePrivada);
        return cipher.doFinal(envelopeDigital);
    }
}
