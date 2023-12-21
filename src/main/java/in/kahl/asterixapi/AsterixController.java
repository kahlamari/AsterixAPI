package in.kahl.asterixapi;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class AsterixController {

    private final CharacterRepo characterRepo;

    public AsterixController(CharacterRepo characterRepo) {
        this.characterRepo = characterRepo;
    }

    @GetMapping("/asterix/characters")
    public List<AsterixCharacter> getCharacters(@RequestParam Optional<String> profession,
                                                @RequestParam Optional<Integer> age,
                                                @RequestParam Optional<String> name) {
        return characterRepo.findAll().stream()
                .filter(character -> profession.map(p -> character.profession().equals(p)).orElse(true))
                .filter(character -> age.map(a -> character.age().equals(a)).orElse(true))
                .filter(character -> name.map(n -> character.name().equals(n)).orElse(true))
                .toList();
    }

    @GetMapping("/asterix/characters/{id}")
    public AsterixCharacter getCharacter(@PathVariable String id) {
        return characterRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("/asterix/characters")
    public AsterixCharacter postCharacter(@RequestBody AsterixCharacter character) {
        return characterRepo.save(character);
    }

    @PutMapping("/asterix/characters/{id}")
    public AsterixCharacter updateProfession(@PathVariable String id, @RequestParam String profession) {
        Optional<AsterixCharacter> character = characterRepo.findById(id);

        if (character.isPresent()) {
            characterRepo.deleteById(id);
            AsterixCharacter c = character.get();
            AsterixCharacter newCharacter = c.withProfession(profession);
            characterRepo.save(newCharacter);
            return newCharacter;
        }
        throw new NoSuchElementException();
    }

    @DeleteMapping("/asterix/characters/{id}")
    public AsterixCharacter deleteCharacter(@PathVariable String id) {
        Optional<AsterixCharacter> character = characterRepo.findById(id);

        if (character.isPresent()) {
            characterRepo.deleteById(id);
            return character.get();
        }
        throw new NoSuchElementException();
    }

    @GetMapping("/asterix/characters/avgage")
    public Double getAverageAge(@RequestParam Optional<String> profession) {
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
