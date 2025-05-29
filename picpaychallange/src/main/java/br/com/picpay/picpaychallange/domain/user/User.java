package br.com.picpay.picpaychallange.domain.user;

import br.com.picpay.picpaychallange.Dto.UserDTO;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "users")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User (UserDTO data){
        this.firstname = data.firstname();
        this.lastname = data.lastname();
        this.balance = data.balance();
        this.userType = data.userType();
        this.password = data.password();
        this.email = data.email();
        this.document = data.document();
    }

    // Construtor padrão (necessário para JPA)
    public User() {}

    // Construtor com todos os argumentos
    public User(Long id, String firstname, String lastname, String document,
                String email, String password, BigDecimal balance, UserType userType) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.document = document;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.userType = userType;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDocument() {
        return document;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UserType getUserType() {
        return userType;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    // equals e hashCode baseados no id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
