package psw.lab7.lab7.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMessage {
    @Id
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @NotNull
    @ManyToOne
    private Message message;

    @NotNull
    private Boolean red;
}
