package ru.mephi.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.Entity;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MigrationService {
    private ObjectMapper objectMapper;
    private List<EntityService<? extends Entity>> migratingServices;

    @Autowired
    public MigrationService(ObjectMapper objectMapper,
                            @Qualifier("migratingService") List<EntityService<? extends Entity>> migratingServices) {
        this.objectMapper = objectMapper;
        this.migratingServices = migratingServices;
    }

    public void exportData(File file) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            migratingServices.forEach(service -> service.exportData(node));
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importData(File file) {
        if (file.exists()) {
            try {
                JsonNode node = objectMapper.readTree(file);
                migratingServices.forEach(service -> {
                    service.deleteAllEntities();
                    service.importData(node);
                });
            } catch (IOException e) {
                migratingServices.forEach(EntityService::deleteAllEntities);
                String message = "Ошибка во время парсинга файла с данными. Убедитесь, что его структура корректна.";
                throw new RuntimeException(message, e);
            }
        } else {
            throw new IllegalArgumentException("По данному пути файла не обнаружено.");
        }
    }
}
