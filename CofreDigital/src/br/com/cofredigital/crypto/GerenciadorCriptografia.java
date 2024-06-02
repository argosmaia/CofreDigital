package br.com.cofredigital.crypto;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class GerenciadorCriptografia {

    private static final String ALGORITHM = "AES";

    public byte[] criptografarArquivo(byte[] dadosArquivo, SecretKey chaveSecreta) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cifrador = Cipher.getInstance(ALGORITHM);
        cifrador.init(Cipher.ENCRYPT_MODE, chaveSecreta);
        return cifrador.doFinal(dadosArquivo);
    }

    public byte[] descriptografarArquivo(byte[] dadosCriptografados, SecretKey chaveSecreta) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decifrador = Cipher.getInstance(ALGORITHM);
        decifrador.init(Cipher.DECRYPT_MODE, chaveSecreta);
        return decifrador.doFinal(dadosCriptografados);
    }

    public SecretKey gerarChaveSecreta() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128); 
        return keyGenerator.generateKey();
    }
}