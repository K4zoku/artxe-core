package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.AdvancedConfigPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectConfigPath extends AdvancedConfigPath<Object, Object> {
    /**
     * Create a config path
     *
     * @param path the path to the value
     * @param def  the default value if it's not found
     */
    public ObjectConfigPath(@NotNull String path, @Nullable Object def) {
        super(path, def);
    }

    @Override
    public @Nullable Object getFromConfig(@NotNull Config config) {
        return config.getConfig().get(getPath());
    }

    @Override
    public @Nullable Object convert(@NotNull Object rawValue) {
        return rawValue;
    }

    @Override
    public @Nullable Object convertToRaw(@NotNull Object value) {
        return value;
    }
}
