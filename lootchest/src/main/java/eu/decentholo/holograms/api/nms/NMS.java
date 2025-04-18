package eu.decentholo.holograms.api.nms;

import eu.decentholo.holograms.api.nms.versions.NMS_1_17;
import eu.decentholo.holograms.api.nms.versions.NMS_1_8;
import eu.decentholo.holograms.api.nms.versions.NMS_1_9;
import eu.decentholo.holograms.api.utils.reflect.ReflectField;
import eu.decentholo.holograms.api.utils.reflect.ReflectMethod;
import eu.decentholo.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentholo.holograms.api.utils.reflect.Version;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public abstract class NMS {



    // SEND PACKET
    protected static final Class<?> PACKET_CLASS;
    protected static final ReflectMethod CRAFT_PLAYER_GET_HANDLE_METHOD;
    protected static final ReflectMethod PLAYER_CONNECTION_SEND_PACKET_METHOD;
    protected static ReflectField<?> ENTITY_PLAYER_CONNECTION_FIELD;


    static {
        // SEND PACKET
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;
        Class<?> craftPlayerClass;
        if (Version.afterOrEqual(17)) {
            entityPlayerClass = ReflectionUtil.getNMClass("server.level.EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMClass("server.network.PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            PACKET_CLASS = ReflectionUtil.getNMClass("network.protocol.Packet");
            // Because NMS has different names for fields in almost every version.
            if (entityPlayerClass != null) {
                for (Field field : entityPlayerClass.getDeclaredFields()) {
                    if (playerConnectionClass != null && field.getType().isAssignableFrom(playerConnectionClass)) {
                        ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, field.getName());
                        break;
                    }
                }
            }
        } else {
            entityPlayerClass = ReflectionUtil.getNMSClass("EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMSClass("PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            PACKET_CLASS = ReflectionUtil.getNMSClass("Packet");
            ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, "playerConnection");

        }
        CRAFT_PLAYER_GET_HANDLE_METHOD = new ReflectMethod(craftPlayerClass, "getHandle");
        if (Version.afterOrEqual(Version.v1_20_R2)) {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "b", PACKET_CLASS);
        } else if (Version.afterOrEqual(18)) {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "a", PACKET_CLASS);
        } else {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "sendPacket", PACKET_CLASS);
        }
    }

    @Getter
    private static NMS instance;

    public static void init() {
        if (Version.before(9)) {
            instance = new NMS_1_8();
        } else if (Version.before(17)) {
            instance = new NMS_1_9();
        } else {
            instance = new NMS_1_17();
        }
    }

    protected Object getPlayerConnection(Player player) {
        Object entityPlayer = CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
        return ENTITY_PLAYER_CONNECTION_FIELD.getValue(entityPlayer);
    }

    public void sendPacket(Player player, Object packet) {
        if (PACKET_CLASS != null && (packet == null || !PACKET_CLASS.isAssignableFrom(packet.getClass()))) return;
        Object playerConnection = getPlayerConnection(player);
        PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(playerConnection, packet);
    }


    public abstract int getFreeEntityId();

    public abstract void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable);

    public abstract void updateFakeEntityCustomName(Player player, String name, int entityId);

    public abstract void teleportFakeEntity(Player player, Location location, int entityId);

    public abstract void hideFakeEntities(Player player, int... entityIds);

}
