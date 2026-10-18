package fr.rolenametag;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum Role {
    MEMBER(2),   // no role in the TAB
    MEDIA(1),    // camera in the TAB
    STAFF(3);    // star in the TAB (mod / admin)

    public static final String CAMERA = "\uE000";
    public static final String PLUS = "\uE001";
    public static final String DIAMOND = "\uE002";
    public static final String STAR = "\uE003";
    public static final String LOGO = "\uE010"; // RankTag logo used in the config screen

    private static final Identifier FONT = Identifier.of("rolenametag", "icons");

    public final int priority;

    Role(int priority) { this.priority = priority; }

    /** Text using the icon font (also used for the previews in the screen). */
    public static MutableText glyph(String glyphs) {
        return Text.literal(glyphs).setStyle(Style.EMPTY.withFont(new StyleSpriteSource.Font(FONT)).withColor(0xFFFFFF));
    }
}
