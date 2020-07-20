package Mathematician.NPCSync.NPC;

public enum NPCAction {

    STILL((byte) 0x00),
    ON_FIRE((byte) 0x01),
    CROUCHED((byte) 0x02),
    SPRINTING((byte) 0x08),
    SWIMMING((byte) 0x10),
    INVISIBLE((byte) 0x20),
    GLOWING((byte) 0x40),
    FLYING((byte) 0x80);

    private byte value;

    NPCAction(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static byte getOverallValue(NPCAction[] actions){
        byte sum = (byte) 0;
        for (NPCAction action : actions) {
            sum = (byte) (sum | action.getValue());
        }
        return sum;
    }
}
