package org.ln.noortools.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.tag.Album;
import org.ln.noortools.tag.Artist;
import org.ln.noortools.tag.Crc32;
import org.ln.noortools.tag.CreationDate;
import org.ln.noortools.tag.Date;
import org.ln.noortools.tag.DecrHex;
import org.ln.noortools.tag.DecrNum;
import org.ln.noortools.tag.DecR;
import org.ln.noortools.tag.FileOwner;
import org.ln.noortools.tag.IncL;
import org.ln.noortools.tag.IncrHex;
import org.ln.noortools.tag.IncrNum;
import org.ln.noortools.tag.IncR;
import org.ln.noortools.tag.Md5;
import org.ln.noortools.tag.ModifyDate;
import org.ln.noortools.tag.RandL;
import org.ln.noortools.tag.RandN;
import org.ln.noortools.tag.Sha256;
import org.ln.noortools.tag.Subs;
import org.ln.noortools.tag.Time;
import org.ln.noortools.tag.Title;
import org.ln.noortools.tag.Word;
import org.ln.noortools.tag.WriteAlbum;
import org.ln.noortools.tag.WriteArtist;
import org.ln.noortools.tag.WriteCreationDate;
import org.ln.noortools.tag.WriteModifyDate;
import org.ln.noortools.tag.WriteOwner;
import org.ln.noortools.tag.WriteTitle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Factory for creating tag instances used by NOOR Tools.
 *
 * Tags are registered dynamically in a central registry instead of being
 * exposed via dozens of near-identical creator methods. This makes it easier
 * to add new tags and to iterate over all available ones when populating the
 * UI catalogue.
 */
@Component
@Scope("singleton")
public class TagFactory {

    private final I18n i18n;
    private final Map<String, TagRegistration> registry = new LinkedHashMap<>();

    public TagFactory(I18n i18n) {
        this.i18n = i18n;
        registerDefaults();
    }

    /** Creates a tag instance by its canonical name (e.g., "IncN"). */
    public AbstractTag create(String tagName, Object... args) {
        TagRegistration registration = registry.get(tagName);
        if (registration == null) {
            return null;
        }
        return registration.factory.apply(safeArgs(args));
    }

    /** Returns a preview instance of each registered tag. */
    public List<AbstractTag> buildAllTags() {
        List<AbstractTag> list = new ArrayList<>();
        for (TagRegistration registration : registry.values()) {
            list.add(registration.factory.apply(safeArgs(registration.previewArgs)));
        }
        return list;
    }

    private void registerDefaults() {
        // Numeric
        register("IncN", args -> new IncrNum(i18n, args), new Object[]{1, 1});
        register("DecN", args -> new DecrNum(i18n, args), new Object[]{1, 1});
        register("IncH", args -> new IncrHex(i18n, args), new Object[]{1, 1});
        register("DecH", args -> new DecrHex(i18n, args), new Object[]{1, 1});
        register("IncR", args -> new IncR(i18n, args), new Object[]{1, 1});
        register("DecR", args -> new DecR(i18n, args), new Object[]{1, 1});
        register("IncL", args -> new IncL(i18n, args), new Object[]{1, 1});
        register("RandN", args -> new RandN(i18n, args), new Object[]{4, 1});

        // String
        register("Subs", args -> new Subs(i18n, args), new Object[]{1, 1});
        register("Word", args -> new Word(i18n, args), new Object[]{1, 1});
        register("RandL", args -> new RandL(i18n, args), new Object[]{4, 1});

        // Date/Time
        register("Date", args -> new Date(i18n, args), new Object[]{"yyyy-mm-dd"});
        register("Time", args -> new Time(i18n, args), new Object[]{"hh:nn:ss"});

        // Audio
        register("Album", args -> new Album(i18n, args), new Object[0]);
        register("Artist", args -> new Artist(i18n, args), new Object[0]);
        register("Title", args -> new Title(i18n, args), new Object[0]);
        register("WriteAlbum", args -> new WriteAlbum(i18n, args), new Object[]{"Name"});
        register("WriteArtist", args -> new WriteArtist(i18n, args), new Object[]{"Name"});
        register("WriteTitle", args -> new WriteTitle(i18n, args), new Object[]{"Name"});

        // Checksum
        register("Sha256", args -> new Sha256(i18n, args), new Object[]{8});
        register("Crc32", args -> new Crc32(i18n, args), new Object[0]);
        register("Md5", args -> new Md5(i18n, args), new Object[0]);

        // FileSystem
        register("CreationDate", args -> new CreationDate(i18n, args), new Object[0]);
        register("ModifyDate", args -> new ModifyDate(i18n, args), new Object[0]);
        register("FileOwner", args -> new FileOwner(i18n, args), new Object[0]);
        register("WriteCreationDate", args -> new WriteCreationDate(i18n, args), new Object[0]);
        register("WriteModifyDate", args -> new WriteModifyDate(i18n, args), new Object[0]);
        register("WriteOwner", args -> new WriteOwner(i18n, args), new Object[0]);
    }

    private void register(String name, Function<Object[], AbstractTag> factory, Object[] previewArgs) {
        registry.put(name, new TagRegistration(factory, safeArgs(previewArgs)));
    }

    private Object[] safeArgs(Object[] args) {
        Object[] nonNull = Objects.requireNonNullElse(args, new Object[0]);
        return nonNull.clone();
    }

    private record TagRegistration(Function<Object[], AbstractTag> factory, Object[] previewArgs) { }
}
