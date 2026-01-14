package ru.mephi.finance.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.AuthorizationCli;
import ru.mephi.finance.service.UserService;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorizationCliTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionCli sessionCli;

    @InjectMocks
    private AuthorizationCli authorizationCli;

    private void provideInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        authorizationCli.setScanner(scanner);
    }

    @Test
    void register_ShouldReadTwoLinesAndCallCreateUser() {
        provideInput("JohnDoe\nMySecretPass\n");

        authorizationCli.register();

        verify(userService).createUser("JohnDoe", "MySecretPass");
    }

    @Test
    void register_ShouldFail_WhenFirstInputIsBlank() {
        provideInput("\n");

        assertThrows(IllegalArgumentException.class, () -> authorizationCli.register());

        verify(userService, never()).createUser(anyString(), anyString());
    }
}