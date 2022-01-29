package Util;

/**
 * Coordinates for stack frame, each coordinate has a depth and a slot,
 * that tell how much in the frames to climb and which slot of the frame to access.
 */
public class Coordinates {
    
    private int depth;
    private String slot;

    public Coordinates(int d, String s) {
        depth = d;
        slot = s;
    }

    public int getDepth() {
        return depth;
    }

    public String getSlot() {
        return slot;
    }

}
