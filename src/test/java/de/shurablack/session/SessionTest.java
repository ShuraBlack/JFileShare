package de.shurablack.session;

import de.shurablack.util.TimeProvider;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionTest {

    @Mock
    private TimeProvider timeProvider = mock(TimeProvider.class);

    @InjectMocks
    private Session session;

    @BeforeEach
    void setUp() {
        session = new Session("/test", timeProvider);
    }

    @Test
    void changeDirectory_setCurrentDirectory_returnChangedDirectory() {
        final String expected = "/test2";

        session.setCurrentDirectory("/test2");

        final String actual = session.getCurrentDirectory();

        assertEquals(expected, actual);
    }

    @Test
    void afterExpiration_isExpired_returnTrue() {
        when(timeProvider.currentTimeMillis()).thenReturn(System.currentTimeMillis() + 1800000L);

        final boolean result = session.isExpired();

        assertTrue(result);
    }

    @Test
    void afterRefresh_isExpired_returnFalse() {
        when(timeProvider.currentTimeMillis()).thenReturn(System.currentTimeMillis() + 1799999L);

        session.refresh();

        final boolean result = session.isExpired();

        assertFalse(result);
    }

    @Test
    void afterRefresh_getIdentifier_returnNewIdentifier() {
        final String oldIdentifier = session.getIdentifier();

        session.refresh();

        final String newIdentifier = session.getIdentifier();

        assertFalse(oldIdentifier.equals(newIdentifier));
    }

    @Test
    void convertTo_json_returnJson() {
        final JSONObject actual = session.toJson();

        assertEquals(session.getIdentifier(), actual.getString("identifier"));
        assertEquals(session.getExpirationTime(), actual.getLong("expirationTime"));
        assertFalse(actual.getBoolean("uploadAllowed"));
    }
}