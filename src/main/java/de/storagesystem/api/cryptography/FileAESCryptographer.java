package de.storagesystem.api.cryptography;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Simon Brebeck
 */
public class FileAESCryptographer implements FileCryptographer {

    /**
     * An {@link SecretKey} instance used for encryption and decryption.
     */
    private final SecretKey secretKey;
    /**
     * An {@link Cipher} algorithm instance to encrypt and decrypt files.
     */
    private final Cipher cipher;

    public FileAESCryptographer(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encryptFile(String path, byte[] contentBytes) {
        try(FileOutputStream fs = new FileOutputStream(path);
            CipherOutputStream cos = new CipherOutputStream(fs, cipher)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            fs.write(iv);
            cos.write(contentBytes);
        } catch (InvalidKeyException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public byte[] decryptFile(String path) {
        byte[] contentBytes;
        try(FileInputStream input = new FileInputStream(path)) {
            byte[] fileIv = new byte[16];
            input.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
            try(CipherInputStream cipherIn = new CipherInputStream(input, cipher);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                int length;
                byte[] buffer = new byte[4096];
                while((length = cipherIn.read(buffer, 0, buffer.length)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                contentBytes = outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        return contentBytes;
    }
}
