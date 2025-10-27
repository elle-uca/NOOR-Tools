package org.ln.noortools;

import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.ln.noortools.enums.ModeCase;
import org.ln.noortools.service.TagProcessorService;
import org.ln.noortools.view.MainFrame;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NoorToolsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
            new SpringApplicationBuilder(NoorToolsApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = context.getBean(MainFrame.class);
            frame.setVisible(true);
        });
        
        

        TagProcessorService processor = context.getBean(TagProcessorService.class);

        List<String> names = Arrays.asList("fileA.txt", "fileB.txt", "fileC.txt");


        System.out.println("Available tags: " + processor.getAvailableTags());
        System.out.println("IncN: " + processor.processTag("IncN", names, 100, 2));
        System.out.println("DecN: " + processor.processTag("RandL", names, 5));
        
        List<String> files = List.of("hello world.txt", "java CODE.doc");

     // Usa un tag
     System.out.println(processor.processTag("IncN", files, 1, 2));

     // Usa una regola
    // System.out.println(processor.processRule("caseruleservice", files, ModeCase.TITLE_CASE));
    }
}