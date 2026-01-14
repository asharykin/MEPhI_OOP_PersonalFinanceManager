package ru.mephi.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.mephi.finance.model.Entity;
import ru.mephi.finance.repository.EntityRepository;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.Collection;

public abstract class EntityService<E extends Entity> implements ImportingService, ExportingService {
    protected Integer index = 0;
    protected Class<E> entityClass;
    protected EntityRepository<E> repository;
    protected ObjectMapper objectMapper;

    @Value("${app.data-path:data/}")
    protected Path dataPath;
    protected String typeStr;
    protected String subtypeStr;

    protected EntityService() {
        // Для определения класса сущности при десериализации. По-другому не получится.
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) superclass.getActualTypeArguments()[0];
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Collection<E> getAllEntities() {
        return repository.findAll();
    }

    public void deleteAllEntities() {
        repository.deleteAll();
    }

    protected String getJsonFieldName() {
        return String.format("%s_%s", typeStr, subtypeStr);
    }

    protected File getJsonDataFile() {
        String fileName = UserService.getCurrentUser().getId() + ".json";
        return dataPath.resolve(typeStr).resolve(subtypeStr).resolve(fileName).toFile();
    }

    public void loadData() {
        File file = getJsonDataFile();
        if (file.exists()) {
            importData(readDataFromFile(file));
        }
    }

    public JsonNode readDataFromFile(File file) {
        try {
            return objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importData(JsonNode node) {
        ArrayNode arrayNode = (ArrayNode) (node.isArray() ? node : node.get(getJsonFieldName()));
        for (JsonNode entityNode : arrayNode) {
            E entity = objectMapper.convertValue(entityNode, entityClass);
            processEntityBeforeImport(entity);
            repository.save(entity);
        }
        index = repository.getLastId();
    }

    protected void processEntityBeforeImport(E entity) {
    }

    @PreDestroy // чтобы при ручной остановке приложения сессионные данные тоже сохранялись в файл
    public void dumpData() {
        if (UserService.isUserAuthorized()) {
            File file = getJsonDataFile();
            writeDataToFile(file);
        }
        deleteAllEntities();
    }

    public void writeDataToFile(File file) {
        try {
            file.getParentFile().mkdirs(); // для создания нужных директорий, если их нет
            objectMapper.writeValue(file, getAllEntities());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportData(ObjectNode node) {
        ArrayNode arrayNode = objectMapper.valueToTree(getAllEntities());
        node.set(getJsonFieldName(), arrayNode);
    }
}
