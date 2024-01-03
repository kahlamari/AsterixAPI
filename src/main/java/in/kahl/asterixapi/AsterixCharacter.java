package in.kahl.asterixapi;

import lombok.With;
import org.springframework.data.annotation.Id;

public record AsterixCharacter(
        String id,
        String name,
        Integer age,
        @With
        String profession

) {
}
