package ru.mephi.finance.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.Entity;
import ru.mephi.finance.model.User;
import ru.mephi.finance.repository.UserRepository;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService extends EntityService<User> {
    private static User currentUser = null;

    private List<EntityService<? extends Entity>> migratingServices;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Qualifier("migratingService") List<EntityService<? extends Entity>> migratingServices) {
        this.repository = userRepository;
        this.migratingServices = migratingServices;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isUserAuthorized() {
        return currentUser != null;
    }

    @Override
    protected File getJsonDataFile() {
        return dataPath.resolve("users.json").toFile();
    }

    @Override
    @PostConstruct
    public void loadData() { // по умолчанию loadData() без @PostConstruct, это нужно только для UserService
        super.loadData();
    }

    @Override
    public void dumpData() {
        File file = getJsonDataFile();
        writeDataToFile(file);
    }

    public void createUser(String username, String password) {
        checkUsername(username);
        User user = new User();
        user.setId(++index);
        user.setUsername(username);
        user.setPassword(password);
        repository.save(user);
    }

    private void checkUsername(String username) {
        for (User user : getAllEntities()) {
            if (user.getUsername().equals(username)) {
                String message = String.format("Пользователь с именем '%s' уже существует.", username);
                throw new IllegalArgumentException(message);
            }
        }
    }

    public void authorize(String username, String password) {
        for (User user : getAllEntities()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                break;
            }
        }

        if (isUserAuthorized()) {
            migratingServices.forEach(ImportingService::loadData);
        } else {
            throw new NoSuchElementException("Пользователя с такими учётными данными не существует.");
        }
    }

    public void deauthorize() {
        migratingServices.forEach(ExportingService::dumpData);
        currentUser = null;
    }
}
