package in.kahl.asterixapi;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdServiceTest {

    @Test
    void randomIdTest() {
        // GIVEN
        UUID mockedUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Mockito.mockStatic(UUID.class);
        Mockito.when(UUID.randomUUID()).thenReturn(mockedUUID);
        IdService underTest = new IdService();
        // WHEN
        String actual = underTest.randomId();

        // THEN
        assertEquals(mockedUUID.toString(), actual);
    }
}