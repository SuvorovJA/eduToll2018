package edu.server.services;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class OpenFileForWriteTest {

    private OpenFileForWrite openFileForWrite = null;
    private String fileName;

    @Test
    public void init_and_writeln() {
        openFileForWrite = new OpenFileForWrite();
        openFileForWrite.init();
        fileName = openFileForWrite.getFileName();
        final File file = new File(fileName);
        assertTrue(file.exists());
        for (Integer i = 0; i < 21 ; i++) {
            openFileForWrite.writeln(Integer.toString(i));
        }
        assertEquals(50L,file.length());
    }
}