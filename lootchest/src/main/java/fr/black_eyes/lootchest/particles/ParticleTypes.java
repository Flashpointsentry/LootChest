package fr.black_eyes.lootchest.particles;

import fr.black_eyes.lootchest.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

final class ParticleTypes {

    static final Class<?> BLOCK_DATA_CLASS = FastReflection.optionalClass("org.bukkit.block.data.BlockData").orElse(null);
    static final Class<?> DUST_OPTIONS_CLASS = FastReflection.optionalClass("org.bukkit.Particle$DustOptions").orElse(null);
    static final Class<?> DUST_TRANSITION_CLASS = FastReflection.optionalClass("org.bukkit.Particle$DustTransition").orElse(null);
    static final Map<Class<?>, Class<?>> DATA_ADAPTERS = new HashMap<>();
    static final boolean LEGACY = !FastReflection.optionalClass("org.bukkit.Particle").isPresent();


    static {
        DATA_ADAPTERS.put(MaterialData.class, ParticleData.BlockData.class);
        DATA_ADAPTERS.put(Color.class, ParticleData.DustOptions.class);
        if (BLOCK_DATA_CLASS != null) {
            DATA_ADAPTERS.put(BLOCK_DATA_CLASS, ParticleData.BlockData.class);
        }
        if (DUST_OPTIONS_CLASS != null) {
            DATA_ADAPTERS.put(DUST_OPTIONS_CLASS, ParticleData.DustOptions.class);
        }
        if (DUST_TRANSITION_CLASS != null) {
            DATA_ADAPTERS.put(DUST_TRANSITION_CLASS, ParticleData.DustTransition.class);
        }
    }

    private ParticleTypes() {
        throw new UnsupportedOperationException();
    }

    static ParticleType of(String name) {
        Objects.requireNonNull(name, "name");

        try {
            if (LEGACY) {
                if(!LegacyParticleType.isInitialized()) LegacyParticleType.init();
                return LegacyParticleType.of(name.toUpperCase(Locale.ROOT));
            }

            return new DefaultParticleType(Particle.valueOf(name.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException | NullPointerException | ExceptionInInitializerError e) {
            Main.getInstance().getLogger().warning("Unknown particle: " + name + " for server version " + Bukkit.getServer().getVersion());
            return null;
        }
    }

    static double color(double color) {
        return color / 255.0;
    }

    static final class DefaultParticleType implements ParticleType {

        private final Particle particle;

        public DefaultParticleType(Particle particle) {
            this.particle = Objects.requireNonNull(particle, "particle");
        }

        @Override
        public String getName() {
            return this.particle.name();
        }

        @Override
        public Class<?> getRawDataType() {
            return this.particle.getDataType();
        }

        @Override
        public <T> void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            Object newData = mapData(data);

            if (newData instanceof Color) {
                Color color = (Color) newData;
                count = 0;
                offsetX = color(color.getRed());
                offsetY = color(color.getGreen());
                offsetZ = color(color.getBlue());
                extra = 1.0;
                newData = null;
            }

            world.spawnParticle(this.particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, newData);
        }

        @Override
        public <T> void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            Object newData = mapData(data);

            if (newData instanceof Color) {
                Color color = (Color) newData;
                count = 0;
                offsetX = color(color.getRed());
                offsetY = color(color.getGreen());
                offsetZ = color(color.getBlue());
                extra = 1.0;
                newData = null;
            }

            player.spawnParticle(this.particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, newData);
        }

        @SuppressWarnings("removal")
        private Object mapData(Object data) {
            Class<?> dataType = this.particle.getDataType();

            if (data instanceof ParticleData.AbstractParticleData) {
                data = ((ParticleData.AbstractParticleData) data).data;
            }

            if (dataType == DUST_OPTIONS_CLASS && data instanceof Color) {
                    return new Particle.DustOptions((Color) data, 1);
                }


            if (dataType == BLOCK_DATA_CLASS && data instanceof MaterialData) {
                return Bukkit.createBlockData(((MaterialData) data).getItemType());
            }

            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof DefaultParticleType)) {
                return false;
            }
            DefaultParticleType particleType = (DefaultParticleType) o;
            return this.particle == particleType.particle;
        }

        @Override
        public int hashCode() {
            return this.particle.hashCode();
        }

        @Override
        public String toString() {
            return "DefaultParticleType{particle=" + this.particle + '}';
        }
    }
}
