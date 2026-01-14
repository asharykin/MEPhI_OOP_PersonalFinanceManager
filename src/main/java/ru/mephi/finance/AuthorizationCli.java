package ru.mephi.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.mephi.finance.cli.BaseCli;
import ru.mephi.finance.cli.SessionCli;
import ru.mephi.finance.service.UserService;

@SpringBootApplication
public class AuthorizationCli extends BaseCli {
    private UserService userService;
    private SessionCli sessionCli;

    @Autowired
    public AuthorizationCli(UserService userService, SessionCli sessionCli) {
        this.userService = userService;
        this.sessionCli = sessionCli;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AuthorizationCli.class, args);
        context.getBean(AuthorizationCli.class).run();
    }

    public void run() {
        boolean running = true;

        while (running) {
            try {
                printMenu();
                int choice = readIntInput();
                System.out.println();
                switch (choice) {
                    case 1 -> printHelp();
                    case 2 -> register();
                    case 3 -> {
                        logIn();
                        sessionCli.manageSession();
                    }
                    case 0 -> running = false;
                    default -> System.out.println("Недопустимый выбор. Введите номер одного из пунктов списка.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void printMenu() {
        System.out.println("\n===== ВХОД =====");
        System.out.println("1.  Помощь");
        System.out.println("2.  Зарегистрироваться");
        System.out.println("3.  Войти");
        System.out.println("0.  Завершить работу");
        System.out.print("Введите ваш выбор: ");
    }

    @Override
    protected void printHelp() {
        System.out.println("2.  Здесь вы можете создать новую учётную запись, указав имя пользователя и пароль. " +
                           "Учтите, что имя не должно совпадать с именами уже существующих пользователей.");
        System.out.println("3.  Здесь вы можете войти в созданную ранее учётную запись и начать полноценную работу " +
                           "с Системой управления личными финансами.");
        System.out.println("0.  Здесь вы можете завершить работу с сохранением данных о новых пользователях на диск.");
    }

    public void register() {
        String username = getUsername();
        String password = getPassword();

        userService.createUser(username, password);
    }

    public void logIn() {
        String username = getUsername();
        String password = getPassword();

        userService.authorize(username, password);
    }

    private String getUsername() {
        System.out.print("Введите имя пользователя: ");
        return readNonEmptyStringInput("Имя пользователя");
    }

    private String getPassword() {
        System.out.print("Введите пароль: ");
        return readNonEmptyStringInput("Пароль");
    }
}
