package nadiendev.exdeorumkjs.material;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;

public interface ExDeorumEvents {
    EventGroup GROUP = EventGroup.of("ExDeorumEvents");

    TargetedEventHandler<String> REGISTRY = GROUP.startup("registry", () -> MaterialRegistryKubeEvent.class).requiredTarget(EventTargetType.STRING);
}
