package com.molkky.molkky.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Entity
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pseudo")
    private String pseudo;

    @Column(name = "surname")
    private String surname;

    @Column(name = "forename")
    private String forename;

    @Column(name = "club")
    private String club;

    @Column(name = "email")
    private String email;

    @Column(name = "isRegistered")
    private Boolean isRegistered;

    @OneToMany
    @JoinColumn(name="idUser", nullable = true)
    private Set<Notification> notifications;

    @ManyToMany
    @JoinTable(name = "team_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams;

    public User(String pseudo, String surname, String forename, String club, String email, Boolean isRegistered) {
        this.pseudo = pseudo;
        this.surname = surname;
        this.forename = forename;
        this.club = club;
        this.email = email;
        this.isRegistered = isRegistered;
    }

    public User() {

    }
}