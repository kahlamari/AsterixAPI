package in.kahl.asterixapi;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepo extends MongoRepository<AsterixCharacter, String> {

    //List<AsterixCharacter> findAllByAgeEquals;
}
