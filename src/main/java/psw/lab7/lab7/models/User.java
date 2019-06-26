package psw.lab7.lab7.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import psw.lab7.lab7.UserENUM;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private UserENUM type;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private LocalDate lastActivity;



}
