package de.shurablack.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MediaEntryTest {

    @InjectMocks
    private MediaEntry mediaEntry = new MediaEntry("media.png", "image/png");

    @Test
    void invalidInternFile_returnEmptyArray() {
        assertArrayEquals(new byte[0], mediaEntry.readData());
    }

    @Test
    void getType_returnType() {
        assertEquals("image/png", mediaEntry.getType());
    }
}