package br.com.cofredigital.crypto;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AssinaturaDigital {

    public byte[] assinarDados(byte[] dados, PrivateKey chavePrivada) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature assinatura = Signature.getInstance("SHA1withRSA");
        assinatura.initSign(chavePrivada);
        assinatura.update(dados);
        return assinatura.sign();
    }

    public boolean verificarAssinatura(byte[] dados, byte[] assinatura, PublicKey chavePublica) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(chavePublica);
        sig.update(dados);
        return sig.verify(assinatura);
    }

    public static PrivateKey obterChavePrivada(String chave) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteChave = Base64.getDecoder().decode(chave);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteChave);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey obterChavePublica(String chave) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteChave = Base64.getDecoder().decode(chave);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteChave);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
