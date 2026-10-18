package fr.rolenametag;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DonutSMP shows names with "text display" entities (big name + money).
 * We insert our role icon right before the name INSIDE that text.
 */
public final class ServerTagRoles {
    private ServerTagRoles() {}

    public record Match(AbstractClientPlayerEntity player, int index) {}

    /** The player this text is the nametag of (within 6 blocks), or null. */
    public static Match match(Entity display, Text original) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || original == null) return null;
            String plain = original.getString();
            if (plain.isEmpty()) return null;

            AbstractClientPlayerEntity best = null;
            int bestIndex = -1;
            double bestDist = 36.0;
            for (AbstractClientPlayerEntity p : mc.world.getPlayers()) {
                String name = p.getName().getString();
                Matcher m = Pattern.compile("(?<![A-Za-z0-9_])" + Pattern.quote(name) + "(?![A-Za-z0-9_])").matcher(plain);
                if (!m.find()) continue;
                double d = p.squaredDistanceTo(display.getX(), display.getY(), display.getZ());
                if (d < bestDist) { bestDist = d; best = p; bestIndex = m.start(); }
            }
            return best == null ? null : new Match(best, bestIndex);
        } catch (Throwable t) {
            return null;
        }
    }

    /** @return the modified text, or null if there is nothing to change. */
    public static Text process(Entity display, Text original) {
        try {
            RoleConfig cfg = RoleConfig.INSTANCE;
            if (!cfg.enabled) return null;
            Match m = match(display, original);
            if (m == null) return null;

            Role role = RoleDetector.detect(m.player().getUuid(), m.player().getName().getString());
            if (role == null) return null;

            boolean self = m.player() == MinecraftClient.getInstance().player;
            MutableText icon = cfg.iconFor(role, self);
            if (icon == null) return null;
            return insertAt(original, m.index(), icon);
        } catch (Throwable t) {
            return null; // never break rendering
        }
    }

    private static Text insertAt(Text original, int index, Text insert) {
        MutableText out = Text.empty();
        int[] pos = {0};
        boolean[] done = {false};
        original.visit((Style style, String s) -> {
            int len = s.length();
            if (!done[0] && index >= pos[0] && index <= pos[0] + len) {
                int cut = index - pos[0];
                if (cut > 0) out.append(Text.literal(s.substring(0, cut)).setStyle(style));
                out.append(insert);
                if (cut < len) out.append(Text.literal(s.substring(cut)).setStyle(style));
                done[0] = true;
            } else if (!s.isEmpty()) {
                out.append(Text.literal(s).setStyle(style));
            }
            pos[0] += len;
            return java.util.Optional.empty();
        }, Style.EMPTY);
        return out;
    }
}
