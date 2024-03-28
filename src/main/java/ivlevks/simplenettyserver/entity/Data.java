package ivlevks.simplenettyserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDateTime time;

    private Integer timeout;

    private String message;

    public Data(Integer timeout, String message) {
        this.timeout = timeout;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
