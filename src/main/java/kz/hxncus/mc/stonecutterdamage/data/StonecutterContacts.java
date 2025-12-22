package kz.hxncus.mc.stonecutterdamage.data;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * StonecutterContacts part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterContacts {

    private final Set<Player> players = new HashSet<>();

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public Set<Player> getAll() {
        return players;
    }
}
