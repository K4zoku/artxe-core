package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.AdvancedConfigPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class StringListConfigPath extends AdvancedConfigPath<List<String>, List<String>> {
    /**
     * Create a config path
     *
     * @param path the path to the value
     * @param def  the default value if it's not found
     */
    public StringListConfigPath(@NotNull String path, @Nullable List<String> def) {
        super(path, def);
    }

    @Override
    public @Nullable List<String> getFromConfig(@NotNull Config config) {
        return config.getConfig().getStringList(getPath());
    }

    @Override
    public @Nullable List<String> convert(@NotNull List<String> rawValue) {
        return Collections.unmodifiableList(rawValue);
    }

    @Override
    public @Nullable List<String> convertToRaw(@NotNull List<String> value) {
        return value;
    }
}
