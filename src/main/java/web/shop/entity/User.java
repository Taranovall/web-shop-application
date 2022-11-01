package web.shop.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User {

    private Long id;
    private String login;
    private String firstName;
    private String secondName;
    private byte[] password;
    private String mail;
    private String profilePicturePath;
    private List<Newsletter> newsletterList;
    private String role;

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }

    public User setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public byte[] getPassword() {
        return password;
    }

    public User setPassword(byte[] password) {
        this.password = password;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public User setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public List<Newsletter> getNewsletterList() {
        return newsletterList;
    }

    public User setNewsletterList(List<Newsletter> newsletterList) {
        this.newsletterList = newsletterList;
        return this;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public User setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", password=" + Arrays.toString(password) +
                ", mail='" + mail + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                ", newsletterList=" + newsletterList +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(secondName, user.secondName)
                && Arrays.equals(password, user.password)
                && Objects.equals(mail, user.mail)
                && Objects.equals(profilePicturePath, user.profilePicturePath)
                && Objects.equals(newsletterList, user.newsletterList)
                && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, login, firstName, secondName, mail, profilePicturePath, newsletterList, role);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
