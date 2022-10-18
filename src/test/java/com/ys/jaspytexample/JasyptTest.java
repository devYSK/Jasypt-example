package com.ys.jaspytexample;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptTest {

    private String encryptKey = "my-secret-password"; // 키 값

    @Test
    void encryptTest() {
        String plainText = "plainText"; // 암호화할 평문

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(encryptKey); // 암호화할 키 값 파라미터로 전달
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘

        String encryptText = jasypt.encrypt(plainText); // 암호화

        System.out.printf("encryptText : %s", encryptText); // 암호화한 문자열 출력
    }
}
