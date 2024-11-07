package de.shurablack.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FormatterTest {

    @Test
    void validSize_formatSize_returnFormattedSize() {
        final long size = 1024L;
        final String expected = "1,0 KB";

        final String actual = Formatter.formatSize(size);

        assertEquals(expected, actual);
    }

    @Test
    void validByteSize_formatSize_returnFormattedSize() {
        final long size = 1L;
        final String expected = "1 B";

        final String actual = Formatter.formatSize(size);

        assertEquals(expected, actual);
    }

    @Test
    void invalidSize_formatSize_returnZeroSize() {
        final long size = -1L;
        final String expected = "0 B";

        final String actual = Formatter.formatSize(size);

        assertEquals(expected, actual);
    }

    @Test
    void validTime_formatTime_returnFormattedTime() {
        final long time = 1615766400000L;
        final String expected = "2021.03.15 01:00:00";

        final String actual = Formatter.formatTime(time);

        assertEquals(expected, actual);
    }
}