package org.ln.noor.tools.directory;

import javax.swing.JFrame;

import org.ln.noor.core.tool.NoorTool;


public class DirectoryTool implements NoorTool {

    @SuppressWarnings("unused")
	private final JFrame owner;

    public DirectoryTool(JFrame owner) {
        this.owner = owner;
    }

    @Override
    public String getId() {
        return "directory";
    }

    @Override
    public String getDisplayName() {
        return "Directory Tools";
    }

    @Override
    public void open() {
       DirectoryToolLauncher.open();
    }
}
