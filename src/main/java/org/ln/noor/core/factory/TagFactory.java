package org.ln.noor.core.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.tag.AbstractTag;
import org.ln.noor.tools.rename.tag.Album;
import org.ln.noor.tools.rename.tag.Artist;
import org.ln.noor.tools.rename.tag.Crc32;
import org.ln.noor.tools.rename.tag.CreationDate;
import org.ln.noor.tools.rename.tag.Date;
import org.ln.noor.tools.rename.tag.DecrHex;
import org.ln.noor.tools.rename.tag.DecrNum;
import org.ln.noor.tools.rename.tag.DecrRom;
import org.ln.noor.tools.rename.tag.FileOwner;
import org.ln.noor.tools.rename.tag.IncrHex;
import org.ln.noor.tools.rename.tag.IncrLet;
import org.ln.noor.tools.rename.tag.IncrNum;
import org.ln.noor.tools.rename.tag.IncrRom;
import org.ln.noor.tools.rename.tag.Md5;
import org.ln.noor.tools.rename.tag.ModifyDate;
import org.ln.noor.tools.rename.tag.Name;
import org.ln.noor.tools.rename.tag.RandLet;
import org.ln.noor.tools.rename.tag.RandNum;
import org.ln.noor.tools.rename.tag.Sha1;
import org.ln.noor.tools.rename.tag.Sha256;
import org.ln.noor.tools.rename.tag.Sha512;
import org.ln.noor.tools.rename.tag.Subs;
import org.ln.noor.tools.rename.tag.Time;
import org.ln.noor.tools.rename.tag.Title;
import org.ln.noor.tools.rename.tag.Word;
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
        register("IncrNum", args -> new IncrNum(i18n, args), new Object[]{1, 1});
        register("DecrNum", args -> new DecrNum(i18n, args), new Object[]{1, 1});
        register("IncrHex", args -> new IncrHex(i18n, args), new Object[]{1, 1});
        register("DecrHex", args -> new DecrHex(i18n, args), new Object[]{1, 1});
        register("IncrRom", args -> new IncrRom(i18n, args), new Object[]{1, 1});
        register("DecrRom", args -> new DecrRom(i18n, args), new Object[]{1, 1});
        register("IncrLet", args -> new IncrLet(i18n, args), new Object[]{1, 1});
        register("RandNum", args -> new RandNum(i18n, args), new Object[]{4});

        // String
        register("Subs", args -> new Subs(i18n, args), new Object[]{1, 1});
        register("Word", args -> new Word(i18n, args), new Object[]{1, 1});
        register("RandLet", args -> new RandLet(i18n, args), new Object[]{4});
        register("Name", args -> new Name(i18n, args), new Object[0]);

        // Date/Time
        register("Date", args -> new Date(i18n, args), new Object[]{"yyyy-mm-dd"});
        register("Time", args -> new Time(i18n, args), new Object[]{"hh-nn-ss"});

        // Audio
        register("Album", args -> new Album(i18n, args), new Object[0]);
        register("Artist", args -> new Artist(i18n, args), new Object[0]);
        register("Title", args -> new Title(i18n, args), new Object[0]);


        // Checksum
        register("Sha1", args -> new Sha1(i18n, args), new Object[0]);
        register("Sha256", args -> new Sha256(i18n, args), new Object[]{8});
        register("Sha512", args -> new Sha512(i18n, args), new Object[0]);
        register("Crc32", args -> new Crc32(i18n, args), new Object[0]);
        register("Md5", args -> new Md5(i18n, args), new Object[0]);

        // FileSystem
        register("CreationDate", args -> new CreationDate(i18n, args), new Object[0]);
        register("ModifyDate", args -> new ModifyDate(i18n, args), new Object[0]);
        register("FileOwner", args -> new FileOwner(i18n, args), new Object[0]);
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
