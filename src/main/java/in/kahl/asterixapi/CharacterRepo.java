package in.kahl.asterixapi;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CharacterRepo extends MongoRepository<AsterixCharacter, String> {

    List<AsterixCharacter> findAsterixCharactersByAgeAfter(Integer age);
}
