package me.foxils.rezineSMP.utilities;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class EntityAttributes {

    private static Plugin plugin;

    private EntityAttributes() {}

    public static void initEntityAttributes(Plugin plugin) {
        if (EntityAttributes.plugin != null) {
            throw new IllegalStateException("EntityAttributes initialized twice");
        }

        EntityAttributes.plugin = plugin;
    }

    private static final Map<EntityType, Map<Attribute, Double>> ENTITYTYPE_TO_ATTRIBUTE_DEFAULT_VALUE_MAP = new HashMap<>();
    private static final Map<Attribute, Double> PLAYER_ATTRIBUTE_DEFAULT_VALUE_MAP = new HashMap<>();

    public static void setEntityAttributeBaseValueForDuration(@NotNull Entity entity, @NotNull Attribute attribute, @NotNull Double baseValue, @NotNull Long durationInTicks) {
        setEntityAttributeBaseValue(entity, attribute, baseValue);

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!setEntityAttributeBaseValueToDefault(entity, attribute)) {
                spigotThrow("Could not return " + entity.getName() + "'s attributes to default value", entity.getServer().getLogger());
            }
        }, durationInTicks);
    }

    public static boolean setEntityAttributeBaseValueToDefault(@NotNull Entity entity, @NotNull Attribute attribute) {
        final EntityType entityType = entity.getType();

        if (entityType == EntityType.PLAYER) {
            return setEntityAttributeBaseValue(entity, attribute, PLAYER_ATTRIBUTE_DEFAULT_VALUE_MAP.get(attribute));
        }

        Map<Attribute, Double> attributeToDefaultValueMap = ENTITYTYPE_TO_ATTRIBUTE_DEFAULT_VALUE_MAP.get(entityType);

        if (attributeToDefaultValueMap == null) {
            ENTITYTYPE_TO_ATTRIBUTE_DEFAULT_VALUE_MAP.putIfAbsent(entityType, new HashMap<>());

            attributeToDefaultValueMap = ENTITYTYPE_TO_ATTRIBUTE_DEFAULT_VALUE_MAP.get(entityType);
        }

        Double defaultValueOfAttribute = attributeToDefaultValueMap.get(attribute);

        if (defaultValueOfAttribute == null) {
            defaultValueOfAttribute = getDefaultAttributeValueOfEntityType(entity, attribute);

            attributeToDefaultValueMap.putIfAbsent(attribute, defaultValueOfAttribute);
        }

        return setEntityAttributeBaseValue(entity, attribute, defaultValueOfAttribute);
    }

    public static boolean setEntityAttributeBaseValue(@NotNull Entity entity, @NotNull Attribute attribute, @NotNull Double baseValue) {
        final AttributeInstance attributeInstanceOfAttribute = EntityAttributes.getEntityAttributeInstance(entity, attribute);

        if (attributeInstanceOfAttribute == null) {
            spigotThrow(entity.getName() + " does not have Attribute " + attribute.name(), entity.getServer().getLogger());
            return false;
        }

        if (entity instanceof Player) {
            PLAYER_ATTRIBUTE_DEFAULT_VALUE_MAP.putIfAbsent(attribute, attributeInstanceOfAttribute.getBaseValue());
        }

        attributeInstanceOfAttribute.setBaseValue(baseValue);

        return true;
    }

    private static double getDefaultAttributeValueOfEntityType(@NotNull Entity entity, @NotNull Attribute attribute) {
        final Class<? extends Entity> entityClass = entity.getType().getEntityClass();

        if (entityClass == null) {
            spigotThrow(entity.getName() + " has no class", entity.getServer().getLogger());
            return 0;
        }

        final Entity entityOfEntityProvidedClass = entity.getWorld().spawn(entity.getLocation(), entityClass);

        final double defaultValue = getEntityAttributeBaseValue(entityOfEntityProvidedClass, attribute);

        entityOfEntityProvidedClass.remove();

        return defaultValue;
    }

    public static double getEntityAttributeBaseValue(@NotNull Entity entity, @NotNull Attribute attribute) {
        final AttributeInstance attributeInstanceOfAttribute = EntityAttributes.getEntityAttributeInstance(entity, attribute);

        if (attributeInstanceOfAttribute == null) {
            return 0;
        }

        return attributeInstanceOfAttribute.getBaseValue();
    }

    @Nullable
    public static AttributeInstance getEntityAttributeInstance(@NotNull Entity entity, @NotNull Attribute attribute) {
        if (!(entity instanceof Attributable attributableEntity)) {
            spigotThrow(entity.getName() + " is not an instance of Attributable", entity.getServer().getLogger());
            return null;
        }

        return attributableEntity.getAttribute(attribute);
    }

    private static void spigotThrow(String message, Logger serverLogger) {
        serverLogger.severe(message);
        final IllegalArgumentException e = new IllegalArgumentException();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            serverLogger.severe(stackTraceElement.toString());
        }
    }

}
