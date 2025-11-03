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
		//initReflection();
		initClassic();
	}

	private void initClassic() {
        // Each tag is created through the factory (I18n injection included)
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

	@SuppressWarnings("unused")
//	private void initReflection() {
//		dataList = new ArrayList<>();
//		
//        List<Class<?>> classes = null;
//		try {
//			classes = FileUtils.getClasses("org.ln.java.renamer.tag");
//		} catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace();
//		}
//        RnTag tag = null;
//        Integer[] arr = {1,1};
//        for (Class<?> clazz : classes) {
//        	if(!clazz.getSuperclass().getName().equals("java.lang.Object")) {
//        		Constructor<?> cons = null;
//				try {
//					cons = clazz.getDeclaredConstructor(Integer[].class);
//					tag = (RnTag) cons.newInstance((Object) arr);
//					dataList.add(tag);
//				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//					e.printStackTrace();
//				}
//        	}
//         }
//	}
	
	
	@Override
	public int getSize() {
		return dataList.size();
	}

	@Override
	public AbstractTag getElementAt(int index) {
		return dataList.get(index);
	}

}
