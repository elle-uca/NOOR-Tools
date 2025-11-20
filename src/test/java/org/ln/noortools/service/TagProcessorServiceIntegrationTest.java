//package org.ln.noortools.service;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.ln.noortools.NoorToolsApplication;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest(classes = NoorToolsApplication.class)
//class TagProcessorServiceIntegrationTest {
//
//    @Autowired
//    private TagProcessorService processor;
//
//    @Test
//    void testSubsTag() {
//        List<String> input = Arrays.asList("Filename.txt", "AnotherFile.log");
//
//        // Subs: prende primi 3 caratteri
//        List<String> output = processor.processTag("Subs", input, 0, 3);
//
//        assertEquals(Arrays.asList("Fil", "Ano"), output);
//    }
//
//    @Test
//    void testIncNTag() {
//        List<String> input = Arrays.asList("fileA", "fileB", "fileC");
//
//        // IncN: start=1 step=2 → 1, 3, 5
//        List<String> output = processor.processTag("IncN", input, 1, 2);
//
//        assertEquals(Arrays.asList("1", "3", "5"), output);
//    }
//
//    @Test
//    void testDecNTag() {
//        List<String> input = Arrays.asList("one", "two", "three");
//
//        // DecN: start=10 step=2 → 10, 8, 6
//        List<String> output = processor.processTag("DecN", input, 10, 2);
//
//        assertEquals(Arrays.asList("10", "8", "6"), output);
//    }
//}
