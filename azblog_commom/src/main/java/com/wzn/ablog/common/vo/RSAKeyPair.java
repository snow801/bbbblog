package com.wzn.ablog.common.vo;

/**
 * RSA密钥对对象
 */
public class RSAKeyPair {

    private String publicKey;
    private String privateKey;

    public RSAKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "RSAKeyPair{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}