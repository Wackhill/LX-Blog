package com.shpakovskiy.lxblog.security.registration;

import com.shpakovskiy.lxblog.security.PasswordEncoder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registration_queue")
public class RegistrationQueueItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "requested_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "token")
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RegistrationQueueItem{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", token='" + token + '\'' +
                '}';
    }

    public static class Builder {
        private RegistrationQueueItem newItem;

        public Builder() {
            newItem = new RegistrationQueueItem();
        }

        public Builder firstName(String firstName) {
            newItem.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            newItem.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            newItem.email = email;
            return this;
        }

        public Builder password(String password) {
            newItem.password = new PasswordEncoder().encode(password);
            return this;
        }

        public Builder token(String token) {
            newItem.token = token;
            return this;
        }

        public RegistrationQueueItem build() {
            return newItem;
        }
    }
}
