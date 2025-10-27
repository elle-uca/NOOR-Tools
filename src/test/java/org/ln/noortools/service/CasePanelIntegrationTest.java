//package org.ln.noortools.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.io.File;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.ln.noortools.enums.ModeCase;
//import org.ln.noortools.model.RenamableFile;
//import org.ln.noortools.util.FileUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class CasePanelIntegrationTest {
//
//    @Autowired
//    private TagProcessorService processor;
//
//    @Test
//    @DisplayName("CasePanel simulation: apply UPPER to file list")
//    void testCasePanelUpper() {
//        // Simula lista file (come se arrivasse da JTable)
//        List<RenamableFile> files = List.of(
//                new RenamableFile(new File("fileOne.txt")),
//                new RenamableFile(new File("helloWorld.java")),
//                new RenamableFile(new File("TestFile.pdf"))
//        );
//
//        // Prendiamo solo i nomi senza estensione
//        List<String> originalNames = files.stream()
//                .map(f -> FileUtil.getNameWithoutExtension(f.getSource()))
//                .collect(Collectors.toList());
//
//        // Simuliamo la scelta di UPPER nel CasePanel
//        List<String> transformed = processor.processRule("caseruleservice", originalNames, ModeCase.UPPER);
//
//        // Aggiorniamo i file (simulando updateView di CasePanel)
//        for (int i = 0; i < files.size(); i++) {
//            files.get(i).setDestinationName(transformed.get(i));
//        }
//
//        // ✅ Verifica risultato
//        assertEquals("FILEONE",    files.get(0).getDestinationName());
//        assertEquals("HELLOWORLD", files.get(1).getDestinationName());
//        assertEquals("TESTFILE",   files.get(2).getDestinationName());
//    }
//}
