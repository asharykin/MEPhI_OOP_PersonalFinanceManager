package ru.mephi.finance.config;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Separators seps = Separators.createDefaultInstance().withObjectFieldValueSpacing(Separators.Spacing.AFTER);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter(seps);
        printer.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultPrettyPrinter(printer);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
        return objectMapper;
    }
}
