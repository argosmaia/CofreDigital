package br.com.cofredigital.crypto;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class Envelope {
    public byte[] criarEnvelope(byte[] chaveSimetrica, PublicKey chavePublica) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.WRAP_MODE, chavePublica);
        return cifrador.wrap(new SecretKeySpec(chaveSimetrica, "AES"));
    }

    public byte[] abrirEnvelope(byte[] envelope, PrivateKey chavePrivada) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decifrador = Cipher.getInstance("RSA");
        decifrador.init(Cipher.UNWRAP_MODE, chavePrivada);
        Key chaveSimetrica = decifrador.unwrap(envelope, "AES", Cipher.SECRET_KEY);
        return chaveSimetrica.getEncoded();
    }
}