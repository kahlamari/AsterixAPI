package in.kahl.asterixapi;

import lombok.With;

public record AsterixCharacter(
        String _id,
        String name,
        Integer age,
        @With
        String profession

) {
}
