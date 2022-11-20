package de.storagesystem.api.cryptography;


/**
 * @author Simon Brebeck
 */
public interface FileCryptographer {

    /**
     * Encryptes a file and stores it at path
     *
     * @param path The path to store the encrypted file
     * @param contentBytes The bytes to encrypt
     */
    void encryptFile(String path, byte[] contentBytes);

    /**
     * Decryptes a file and returns the decrypted bytes
     *
     * @param path The path to the file to decrypt
     * @return The decrypted bytes
     */
    byte[] decryptFile(String path);
}
