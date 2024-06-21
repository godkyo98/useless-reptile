package nordmods.uselessreptile.common.util.dragon_spawn;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import nordmods.uselessreptile.UselessReptile;

import java.util.Map;

public class DragonSpawnReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public DragonSpawnReloadListener() {
        super(new GsonBuilder().create(), "dragon_spawns");
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DragonSpawnReloadListener());
    }

    @Override
    public Identifier getFabricId() {
        return UselessReptile.id("dragon_spawns");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        DragonSpawn.clearSpawns();
        for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
            String path = entry.getKey().getPath();

            String dragon = path.substring(0, path.indexOf("/"));
            JsonElement element = entry.getValue();
            DragonSpawn data = DragonSpawn.deserialize(element.getAsJsonObject());
            DragonSpawn.addSpawn(dragon, data);
        }
        DragonSpawn.debugPrint();
    }
}
