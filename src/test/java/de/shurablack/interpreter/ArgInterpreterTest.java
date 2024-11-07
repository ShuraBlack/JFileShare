package de.shurablack.interpreter;

import de.shurablack.util.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArgInterpreterTest {

    @Test
    void validArguments_interpret_changeConfig() {
        String[] args = {"-port=8080", "-verbose", "-hidden", "-noroot", "-threads=4", "-root=/home/user"};

        ArgInterpreter.interpret(args);

        assertEquals(8080, Config.getPort());
        assertTrue(Config.isVerbose());
        assertTrue(Config.isShowHiddenFiles());
        assertFalse(Config.isRootRestricted());
        assertEquals(4, Config.getThreadPoolSize());
        assertEquals("/home/user", Config.getRootDirectory());
    }
}