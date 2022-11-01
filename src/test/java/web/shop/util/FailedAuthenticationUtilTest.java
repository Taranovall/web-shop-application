package web.shop.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FailedAuthenticationUtilTest {

    private static final int MAX_AUTH_ATTEMPT = 2;
    private static final int BLOCK_TIME_DURATION_IN_MINUTES = 10;
    private final FailedAuthenticationUtil failedAuthenticationUtil = new FailedAuthenticationUtil(MAX_AUTH_ATTEMPT, BLOCK_TIME_DURATION_IN_MINUTES);

    @Test
    void shouldHandleFirstAuthAttempt() {
        List<UserDTO> actualList = new LinkedList<>();
        this.failedAuthenticationUtil.setFailedAuthenticationUsers(actualList);

        String login = "First failed attempt";
        failedAuthenticationUtil.handleFailedAuthentication(login);

        List<UserDTO> expectedList = List.of(new UserDTO()
                .setLogin(login)
                .setFailedAuthenticationAttemptCount(1)
        );

        assertEquals(expectedList, actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void shouldHandleSecondAuthAttempt() {
        String login = "Second failed attempt";
        UserDTO user = new UserDTO()
                .setLogin(login)
                .setFailedAuthenticationAttemptCount(1);
        List<UserDTO> list = Collections.singletonList(user);

        this.failedAuthenticationUtil.setFailedAuthenticationUsers(list);
        failedAuthenticationUtil.handleFailedAuthentication(login);

        int expectedFailedAttemptCount = 2;
        int actualFailedAttemptCount = user.getFailedAuthenticationAttemptCount();

        assertEquals(expectedFailedAttemptCount, actualFailedAttemptCount);
    }

    @Test
    void shouldBanUserAfterExceedingFailedAuthAttemptCount() {
        String login = "to be banned";
        UserDTO user = new UserDTO()
                .setLogin(login)
                .setFailedAuthenticationAttemptCount(MAX_AUTH_ATTEMPT);
        List<UserDTO> actualList = List.of(
                user,
                new UserDTO()
                        .setLogin("test")
                        .setFailedAuthenticationAttemptCount(1)
        );
        this.failedAuthenticationUtil.setFailedAuthenticationUsers(actualList);

        assertFalse(failedAuthenticationUtil.isUserBanned(login));

        LocalDateTime unblockingTimeBeforeBan = user.getUnblockingTime();
        failedAuthenticationUtil.handleFailedAuthentication(login);
        LocalDateTime unblockingTimeAfterBan = user.getUnblockingTime();

        assertNotEquals(unblockingTimeBeforeBan, unblockingTimeAfterBan);

        int expectedFailedAuthAttempt = 0;
        int actualFailedAuthAttempt = user.getFailedAuthenticationAttemptCount();

        assertEquals(expectedFailedAuthAttempt, actualFailedAuthAttempt);
        assertTrue(failedAuthenticationUtil.isUserBanned(login));

        String minutesRemainingToUnban = failedAuthenticationUtil.getMinutesRemainingToUnban(login);
        boolean doesTimeMatchFormat = minutesRemainingToUnban.matches("^09:\\d{2}$");
        assertTrue(doesTimeMatchFormat);
    }
}