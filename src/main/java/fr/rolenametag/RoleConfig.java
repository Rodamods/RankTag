package fr.rolenametag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Config: .minecraft/config/rolenametag.json (editable in game through the config screen). */
public final class RoleConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("rolenametag.json");
    public static RoleConfig INSTANCE = new RoleConfig();

    /** Master switch. */
    public boolean enabled = true;

    // --- My role (shown before my name, only if the TAB counts me in that role) ---
    public boolean showOwnRole = true;
    public boolean ownStar = true;      // if I am Mod / Admin
    public boolean ownCamera = true;    // if I am Media
    public boolean ownPlus = true;      // cyan plus after the camera, if I am Media
    public boolean ownDiamond = true;   // if I am a member
    /** Show my own nametag in third person. */
    public boolean showOnSelf = true;

    // --- Other players: only the members' blue diamond ---
    public boolean showMember = true;

    /** Default role when the player is in the TAB with no recognised role. */
    public String defaultRole = "MEMBER";

    /** Server icons (hex) -> role. Learned automatically. */
    public Map<String, String> glyphRoles = new LinkedHashMap<>();

    public List<String> mediaKeywords = new ArrayList<>(List.of("media", "youtube", "youtuber", "streamer", "tiktok"));
    public List<String> staffKeywords = new ArrayList<>(List.of(
            "owner", "admin", "mod", "moderator", "helper", "dev", "developer", "staff", "manager", "support"));

    /**
     * Icon to place before the name, or null.
     *  - me: depending on my role in the TAB (star / camera + plus / diamond) and the ticked options;
     *  - others: only the members' blue diamond.
     */
    public MutableText iconFor(Role role, boolean self) {
        if (!enabled) return null;
        StringBuilder sb = new StringBuilder();
        if (self) {
            if (!showOwnRole) return null;
            switch (role) {
                case MEDIA -> { if (ownCamera) sb.append(Role.CAMERA); if (ownPlus) sb.append(Role.PLUS); }
                case STAFF -> { if (ownStar) sb.append(Role.STAR); }
                case MEMBER -> { if (ownDiamond) sb.append(Role.DIAMOND); }
            }
        } else if (role == Role.MEMBER && showMember) {
            sb.append(Role.DIAMOND);
        }
        return sb.isEmpty() ? null : Role.glyph(sb.toString());
    }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                RoleConfig c = GSON.fromJson(Files.readString(FILE), RoleConfig.class);
                if (c != null) INSTANCE = c;
            }
            save();
        } catch (Exception e) {
            System.err.println("[rolenametag] unreadable config, using defaults: " + e);
        }
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            System.err.println("[rolenametag] could not write the config: " + e);
        }
    }

    public Role defaultRoleEnum() {
        try { return Role.valueOf(defaultRole.toUpperCase(Locale.ROOT)); }
        catch (Exception e) { return Role.MEMBER; }
    }
}
