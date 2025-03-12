package demo.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "passenger")
public class Passenger extends BaseEntity{
    private String name;
    private String surname;
    private String  patronymic;
    private LocalDate birthday;
    private String identificationDocument;

    private String telephone;
    private String email;
    private String login;
    private String password;

   private List<Role> roles;



    private Set<Ticket> tickets;

    public Passenger(String name, String surname, String patronymic, LocalDate birthday, String identificationDocument, String telephone, String email, String login, String password, Set<Ticket> tickets) {
        this();

        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.identificationDocument = identificationDocument;
        this.telephone = telephone;
        this.email = email;
        this.login = login;
        this.password = password;
        this.tickets = tickets;
    }

    public Passenger(String name, String surname, String patronymic, String email, String login, String password) {
        this();
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = null;
        this.identificationDocument = null;
        this.telephone = null;
        this.email = email;
        this.login = login;
        this.password = password;
        this.tickets = null;
    }

    protected Passenger(){
        this.roles = new ArrayList<>();
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "surname", nullable = false)
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Column(name = "patronymic", nullable = true)
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    @Column(name = "birthday", nullable = true)
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }


    @Column(name = "identification_document", length = 10, unique = true)
    public String getIdentificationDocument() {
        return identificationDocument;
    }
    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    @Column(name = "telephone", length = 12, unique = true)
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "login", unique = true, nullable = false)
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }







    @ManyToMany(fetch = FetchType.EAGER)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }






    @OneToMany(mappedBy = "passenger", targetEntity = Ticket.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }



}
