package in.kahl.asterixapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asterix")
public class AsterixController {

    private final AsterixService service;

    public AsterixController(AsterixService service) {
        this.service = service;
    }

    @GetMapping("/characters")
    public List<AsterixCharacter> getAllCharacters(@RequestParam Optional<Integer> age) {
        return service.getAllCharacters(age);
    }

    @GetMapping("/characters/{id}")
    public AsterixCharacter getCharacter(@PathVariable String id) {
        return service.getCharacter(id);
    }

    @PostMapping("/characters")
    @ResponseStatus(HttpStatus.CREATED)
    public AsterixCharacter postCharacter(@RequestBody AsterixCharacterDTO character) {
        return service.save(character);
    }

    @PutMapping("/characters/{id}")
    public AsterixCharacter updateProfession(@PathVariable String id, @RequestParam String profession) {
        return service.updateProfession(id, profession);
    }

    @DeleteMapping("/characters/{id}")
    public AsterixCharacter deleteCharacter(@PathVariable String id) {
        return service.delete(id);
    }

    @GetMapping("/characters/avgage")
    public Double getAverageAge(@RequestParam Optional<String> profession) {
        return service.getAverageAge(profession);
    }
}
