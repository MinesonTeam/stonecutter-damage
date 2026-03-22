package kz.hxncus.mc.stonecutterdamage.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * StonecutterContacts part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterContacts {

    private final Set<UUID> players = new HashSet<>();

    public void add(UUID uniqueId) {
        players.add(uniqueId);
    }

    public void remove(UUID uniqueId) {
        players.remove(uniqueId);
    }

    public boolean contains(UUID uniqueId) {
        return players.contains(uniqueId);
    }

    public Set<UUID> getAll() {
        return players;
    }
}
