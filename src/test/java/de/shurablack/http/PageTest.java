package de.shurablack.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PageTest {

    @InjectMocks
    private Page page = new Page("null.html", true);

    @Test
    void invalidInternFile_returnEmptyArray() {
        assertArrayEquals(new byte[0], page.getBytes());
    }
}