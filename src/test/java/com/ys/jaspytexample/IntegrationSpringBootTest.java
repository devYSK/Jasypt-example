package com.ys.jaspytexample;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@EnableEncryptableProperties
@SpringBootTest(properties = {"jasypt.encryptor.password=my-secret-password"})
public class IntegrationSpringBootTest {


    @Value("${example.encrypt-value}")
    private String encryptValue;

    @Test
    void printValue() {
        System.out.println(encryptValue);
    }

}
