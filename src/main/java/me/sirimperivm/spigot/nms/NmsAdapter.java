package me.sirimperivm.spigot.nms;

import org.bukkit.scoreboard.Score;

/**
 * Adapter for NMS operations, implemented using internal server classes.
 */

@SuppressWarnings("all")
public interface NmsAdapter {

    /**
     * Removes oceans from the world generation.
     * <p>
     *     This method is NOT safe to call during world generation.
     * </p>
     *
     * @throws NmsOperationException if the operation fails
     */
    void removeOceans() throws NmsOperationException;

    /**
     * Sets the value of a player Score even if it is read-only. This can be used to initialize
     * read-only player scores without the need to mutate the underlying value (e.g. health).
     *
     * @see <a href="https://bugs.mojang.com/browse/MC-111729">MC-111729</a>
     *
     * @param playerScore the player Score to set
     * @param value the value
     */
    default void setReadOnlyPlayerScore(Score playerScore, int value) {
        // Works on Minecraft 1.20.2 and below, before the fix of https://bugs.mojang.com/browse/MC-163943.
        playerScore.setScore(value);
    }

}
