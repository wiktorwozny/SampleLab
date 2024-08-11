package agh.edu.pl.slpbackend.config;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorConfig {
    @Value("${jasypt.encryptor.password}")
    private String encryptionKey;

    @Bean(name = "textEncryptor")
    public BasicTextEncryptor textEncryptor() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encryptionKey);
        return encryptor;
    }
}
