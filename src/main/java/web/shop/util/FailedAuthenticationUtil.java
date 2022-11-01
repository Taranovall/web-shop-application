package web.shop.util;

import web.shop.dto.UserDTO;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FailedAuthenticationUtil {

    private final Integer maxAuthAttempts;
    private final Integer blockTimeDurationInMinutes;
    private List<UserDTO> failedAuthenticationUsers;

    public FailedAuthenticationUtil() {
        this.failedAuthenticationUsers = new LinkedList<>();
        this.maxAuthAttempts = 3;
        this.blockTimeDurationInMinutes = 10;
    }

    public FailedAuthenticationUtil(Integer maxAuthAttempt, Integer blockTimeDurationInMinutes) {
        this.failedAuthenticationUsers = new LinkedList<>();
        this.maxAuthAttempts = maxAuthAttempt;
        this.blockTimeDurationInMinutes = blockTimeDurationInMinutes;
    }

    public FailedAuthenticationUtil(List<UserDTO> failedAuthenticationUsers, Integer maxAuthAttempt, Integer blockTimeDurationInMinutes) {
        this.failedAuthenticationUsers = failedAuthenticationUsers;
        this.maxAuthAttempts = maxAuthAttempt;
        this.blockTimeDurationInMinutes = blockTimeDurationInMinutes;
    }

    public void handleFailedAuthentication(String login) {
        Optional<UserDTO> userDTO = getUserDTO(login);
        userDTO.ifPresentOrElse(this::handleExistingFailedAuthenticationUser,
                () -> handleNonExistingFailedAuthenticationUser(login));
    }

    private void handleNonExistingFailedAuthenticationUser(String login) {
        UserDTO user = new UserDTO()
                .setLogin(login)
                .setFailedAuthenticationAttemptCount(1);
        failedAuthenticationUsers.add(user);
    }

    private void handleExistingFailedAuthenticationUser(UserDTO user) {
        if (user.getFailedAuthenticationAttemptCount() >= maxAuthAttempts) {
            user.setFailedAuthenticationAttemptCount(0);
            user.setUnblockingTime(LocalDateTime.now().plusMinutes(blockTimeDurationInMinutes));
        } else {
            Integer newFailedAttemptsCount = user.getFailedAuthenticationAttemptCount() + 1;
            user.setFailedAuthenticationAttemptCount(newFailedAttemptsCount);
        }
    }

    public boolean isUserBanned(String login) {
        Optional<UserDTO> user = getUserDTO(login);

        return user.isPresent() && LocalDateTime.now().isBefore(user.get().getUnblockingTime());
    }

    private Optional<UserDTO> getUserDTO(String login) {
        return failedAuthenticationUsers.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }

    public void authenticate(String login) {
        failedAuthenticationUsers.removeIf(u -> u.getLogin().equals(login));
    }

    public String getMinutesRemainingToUnban(String login) {
        UserDTO user = getUserDTO(login).get();
        Long timeRemainingToUnbanInMillis = Duration.between(LocalDateTime.now(), user.getUnblockingTime()).toMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(timeRemainingToUnbanInMillis);
    }

    public void setFailedAuthenticationUsers(List<UserDTO> failedAuthenticationUsers) {
        this.failedAuthenticationUsers = failedAuthenticationUsers;
    }
}
