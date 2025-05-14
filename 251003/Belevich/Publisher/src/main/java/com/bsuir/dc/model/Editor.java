package com.bsuir.dc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_editor")
public class Editor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setLogin(String login) { this.login = login; }
    public String getLogin() { return login; }

    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }

    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getFirstname() { return firstname; }

    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getLastname() { return lastname; }
}
