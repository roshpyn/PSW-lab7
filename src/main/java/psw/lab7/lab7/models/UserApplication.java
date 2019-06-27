package psw.lab7.lab7.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserApplication {
    @Id
    private Long id;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Course course;
}
