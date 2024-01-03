package in.kahl.asterixapi;

import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AsterixService {
    private final CharacterRepo characterRepo;
    public AsterixService(CharacterRepo characterRepo) {
        this.characterRepo = characterRepo;
    }

    public List<AsterixCharacter> getCharacters(Optional<String> profession, Optional<Integer> age, Optional<String> name) {
        return characterRepo.findAll().stream()
                .filter(character -> profession.map(p -> character.profession().equals(p)).orElse(true))
                .filter(character -> age.map(a -> character.age().equals(a)).orElse(true))
                .filter(character -> name.map(n -> character.name().equals(n)).orElse(true))
                .toList();
    }

    public AsterixCharacter getCharacter(String id) {
        return characterRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public AsterixCharacter save(AsterixCharacterDTO character) {
        AsterixCharacter asterix = new AsterixCharacter(UUID.randomUUID().toString(),
                character.name(), character.age(), character.profession());
        return characterRepo.save(asterix);
    }

    public AsterixCharacter updateProfession(String id, String profession) {
        return characterRepo.findById(id)
                .map(character -> characterRepo.save(character.withProfession(profession)))
                .orElseThrow(NoSuchElementException::new);
    }

    public AsterixCharacter delete(String id) {
        Optional<AsterixCharacter> character = characterRepo.findById(id);

        if (character.isPresent()) {
            characterRepo.deleteById(id);
            return character.get();
        }
        throw new NoSuchElementException();
    }

    public Double getAverageAge(Optional<String> profession) {
        List<AsterixCharacter> characters = characterRepo.findAll();
        if (profession.isPresent()) {
            characters = characters.stream()
                    .filter(character -> character.profession().equals(profession.get()))
                    .toList();
        }
        return characters.stream()
                .mapToInt(AsterixCharacter::age)
                .average()
                .orElse(0.0);
    }
}
