package me.sirimperivm.spigot.nms.v1_20_R1;

import me.sirimperivm.spigot.nms.NmsOperationException;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;

import java.lang.reflect.Method;

@SuppressWarnings("all")
public abstract class RegistryUtils {

    private static RegistryAccess getRegistryAccess() {
        final DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        return server.registryAccess();
    }

    /**
     * Overrides the value of a holder in a registry.
     *
     * @param <T> the type of the holder
     * @param registryKey the key for the registry which contains the holder
     * @param holderKey the key for the holder
     * @param mapper the mapper, which returns the override value based on the original value
     * @throws NmsOperationException if the operation fails
     */
    public static <T> void overrideHolder(
            ResourceKey<Registry<T>> registryKey,
            ResourceKey<T> holderKey,
            Mapper<T> mapper
    ) throws NmsOperationException {
        final Registry<T> registry = getRegistryAccess().registry(registryKey)
                .orElseThrow(() -> new NmsOperationException("Missing registry: " + registryKey));
        final Holder.Reference<T> holder = registry.getHolder(holderKey)
                .orElseThrow(() -> new NmsOperationException("Missing holder: " + registryKey));

        // Starting from Minecraft 1.19.3, WritableRegistry#registerOrOverride is no longer available.
        // Previously, it was used to mutate/override the registry when worldgen datapacks were loaded, but now
        // with 1.19.3 it seems like everything is loaded at once, so once the worldgen registries are created,
        // they are never modified again.
        // It might be possible to somehow reimplement WritableRegistry#registerOrOverride using reflection,
        // but I don't know what side effects it would have, and there are probably many caveats and complexities.
        // As a simpler workaround, it turns out that we can get the Holder.Reference and simply invoke
        // #bindValue, so that anyone using the reference will get our replacement for NoiseRouterData.CONTINENTS.
        try {
            final Method bindValueMethod = Holder.Reference.class.getDeclaredMethod("b", Object.class);
            bindValueMethod.setAccessible(true);
            bindValueMethod.invoke(holder, mapper.map(holder.value()));
        } catch (ReflectiveOperationException e) {
            throw new NmsOperationException(e);
        }
    }

    @FunctionalInterface
    public interface Mapper<T> {
        T map(T original) throws NmsOperationException;
    }

}
