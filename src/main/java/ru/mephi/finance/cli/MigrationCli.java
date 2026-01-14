package ru.mephi.finance.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.finance.service.MigrationService;

import java.io.File;

@Service
public class MigrationCli extends BaseCli {

    @Autowired
    private MigrationService migrationService;

    public void manageMigration() {
        boolean managingMigration = true;

        while (managingMigration) {
            try {
                printMenu();
                int choice = readIntInput();
                System.out.println();
                switch (choice) {
                    case 1 -> printHelp();
                    case 2 -> exportDataToFile();
                    case 3 -> importDataFromFile();
                    case 0 -> managingMigration = false;
                    default -> System.out.println("Недопустимый выбор. Введите номер одного из пунктов списка.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void printMenu() {
        System.out.println("\n===== ЭКСПОРТ/ИМПОРТ =====");
        System.out.println("1.  Помощь");
        System.out.println("2.  Экспорт данных");
        System.out.println("3.  Импорт данных");
        System.out.println("0.  Вернуться в главное меню");
        System.out.print("Введите ваш выбор: ");
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете экспортировать данные пользователя о категориях и операциях в файл.");
        System.out.println("3.  Здесь вы можете импортировать данные о категориях и операциях из файла.");
        System.out.println("0.  Здесь вы можете вернуться в главное меню.");
    }

    public void exportDataToFile() {
        System.out.println("Далее вам нужно будет указать путь (относительный или абсолютный) до директории, " +
                           "где будет создан файл 'data.json' с экспортированными данными.");
        System.out.println("Если указанной директории не существует, то по возможности она будет создана. " +
                           "Если вы оставите ввод пустым, то файл будет создан в текущей директории.");

        System.out.print("Введите путь: ");
        String dirPath = scanner.nextLine().trim();
        dirPath = dirPath.isBlank() ? "." : dirPath;
        File file = new File(dirPath,"data.json");

        migrationService.exportData(file);
        System.out.println("Данные успешно экспортированы.");
    }

    public void importDataFromFile() {
        System.out.println("ВНИМАНИЕ: Импорт приведёт к перезаписи уже имеющихся у пользователя данных!");
        System.out.print("Уверены ли вы, что хотите продолжить (да/нет): ");

        if (readPositiveReply()) {
            System.out.println();
            System.out.println("Далее вам нужно будет указать путь (относительный или абсолютный) до файла с " +
                    "подготовленными для импорта данными в формате json.");
            System.out.println("Во избежание ошибок настоятельно рекомендуется использовать только файлы, " +
                    "созданные этим приложением в результате экспорта данных.");

            System.out.print("Введите путь: ");
            String filePath = readNonEmptyStringInput("Путь к файлу");
            File file = new File(filePath);

            migrationService.importData(file);
            System.out.println("Данные успешно импортированы.");
        }
    }
}
