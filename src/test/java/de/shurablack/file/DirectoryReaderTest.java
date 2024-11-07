package de.shurablack.file;

import de.shurablack.util.Config;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectoryReaderTest {

    @Mock
    private File file = mock(File.class);

    @Test
    void nonDirectory_readDirectory_returnEmptyJSONArray() {
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);

        final JSONArray result = DirectoryReader.readDirectory(file);

        assertEquals(0, result.length());
    }

    @Test
    void nonExistingDirectory_readDirectory_returnEmptyJSONArray() {
        when(file.exists()).thenReturn(false);

        final JSONArray result = DirectoryReader.readDirectory(file);

        assertEquals(0, result.length());
    }

    @Test
    void readDirectory_returnJSONArray() {
        when(file.getName()).thenReturn("test");
        when(file.isHidden()).thenReturn(false);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.listFiles()).thenReturn(new File[] {file, file});

        final JSONArray result = DirectoryReader.readDirectory(file);

        assertNotNull(result);
        assertEquals(2, result.length());
    }

    @Test
    void hiddenFiles_readDirectory_returnEmptyJSONArray() {
        Config.set("SHOW_HIDDEN_FILES", "false");
        when(file.isHidden()).thenReturn(true);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.listFiles()).thenReturn(new File[] {file, file});

        final JSONArray result = DirectoryReader.readDirectory(file);

        assertEquals(0, result.length());
    }

    @Test
    void allowHiddenFiles_readDirectory_returnJSONArray() {
        Config.set("SHOW_HIDDEN_FILES", "true");
        when(file.isHidden()).thenReturn(true);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        when(file.listFiles()).thenReturn(new File[] {file, file});

        final JSONArray result = DirectoryReader.readDirectory(file);

        assertEquals(2, result.length());
    }
}