package in.kahl.asterixapi;

import lombok.With;

import java.time.Instant;

public record AsterixCharacter(
        String id,
        String name,
        Integer age,
        @With
        String profession,
        Instant createdAt

) {
}
