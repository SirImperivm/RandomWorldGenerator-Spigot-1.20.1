package me.sirimperivm.spigot.nms;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.bukkit.Bukkit;

/**
 * Factory class used to create {@link NmsAdapter}s.
 */
@SuppressWarnings("all")
public final class NmsAdapterFactory {

    /**
     * Creates an {@link NmsAdapter} for the running server version.
     *
     * @return the adapter
     * @throws CreateNmsAdapterException if the adapter can't be created
     */
    public static NmsAdapter create() throws CreateNmsAdapterException {
        final String nmsVersion = getNmsVersion();
        if (nmsVersion == null) {
            throw new CreateNmsAdapterException("Unable to find CraftBukkit version");
        }
        try {
            Class<?> adapterImplClass = loadAdapterImplClass(nmsVersion);
            return (NmsAdapter) adapterImplClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError | SecurityException
                 | IllegalArgumentException | ClassCastException e) {
            throw new CreateNmsAdapterException(
                    "Unable to create adapter for NMS version " + nmsVersion, e);
        }
    }

    /**
     * Loads and returns the adapter implementation class for a given NMS version.
     *
     * @param nmsVersion the NMS version string
     * @return the adapter implementation class
     * @throws ClassNotFoundException if the class cannot be located
     * @throws LinkageError if the class cannot be linked
     */
    private static Class<?> loadAdapterImplClass(String nmsVersion) throws ClassNotFoundException, LinkageError {
        final String adapterPackageName = NmsAdapter.class.getPackage().getName();
        final String adapterImplName = NmsAdapter.class.getSimpleName() + "Impl";
        return Class.forName(String.join(".", adapterPackageName, nmsVersion, adapterImplName));
    }

    /**
     * Gets the NMS version of the running server, such as {@code v1_18_R2}.
     *
     * @return the NMS version, or {@code null} if not found
     */
    private static String getNmsVersion() {
        // On Spigot, and on Paper for Minecraft <= 1.20.4, the server implementation class is located in a package named after the NMS version.
        // On Paper for Minecraft 1.20.5+, we can get the NMS version from CraftBukkit-Package-Version in the server JAR manifest.
        final String qualifiedNmsPackageName = Bukkit.getServer().getClass().getPackage().getName();
        final String nmsPackageName = qualifiedNmsPackageName.substring(qualifiedNmsPackageName.lastIndexOf('.') + 1);
        if (nmsPackageName.startsWith("v") && nmsPackageName.contains("_R")) {
            return nmsPackageName;
        } else {
            try {
                Enumeration<URL> manifestUrls = Bukkit.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
                while (manifestUrls.hasMoreElements()) {
                    URL manifestUrl = manifestUrls.nextElement();
                    Manifest manifest = new Manifest(manifestUrl.openStream());
                    String cbPackageVersion = manifest.getMainAttributes().getValue("CraftBukkit-Package-Version");
                    if (cbPackageVersion != null) {
                        return cbPackageVersion;
                    }
                }
                return null;
            } catch (IOException e) {
                return null;
            }
        }
    }

}
