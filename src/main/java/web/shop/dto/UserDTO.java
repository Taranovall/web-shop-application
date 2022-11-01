package web.shop.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO {

    private String login;
    private LocalDateTime unblockingTime;
    private Integer failedAuthenticationAttemptCount;

    public UserDTO() {
        this.unblockingTime = LocalDateTime.MIN;
    }

    public String getLogin() {
        return login;
    }

    public UserDTO setLogin(String login) {
        this.login = login;
        return this;
    }

    public LocalDateTime getUnblockingTime() {
        return unblockingTime;
    }

    public UserDTO setUnblockingTime(LocalDateTime unblockingTime) {
        this.unblockingTime = unblockingTime;
        return this;
    }

    public Integer getFailedAuthenticationAttemptCount() {
        return failedAuthenticationAttemptCount;
    }

    public UserDTO setFailedAuthenticationAttemptCount(Integer failedAuthenticationAttemptCount) {
        this.failedAuthenticationAttemptCount = failedAuthenticationAttemptCount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(login, userDTO.login) && Objects.equals(unblockingTime, userDTO.unblockingTime) && Objects.equals(failedAuthenticationAttemptCount, userDTO.failedAuthenticationAttemptCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, unblockingTime, failedAuthenticationAttemptCount);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", unblockingTime=" + unblockingTime +
                ", failedAuthenticationAttemptCount=" + failedAuthenticationAttemptCount +
                '}';
    }
}
