package ru.mephi.finance.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.service.MigrationService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MigrationCliTest {

    @Mock
    private MigrationService migrationService;

    @InjectMocks
    private MigrationCli migrationCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        migrationCli.setScanner(scanner);
    }

    @Test
    void exportDataToFile_ShouldUseCurrentDir_WhenInputIsEmpty() {
        provideInput("\n");

        migrationCli.exportDataToFile();

        verify(migrationService).exportData(argThat(file ->
                file.getName().equals("data.json") && file.getParent().equals(".")
        ));
    }

    @Test
    void exportDataToFile_ShouldUseSpecificPath_WhenProvided() {
        String path = "my/custom/dir";
        provideInput(path + "\n");

        migrationCli.exportDataToFile();

        verify(migrationService).exportData(argThat(file ->
                file.getPath().replace("\\", "/").contains("my/custom/dir/data.json")
        ));
    }

    @Test
    void importDataFromFile_ShouldCallService_WhenConfirmed() {
        provideInput("да\n/backups/my_data.json\n");

        migrationCli.importDataFromFile();

        verify(migrationService).importData(argThat(file ->
                file.getPath().replace("\\", "/").equals("/backups/my_data.json")
        ));
    }

    @Test
    void importDataFromFile_ShouldNotCallService_WhenDenied() {
        provideInput("нет\n");

        migrationCli.importDataFromFile();

        verify(migrationService, never()).importData(any(File.class));
    }

    @Test
    void importDataFromFile_ShouldThrowException_WhenPathIsEmpty() {
        provideInput("да\n \n");

        assertThrows(IllegalArgumentException.class, () -> migrationCli.importDataFromFile());
        verify(migrationService, never()).importData(any());
    }
}
