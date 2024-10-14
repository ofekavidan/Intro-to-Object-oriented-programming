package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Represents a class responsible for generating trees within a specified range.
 */
public class Flora {

    private static final int BASIC_HEIGHT_IN_BLOCKS = 8;
    private static final int MAX_HEIGHT_NOISE_IN_BLOCKS = 3;
    private static final float PLANTING_BIASED_LIMIT = 0.1f;
    private static final float FRUIT_ENERGY_BOOST = 10f;

    private final Function<Float, Float> groundHeightAt;
    private final Vector2 occupiedXLimits;
    private final Consumer<Float> addEnergy;
    private final float cycleLength;
    private final Random random = new Random();

    /**
     * Constructs a Flora object with the specified functions and parameters.
     *
     * @param groundHeightAt  The function to get the ground height at a specific X position.
     * @param occupiedXLimits The limits of occupied X positions where flora can be generated.
     * @param addEnergy       The consumer function to add energy to the avatar.
     * @param cycleLength     The length of the cycle for regeneration of flora.
     */
    public Flora(Function<Float, Float> groundHeightAt, Vector2 occupiedXLimits,
                 Consumer<Float> addEnergy, float cycleLength) {
        this.groundHeightAt = groundHeightAt;
        this.occupiedXLimits = occupiedXLimits;
        this.addEnergy = addEnergy;
        this.cycleLength = cycleLength;
    }

    /**
     * Creates a list of trees within the specified X range, including the given range limits.
     * It ensures that the loop covers all values in the given range.
     * @param minX  The minimum X coordinate.
     * @param maxX  The maximum X coordinate.
     * @return      A list of trees generated within the specified X range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> trees = new ArrayList<>();
        int roundedMinX = Math.floorDiv(minX, Block.SIZE) * Block.SIZE;  // <= minX
        int roundedMaxX = ((int) Math.ceil(((double) maxX) / ((double) Block.SIZE))) * Block.SIZE; // >= maxX
        for (int x = roundedMinX; x <= roundedMaxX; x += Block.SIZE) {
            if (!isOccupied(x) && (random.nextFloat() <= PLANTING_BIASED_LIMIT)) {
                // if this x position isn't occupied already (by the avatar), and if it is drawn:
                int y = (int) Math.floor(
                        groundHeightAt.apply((float) x) / Block.SIZE
                ) * Block.SIZE;
                trees.add(
                        new Tree(
                                Vector2.of(x + Block.SIZE, y), getRandomHeight(),
                                () -> addEnergy.accept(FRUIT_ENERGY_BOOST), cycleLength
                        )
                );
            }
        }
        return trees;
    }

    /*
     * Checks if a given X position is occupied by the avatar.
     *
     * @param x The X position to check.
     * @return  True if the position is occupied, false otherwise.
     */
    private boolean isOccupied(int x) {
        return (occupiedXLimits.x() <= x) && ((x-Block.SIZE) <= occupiedXLimits.y());
    }

    /*
     * Generates a random height for a tree.
     *
     * @return The height of the tree.
     */
    private int getRandomHeight() {
        boolean positive = random.nextBoolean();
        int noise = random.nextInt(MAX_HEIGHT_NOISE_IN_BLOCKS);
        noise = positive ? noise : -noise;
        return (BASIC_HEIGHT_IN_BLOCKS + noise) * Block.SIZE;
    }

}
