package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.Random;


/**
 * The Terrain class represents the ground terrain in the game world.
 * It generates and manages the terrain blocks within the specified range.
 */
public class Terrain {
    private static final String BLOCKS_TAG = "ground";

    private static final float HEIGHT_AT_X0_FACTOR = 2f / 3f;
    private static final float NOISE_FACTOR = 7 * Block.SIZE;
    private static final Color BASIC_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    private final float groundHeightAtX0;  // the initial value to start generating ground's heights from
    private final NoiseGenerator noiseGenerator;


    /**
     * Constructs a Terrain object with the specified window dimensions and seed.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for generating pseudo-random noise.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = HEIGHT_AT_X0_FACTOR * windowDimensions.y();
        noiseGenerator = new NoiseGenerator(new Random().nextDouble(), (int) groundHeightAtX0);
    }

    /**
     * Returns the height of the ground at the specified x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }


    /**
     * Creates a list of blocks within the specified X range, including the given range limits.
     * It ensures that the loop covers all values up to and including maxX.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return A list of terrain blocks within the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        // find the rounded minX, maxX for padding x-limits to be multiples of Block.SIZE:
        int roundedMinX = Math.floorDiv(minX, Block.SIZE) * Block.SIZE;  // <= minX
        int roundedMaxX = ((int) Math.ceil(((double) maxX) / ((double) Block.SIZE))) * Block.SIZE; // >= maxX
        for (int x = roundedMinX; x <= roundedMaxX; x += Block.SIZE) {
            // find the rounded groundHeightAt(x) for padding y-limits to be multiples of Block.SIZE:
            int roundedMinY = (int) Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            int roundedMaxY = (roundedMinY + (TERRAIN_DEPTH * Block.SIZE));
            for (int y = roundedMinY; y < roundedMaxY; y += Block.SIZE) {
                addNewBlock(x, y, blocks);
            }
        }
        return blocks;
    }


    /*
     * Adds a new block to the list of blocks.
     *
     * @param x      The x-coordinate of the block.
     * @param y      The y-coordinate of the block.
     * @param blocks The list of blocks to which the new block will be added.
     */
    private static void addNewBlock(float x, float y, List<Block> blocks) {
        Block block = new Block(
                Vector2.of(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(BASIC_COLOR))
        );
        blocks.add(block);
        block.setTag(BLOCKS_TAG);
    }
}
