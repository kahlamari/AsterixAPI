package in.kahl.asterixapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsterixServiceTest {
    private final CharacterRepo repo = mock(CharacterRepo.class);
    private final IdService idService = mock(IdService.class);
    @Test
    void getAllCharactersTest_whenNoAge_thenAllCharacters() {
        // GIVEN
        List<AsterixCharacter> testdata = List.of(
                new AsterixCharacter("1", "Asterix", 35, "Krieger", Instant.now()),
                new AsterixCharacter("2", "Obelix", 45, "Lieferant", Instant.now())
        );
        when(repo.findAll()).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        List<AsterixCharacter> actual = underTest.getAllCharacters(Optional.empty());

        // THEN
        assertEquals(testdata, actual);
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getAllCharactersTest_whenAgeLT36_thenAllCharacters() {
        // GIVEN
        List<AsterixCharacter> testdata = List.of(
                new AsterixCharacter("2", "Obelix", 45, "Lieferant", Instant.now())
        );
        when(repo.findAsterixCharactersByAgeAfter(36)).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        List<AsterixCharacter> actual = underTest.getAllCharacters(Optional.of(36));

        // THEN
        assertEquals(testdata, actual);
        verify(repo).findAsterixCharactersByAgeAfter(36);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void deleteTest_whenCharacterExists_thenDelete() {
        // GIVEN
        Optional<AsterixCharacter> testdata = Optional.of(new AsterixCharacter("2", "Obelix", 45, "Lieferant", Instant.now()));
        when(repo.findById("2")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        AsterixCharacter actual = underTest.delete("2");

        // THEN
        assertEquals(testdata.get(), actual);
        verify(repo).findById("2");
        verify(repo).deleteById("2");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void deleteTest_whenCharacterNotExists_thenException() {
        // GIVEN
        Optional<AsterixCharacter> testdata = Optional.empty();
        when(repo.findById("2")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        assertThrows(NoSuchElementException.class, () ->

        // THEN
                underTest.delete("2"));

        verify(repo).findById("2");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getAverageAgeTest_whenNoProfessionFilter_whenAverageAge40() {
        // GIVEN
        List<AsterixCharacter> testdata = List.of(
                new AsterixCharacter("1", "Asterix", 35, "Krieger", Instant.now()),
                new AsterixCharacter("2", "Obelix", 45, "Lieferant", Instant.now())
        );
        when(repo.findAll()).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        Double actual = underTest.getAverageAge(Optional.empty());

        // THEN
        assertEquals(40.0, actual);
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getAverageAgeTest_whenProfessionKrieger_whenAverageAg35() {
        // GIVEN
        List<AsterixCharacter> testdata = List.of(
                new AsterixCharacter("1", "Asterix", 35, "Krieger", Instant.now()),
                new AsterixCharacter("2", "Obelix", 45, "Lieferant", Instant.now())
        );
        when(repo.findAll()).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        Double actual = underTest.getAverageAge(Optional.of("Krieger"));

        // THEN
        assertEquals(35.0, actual);
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void updateProfessionTest_whenProfessionKrieger_whenProfessionWarrior() {
        // GIVEN
        Instant time = Instant.now();
        Optional<AsterixCharacter> testdata = Optional.of(new AsterixCharacter("1", "Asterix", 45, "Krieger", time));
        when(repo.findById("1")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);
        AsterixCharacter expected = new AsterixCharacter("1", "Asterix", 45, "Warrior", time);
        when(repo.save(expected)).thenReturn(expected);

        // WHEN
        AsterixCharacter actual = underTest.updateProfession("1", "Warrior");

        // THEN
        assertEquals(expected, actual);
        verify(repo).findById("1");
        verify(repo).save(expected);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void udpateProfessionTest_whenCharacterDoesntExist_thenException() {
        // GIVEN
        Optional<AsterixCharacter> testdata = Optional.empty();
        when(repo.findById("2")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        assertThrows(NoSuchElementException.class, () ->

                // THEN
                underTest.updateProfession("2", "Warrior"));

        verify(repo).findById("2");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getCharacterTest_whenExists_thenReturnCharacter() {
        // GIVEN
        Optional<AsterixCharacter> testdata = Optional.of(new AsterixCharacter("1", "Asterix", 45, "Krieger", Instant.now()));
        when(repo.findById("1")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        AsterixCharacter actual = underTest.getCharacter("1");

        // THEN
        assertEquals(testdata.get(), actual);
        verify(repo).findById("1");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void getCharacterTest_whenNotExists_thenThrowException() {
        // GIVEN
        Optional<AsterixCharacter> testdata = Optional.empty();
        when(repo.findById("2")).thenReturn(testdata);
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        assertThrows(NoSuchElementException.class, () ->

                // THEN
                underTest.getCharacter("2"));

        verify(repo).findById("2");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void saveTest_whenNewCharacter_thenReturnNewCharacterWithId() {
        // GIVEN
        when(idService.randomId()).thenReturn("123");
        Instant time = Instant.now();
        AsterixCharacterDTO input = new AsterixCharacterDTO("Asterix", 45, "Krieger");
        AsterixCharacter expected = new AsterixCharacter("123", "Asterix", 45, "Krieger", time);
        when(repo.save(expected)).thenReturn(expected);
        AsterixService underTest = new AsterixService(repo, idService);
        Mockito.mockStatic(Instant.class);
        Mockito.when(Instant.now()).thenReturn(time);

        // WHEN
        AsterixCharacter actual = underTest.save(input);

        // THEN
        assertEquals(expected, actual);
        verify(idService).randomId();
        verify(repo).save(expected);
        verifyNoMoreInteractions(idService);
        verifyNoMoreInteractions(repo);
    }

    @Captor
    ArgumentCaptor<AsterixCharacter> asterixCaptor;

    @Test
    void saveTest_whenNewCharacter_SanityCheckCreatedAt() {
        // GIVEN
        when(idService.randomId()).thenReturn("123");
        AsterixCharacterDTO input = new AsterixCharacterDTO("Asterix", 45, "Krieger");
        AsterixService underTest = new AsterixService(repo, idService);

        // WHEN
        underTest.save(input);

        // THEN
        verify(repo).save(asterixCaptor.capture());
        AsterixCharacter capturedCharacter = asterixCaptor.getValue();
        Instant capturedCreatedAt = capturedCharacter.createdAt();
        assertTrue(capturedCreatedAt.isAfter(Instant.parse("2000-01-01T00:00:00Z")));

        ZonedDateTime capturedZoned = capturedCreatedAt.atZone(ZoneId.systemDefault());
        ZonedDateTime ninePM = capturedZoned.withHour(21).withMinute(0).withSecond(0).withNano(0);
        assertTrue(capturedZoned.isBefore(ninePM));
        verify(idService).randomId();
        verifyNoMoreInteractions(idService);
        verifyNoMoreInteractions(repo);
    }
}