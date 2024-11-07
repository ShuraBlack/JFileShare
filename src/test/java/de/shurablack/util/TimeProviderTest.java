package de.shurablack.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeProviderTest {

    @Test
    void currentTimeMillis_returnCurrentTime() {
        final TimeProvider timeProvider = new TimeProvider();
        final long currentTime = timeProvider.currentTimeMillis();
        final long currentTimeAfter = timeProvider.currentTimeMillis();
        assertTrue(currentTime <= currentTimeAfter);
    }
}