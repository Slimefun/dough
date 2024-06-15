package io.github.bakedlibs.dough.skins.nms;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;

import javax.annotation.ParametersAreNonnullByDefault;

public class PlayerHeadAdapterPaper implements PlayerHeadAdapter {

    @Override
    @ParametersAreNonnullByDefault
    public void setGameProfile(Block block, GameProfile profile, boolean sendBlockUpdate) {
        BlockState state = block.getState();
        if (!(state instanceof Skull)) return;

        Skull skull = (Skull) state;

        Property property = profile.getProperties().get("textures").iterator().next();

        PlayerProfile paperPlayerProfile = Bukkit.createProfile(profile.getId(), profile.getName());
        paperPlayerProfile.setProperty(new ProfileProperty(property.name(), property.value(), property.signature()));

        skull.setPlayerProfile(paperPlayerProfile);

        if (sendBlockUpdate) {
            skull.update(true, false);
        }
    }
}
