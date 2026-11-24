package nadiendev.exdeorumkjs.material;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public interface ExDeorumEvents {
    EventGroup GROUP = EventGroup.of("ExDeorumEvents");

    EventHandler REGISTRY = GROUP.startup("registry", () -> MaterialRegistryKubeEvent.class).extra(Extra.REQUIRES_STRING);
}
