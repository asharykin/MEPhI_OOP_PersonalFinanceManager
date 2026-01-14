package ru.mephi.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MigrationServiceTest {

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EntityService<? extends Entity> migratingService;

    private MigrationService migrationService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        migrationService = new MigrationService(objectMapper, List.of(migratingService));
    }

    @Test
    void exportData_Success() {
        File file = tempDir.resolve("data.json").toFile();

        migrationService.exportData(file);

        assertTrue(file.exists());
        verify(migratingService, times(1)).exportData(any(ObjectNode.class));
    }

    @Test
    void importData_Success() throws IOException {
        File file = tempDir.resolve("data.json").toFile();
        ObjectNode node = objectMapper.createObjectNode();
        objectMapper.writeValue(file, node);

        migrationService.importData(file);

        verify(migratingService, times(1)).deleteAllEntities();
        verify(migratingService, times(1)).importData(any(JsonNode.class));
    }

    @Test
    void importData_FileNotFound_ThrowsException() {
        File nonExistentFile = new File("non_existent.json");

        assertThrows(IllegalArgumentException.class, () -> migrationService.importData(nonExistentFile));
    }

    @Test
    void importData_ParsingError_ThrowsRuntimeException() throws IOException {
        File invalidFile = tempDir.resolve("invalid.json").toFile();
        java.nio.file.Files.writeString(invalidFile.toPath(), "{ invalid json }");

        assertThrows(RuntimeException.class, () -> migrationService.importData(invalidFile));

        // Проверяем, что в случае ошибки вызвано удаление сущностей для очистки
        verify(migratingService, atLeastOnce()).deleteAllEntities();
    }

    @Test
    void exportData_ThrowsExceptionOnIoError() {
        // Создаем файл в месте, где запись невозможна
        File readOnlyFile = new File("/");

        assertThrows(RuntimeException.class, () ->
                migrationService.exportData(readOnlyFile)
        );
    }
}
