package org.ezapi.chat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class MultiPlaceholderProvider implements PlaceholderProvider {

    private final List<PlaceholderProvider> placeholderProviders = new ArrayList<>();

    public MultiPlaceholderProvider(PlaceholderProvider... placeholderProviders) {
        this(Arrays.asList(placeholderProviders));
    }

    public MultiPlaceholderProvider(Collection<PlaceholderProvider> placeholderProviders) {
        if (placeholderProviders.size() == 0) return;
        for (PlaceholderProvider placeholderProvider : placeholderProviders) {
            if (placeholderProvider != this) {
                this.placeholderProviders.add(placeholderProvider);
            }
        }
    }

    @Override
    public String setPlaceholder(String message) {
        if (this.placeholderProviders.size() > 0) {
            for (PlaceholderProvider placeholderProvider : placeholderProviders) {
                message = placeholderProvider.setPlaceholder(message);
            }
        }
        return message;
    }

    @Override
    public String setPlaceholder(String message, Player player) {
        if (this.placeholderProviders.size() > 0) {
            for (PlaceholderProvider placeholderProvider : placeholderProviders) {
                message = placeholderProvider.setPlaceholder(message, player);
            }
        }
        return message;
    }

}
