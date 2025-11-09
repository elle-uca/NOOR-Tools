package org.ln.noortools.view;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.swing.AbstractListModel;

import org.ln.noortools.factory.TagFactory;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.tag.AbstractTag.TagType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ListModel for available tags with built-in filtering:
 *  - text query (matches name/description/tag string)
 *  - type filter (one or more TagType)
 *
 * Keeps a master list (all tags) and a filtered view.
 */
@SuppressWarnings("serial")
@Component
@Scope("singleton")
public class TagListModel extends AbstractListModel<AbstractTag> {
	
//	private AbstractTag.TagType activeType = null; // null = mostra tutto

    // Immutable master data loaded from TagFactory
    private final List<AbstractTag> master = new ArrayList<>();
    // Current filtered view
    private final List<AbstractTag> filtered = new ArrayList<>();

    // Filters
    private String query = "";
    private EnumSet<TagType> typeFilter = EnumSet.allOf(TagType.class);

    // For quick stats per type (optional utility for UI badges, etc.)
    private final Map<TagType, Integer> typeCounts = new EnumMap<>(TagType.class);

    public TagListModel(TagFactory tagFactory) {
        // 1) Load all tags once from the factory
        master.addAll(tagFactory.buildAllTags());
        // 2) Precompute counts per type
        recomputeTypeCounts();
        // 3) Initial filtered = all
        refilterAndFire();
    }

    // ---------------------------
    // AbstractListModel contract
    // ---------------------------

    @Override
    public int getSize() {
        return filtered.size();
    }

    @Override
    public AbstractTag getElementAt(int index) {
        return filtered.get(index);
    }

    // ---------------------------
    // Public API (filters)
    // ---------------------------

    /** Set a free-text query (case-insensitive). Null becomes empty. */
    public void setQuery(String q) {
        String newQ = (q == null) ? "" : q.trim();
        if (!Objects.equals(newQ, this.query)) {
            this.query = newQ;
            refilterAndFire();
        }
    }

    /** Replace the type filter with a set of allowed types. If null or empty, show none. */
    public void setTypeFilter(EnumSet<TagType> types) {
        EnumSet<TagType> newFilter = (types == null) ? EnumSet.noneOf(TagType.class) : EnumSet.copyOf(types);
        if (!newFilter.equals(this.typeFilter)) {
            this.typeFilter = newFilter;
            refilterAndFire();
        }
    }

    /** Convenience: show all types. */
    public void clearTypeFilter() {
        setTypeFilter(EnumSet.allOf(TagType.class));
    }

     
    /** Convenience: filter to a single type, or show all if null. */
    public void setSingleType(TagType type) {
        if (type == null) {
            clearTypeFilter(); // mostra tutto
        } else {
            setTypeFilter(EnumSet.of(type));
        }
    }

    /** Current text query. */
    public String getQuery() {
        return query;
    }

    /** Current type filter. */
    public EnumSet<TagType> getTypeFilter() {
        return EnumSet.copyOf(typeFilter);
    }

    /**
     * Optional: return the total number of tags per type (independent of current filter).
     * Useful to render counts/badges in a segmented control or tab headers.
     */
    public Map<TagType, Integer> getTypeCounts() {
        return new EnumMap<>(typeCounts);
    }

    // ---------------------------
    // Internals
    // ---------------------------

    private void refilterAndFire() {
        int oldSize = filtered.size();
        filtered.clear();

        final String q = query.toLowerCase(Locale.ROOT);

        for (AbstractTag tag : master) {
            if (!typeFilter.contains(tag.getType()))
                continue;

            if (q.isEmpty() || matchesQuery(tag, q)) {
                filtered.add(tag);
            }
        }

        // Notify model changes. Easiest: full reset.
        if (oldSize > 0 || !filtered.isEmpty()) {
            fireContentsChanged(this, 0, Math.max(oldSize, filtered.size()));
        }
    }

    private boolean matchesQuery(AbstractTag tag, String q) {
        // Match on tag name, description, or tag string
        String name = safe(tag.getTagName());
        String desc = safe(tag.getDescription());
        String tagStr = safe(tag.getTagString());
        return name.contains(q) || desc.contains(q) || tagStr.contains(q);
    }

    private String safe(String s) {
        return (s == null) ? "" : s.toLowerCase(Locale.ROOT);
    }
//
//    private List<AbstractTag> buildAllTags(TagFactory f) {
//        // Central place to list all tags; you can move this into TagFactory as `createAllTags()`
//        List<AbstractTag> list = new ArrayList<>();
//        // Numeric
//        list.add(f.createIncNTag(1, 1));
//        list.add(f.createDecNTag(1, 1));
//        list.add(f.createIncHTag(1, 1));
//        list.add(f.createDecHTag(1, 1));
//         list.add(f.createIncRTag(1, 1));
//        list.add(f.createDecRTag(1, 1));
//        list.add(f.createRandNTag(4, 1));
//        // String
//        list.add(f.createIncLTag(1, 1));
//        list.add(f.createSubsTag(1, 1));
//        list.add(f.createWordTag(1, 1));
//        list.add(f.createRandLTag(4, 1));
//        // Date/Time
//        list.add(f.createDateTag("dd-MMM-yyyy"));
//        list.add(f.createTimeTag("HH:mm:ss"));
//        // Audio
//        // list.add(f.createAudioTag(...)) // when available
//        // Checksum
//     
//        return list;
//    }

    private void recomputeTypeCounts() {
        typeCounts.clear();
        for (TagType t : TagType.values()) typeCounts.put(t, 0);
        for (AbstractTag tag : master) {
            TagType t = tag.getType();
            typeCounts.put(t, typeCounts.getOrDefault(t, 0) + 1);
        }
    }
    
    
    
    
}


//

//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.AbstractListModel;
//
//import org.ln.noortools.factory.TagFactory;
//import org.ln.noortools.tag.AbstractTag;
//import org.springframework.stereotype.Component;
//
//
///**
// * List model for displaying available renaming tags in the UI.
// * 
// * The model delegates tag creation to {@link TagFactory},
// * ensuring that each tag receives its dependencies (e.g. I18n)
// * through Spring-managed injection.
// * 
// * Author: Luca Noale
// */
//@SuppressWarnings("serial")
//@Component
//public class TagListModel extends AbstractListModel<AbstractTag> {
//
//	List<AbstractTag> dataList;
//
//	  private final TagFactory tagFactory;
//
//	public TagListModel(TagFactory tagFactory) {
//		this.tagFactory = tagFactory;
//		dataList = new ArrayList<AbstractTag>();
//		initClassic();
//	}
//
//	private void initClassic() {
//        dataList.add(tagFactory.createIncNTag(1, 1));
//        dataList.add(tagFactory.createDecNTag(1, 1));
//        dataList.add(tagFactory.createSubsTag(1, 1));
//        dataList.add(tagFactory.createWordTag(1, 1));
//        dataList.add(tagFactory.createRandNTag(4, 1));
//        dataList.add(tagFactory.createRandLTag(4, 1));
//        dataList.add(tagFactory.createIncRTag(1, 1));
//        dataList.add(tagFactory.createIncHTag(1, 1));
//        dataList.add(tagFactory.createIncLTag(1, 1));
//        dataList.add(tagFactory.createDateTag("dd-mmm-yyyy"));
//        dataList.add(tagFactory.createTimeTag("hh:nn:ss"));
//	}
//
//	
//	@Override
//	public int getSize() {
//		return dataList.size();
//	}
//
//	@Override
//	public AbstractTag getElementAt(int index) {
//		return dataList.get(index);
//	}
//
//}
