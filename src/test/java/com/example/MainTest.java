package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testAdd() {
        assertEquals(3, Main.add(1, 2));
    }
}
