package in.kahl.asterixapi;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;


import java.util.List;
import java.util.Optional;

@Service
public class AsterixService {
    private final CharacterRepo characterRepo;
    private final IdService idService;
    public AsterixService(CharacterRepo characterRepo, IdService ids) {
        this.characterRepo = characterRepo;
        this.idService = ids;
    }

    public List<AsterixCharacter> getAllCharacters(Optional<Integer> age) {
        if (age.isPresent()) {
            return characterRepo.findAsterixCharactersByAgeAfter(age.get());
        }
        return characterRepo.findAll();
    }

    public AsterixCharacter getCharacter(String id) {
        return characterRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public AsterixCharacter save(AsterixCharacterDTO character) {
        AsterixCharacter asterix = new AsterixCharacter(idService.randomId(),
                character.name(), character.age(), character.profession(),
                Instant.now());
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
