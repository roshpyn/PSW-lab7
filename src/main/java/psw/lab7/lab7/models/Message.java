package psw.lab7.lab7.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    private Long id;

    @NotNull
    private Long lessonId;

    @NotNull
    private String subject;

    @NotNull
    private String body;


}
