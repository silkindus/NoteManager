package com.example.dilk.notemanager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Note testNote;

    @Before
    public void init(){
        testNote = new Note("05", "Sample", "Text");
    }

    @Test
    public void ID_isCorrect() {
        String expectedID = "05";
        assertEquals(expectedID, testNote.getId());
    }
    @Test
    public void Title_isCorrect() {
        String expectedTitle = "Sample";
        assertEquals(expectedTitle, testNote.getTitle());
    }
    @Test
    public void Content_isCorrect() {
        String expectedContent = "Text";
        assertEquals(expectedContent, testNote.getContent());
    }
}