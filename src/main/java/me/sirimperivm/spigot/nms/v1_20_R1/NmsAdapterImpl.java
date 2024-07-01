package me.sirimperivm.spigot.nms.v1_20_R1;

import me.sirimperivm.spigot.nms.NmsAdapter;
import me.sirimperivm.spigot.nms.NmsOperationException;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;

@SuppressWarnings("all")
public class NmsAdapterImpl implements NmsAdapter {

    @Override
    public void removeOceans() throws NmsOperationException {
        RegistryUtils.overrideHolder(Registries.DENSITY_FUNCTION, NoiseRouterData.CONTINENTS, continents -> {
            // The original continents DF is wrapped in a FlatCache.
            // See net.minecraft.world.level.levelgen.NoiseRouterData.bootstrap()
            final DensityFunction continentsUnwrapped;
            try {
                continentsUnwrapped = ((DensityFunctions.MarkerOrMarked) continents).wrapped();
            } catch (ClassCastException e) {
                throw new NmsOperationException(e);
            }

            // We keep the FlatCache for performance, but take the absolute value of the noise function.
            // This stops ocean generation, but keeps the qualities of the original noise.
            return DensityFunctions.flatCache(continentsUnwrapped.abs());
        });
    }

}

