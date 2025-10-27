package org.ln.noortools.tag;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for all renaming tags used by NOOR Tools.
 * 
 * Example of a tag: <IncN:1:1> or <Date:yyyy-MM-dd>
 * 
 * Each tag performs a specific transformation or generates a part
 * of the new filename during the renaming process.
 * 
 * @author Luca Noale
 */
public abstract class AbstractTag {


	protected String tagName;
	
	protected int start; 	
	protected int step;		
	protected int pos;		
	
	private List<String> oldNames = new ArrayList<String>();
	
	private List<String> newNames = new ArrayList<String>();;
	
	 protected Object[] rawArgs;
	 
	 protected Integer[] normArgs;
	
	 @Autowired
	 protected I18n i18n;


	protected static final char OPEN_TAG = '<';
	protected static final char CLOSE_TAG = '>';
	protected static final String SEPARATOR_TAG = ":";

	protected Object[] args;
	

	 /**
     * Base constructor for all tags.
     */
    public AbstractTag(Object... args) {
    	setArgs(args);
    }


	
    /** Parser/normalizer centrale degli argomenti */
    public final void setArgs(Object... args) {
        this.rawArgs = args != null ? args : new Object[0];
        this.normArgs = new Integer[rawArgs.length];

        for (int i = 0; i < rawArgs.length; i++) {
            normArgs[i] = coerceToInt(rawArgs[i], 0);
        }
    }
	
    /** Utility: prova a convertire un Object in int */
    protected int coerceToInt(Object val, int def) {
        if (val == null) return def;
        if (val instanceof Number) return ((Number) val).intValue();
        if (val instanceof String) {
            try { return Integer.parseInt((String) val); }
            catch (NumberFormatException e) { return def; }
        }
        return def;
    }
    
    /** Utility generica per recuperare argomento */
    protected int getIntArg(int index, int def) {
        return (index < normArgs.length) ? normArgs[index] : def;
    }

    protected String getStringArg(int index, String def) {
        if (index < rawArgs.length && rawArgs[index] != null) {
            return rawArgs[index].toString();
        }
        return def;
    }

    
	
    /**
     * Initialize the tag logic and produce new names.
     */
	public abstract void init() ;
	


    /**
     * Returns a short description of what this tag does.
     */
    public abstract String getDescription();



    /**
     * Returns the tag name (e.g., "IncN" or "Date").
     */
    public String getTagName() {
        return tagName;
    }


    /**
     * Returns the processed list of names.
     */
    public List<String> getNewNames() {
        return newNames;
    }
	/**
	 * @param newName the newName to set
	 */
	public void setNewNames(List<String> newNames) {
		this.newNames = newNames;
	}


	/**
	 * @return the oldName
	 */
	public List<String> getOldNames() {
		return oldNames;
	}


    /**
     * Sets the list of original names before processing.
     */
    public void setOldNames(List<String> oldNames) {
        this.oldNames = oldNames;
    }

    
    
    /**
	 * @return
	 * @see java.util.List#size()
	 */
	public int oldSize() {
		return oldNames.size();
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void oldClear() {
		oldNames.clear();
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int newSize() {
		return newNames.size();
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void newClear() {
		newNames.clear();
	}

	
	
	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean newAdd(String e) {
		return newNames.add(e);
	}



	/**
     * Returns the arguments passed to the tag (if any).
     */
    public Object[] getArgs() {
        return args;
    }
    
    


	public String getTagString() {
		return OPEN_TAG+tagName+SEPARATOR_TAG+start+CLOSE_TAG;
	}

	@Override
	public String toString() {
		return getTagString()+" - "+getDescription();
	}
}
