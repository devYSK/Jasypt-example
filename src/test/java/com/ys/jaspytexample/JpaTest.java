package com.ys.jaspytexample;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ys.jaspytexample.config.JasyptConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest(properties = {"jasypt.encryptor.password=my-secret-password"})
@Import({JasyptConfig.class})
@EnableEncryptableProperties
public class JpaTest {

    @Value("${example.encrypt-value}")
    private String encryptValue;

    @Test
    void printValue() {
        System.out.println(encryptValue);
    }

}
