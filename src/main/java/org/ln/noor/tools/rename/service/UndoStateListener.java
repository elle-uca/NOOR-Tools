package org.ln.noor.tools.rename.service;
/**
 * UndoStateListener.
 *
 * @author Luca Noale
 */


public interface UndoStateListener {
	
	
	void onUndoStateChanged(boolean undoAvailable);
}
