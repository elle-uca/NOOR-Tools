package org.ln.noortools.model;

import java.io.File;
import java.util.Objects;

import org.ln.noortools.enums.FileStatus;
import org.ln.noortools.util.FileNameUtil;

/**
 * Represents a file that can be renamed.
 * Wraps the original {@link File}, the destination name, 
 * selection status and rename result.
 * 
 * Author: Luca Noale
 */
public class RenamableFile {

    private final File source;              // original file
    private FileStatus fileStatus;          // status (OK, KO, etc.)
    private String destinationName;         // new name without path
    private boolean selected = true;        // default = selected

    public RenamableFile(String string) {
		this(new File(string));
	}   
    
    public RenamableFile(File source) {
        this.source = Objects.requireNonNull(source, "source file cannot be null");
        this.destinationName = "";
        this.fileStatus = source.exists() ? FileStatus.OK : FileStatus.KO;
    }



	/** @return original source file */
    public File getSource() {
        return source;
    }

    /** @return file extension (without dot) */
    public String getExtension() {
        return FileNameUtil.getExtension(source.getName());
    }

    /** @return parent directory path */
    public String getParentPath() {
        return source.getParent();
    }

    /** @return new destination name (without path) */
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    /** @return file status */
    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    /** @return true if selected for renaming */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSafeDestinationName() {
        return (destinationName == null || destinationName.isBlank())
                ? source.getName()
                : destinationName;
    }
    
    @Override
    public String toString() {
        return "RenamableFile{source=" + source +
               ", destName='" + destinationName + '\'' +
               ", status=" + fileStatus +
               ", selected=" + selected +
               '}';
    }
}
