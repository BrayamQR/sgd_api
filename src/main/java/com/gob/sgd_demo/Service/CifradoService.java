package com.gob.sgd_demo.Service;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class CifradoService {
    public String cifrar(String sinCifrar, String key) throws Exception {
        final byte[] bytes = sinCifrar.getBytes("UTF-8");
        final Cipher aes = obtieneCipher(true, key);
        final byte[] cifrado = aes.doFinal(bytes);
        return HexFormat.of().formatHex(cifrado);
    }

    public String descifrar(String cifrado, String key) throws Exception {
        byte[] bcadena = HexFormat.of().parseHex(cifrado);
        final Cipher aes = obtieneCipher(false, key);
        final byte[] bytes = aes.doFinal(bcadena);
        return new String(bytes, "UTF-8");
    }

    private Cipher obtieneCipher(boolean paraCifrar, String keyString) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(keyString.getBytes("UTF-8"));
        final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (paraCifrar) {
            aes.init(Cipher.ENCRYPT_MODE, key);
        } else {
            aes.init(Cipher.DECRYPT_MODE, key);
        }
        return aes;
    }
}
