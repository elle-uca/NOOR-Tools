package org.ln.noortools.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import org.ln.noortools.factory.TagFactory;
import org.ln.noortools.tag.AbstractTag;
import org.springframework.stereotype.Component;


/**
 * List model for displaying available renaming tags in the UI.
 * 
 * The model delegates tag creation to {@link TagFactory},
 * ensuring that each tag receives its dependencies (e.g. I18n)
 * through Spring-managed injection.
 * 
 * Author: Luca Noale
 */
@SuppressWarnings("serial")
@Component
public class TagListModel extends AbstractListModel<AbstractTag> {

	List<AbstractTag> dataList;

	  private final TagFactory tagFactory;

	public TagListModel(TagFactory tagFactory) {
		this.tagFactory = tagFactory;
		dataList = new ArrayList<AbstractTag>();
		initClassic();
	}

	private void initClassic() {
        dataList.add(tagFactory.createIncNTag(1, 1));
        dataList.add(tagFactory.createDecNTag(1, 1));
        dataList.add(tagFactory.createSubsTag(1, 1));
        dataList.add(tagFactory.createWordTag(1, 1));
        dataList.add(tagFactory.createRandNTag(4, 1));
        dataList.add(tagFactory.createRandLTag(4, 1));
        dataList.add(tagFactory.createIncRTag(1, 1));
        dataList.add(tagFactory.createIncHTag(1, 1));
        dataList.add(tagFactory.createIncLTag(1, 1));
        dataList.add(tagFactory.createDateTag("dd-mmm-yyyy"));
        dataList.add(tagFactory.createTimeTag("hh:nn:ss"));
	}

	
	@Override
	public int getSize() {
		return dataList.size();
	}

	@Override
	public AbstractTag getElementAt(int index) {
		return dataList.get(index);
	}

}
