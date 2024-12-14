package agh.edu.pl.slpbackend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

    @Autowired
    private BasicTextEncryptor textEncryptor;

    @Override
    public String convertToDatabaseColumn(String password) {
        return textEncryptor.encrypt(password);
    }

    @Override
    public String convertToEntityAttribute(String password) {
        return textEncryptor.decrypt(password);
    }
}
