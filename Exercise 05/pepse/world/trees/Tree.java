package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.Observer;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a tree in the game world.
 * uses Leaf, Trunk and Fruit classes as helper classes for creating each one of them.
 */
public class Tree implements Observer {

    private static final float MIDDLE_FACTOR = 0.5f;
    private static final int TREETOP_QUARTER_SIZE = 4;
    private static final float LEAF_BIASED_LIMIT = 0.5f;
    private static final float FRUIT_BIASED_LIMIT = 0.1f;
    private static final float LEAF_UPDATE_ANGLE = 90f;
    private static final float LEAF_UPDATE_DURATION = 1f;

    private final Block trunk;
    private final List<Block> leaves = new ArrayList<>();
    private final List<Block> fruits = new ArrayList<>();
    private final Fruit fruitsGenerator;


    /**
     * Constructs a Tree object with the specified position, height, external effect for fruits,
     * and cycle length for fruit regeneration.
     *
     * @param pos                 The position of the tree.
     * @param height              The height of the tree.
     * @param fruitsExternalEffect The external effect caused by eating the fruits of the tree.
     * @param cycleLength         The length of the cycle for regenerating fruits.
     */
    public Tree(Vector2 pos, float height, Runnable fruitsExternalEffect, float cycleLength) {
        Random random = new Random();
        trunk = Trunk.create(pos, height);
        Vector2 treetopCenter = trunk.getTopLeftCorner().add(
                Vector2.RIGHT.mult(trunk.getDimensions().x() * MIDDLE_FACTOR)
        );  // the center of the top of the trunk, around this point the treetop will be generated
        fruitsGenerator = new Fruit(fruitsExternalEffect, cycleLength);
        for (int i = -TREETOP_QUARTER_SIZE; i < TREETOP_QUARTER_SIZE; i++) {
            for (int j = -TREETOP_QUARTER_SIZE; j < TREETOP_QUARTER_SIZE; j++) {
                if (random.nextFloat() <= LEAF_BIASED_LIMIT) {  // if leaf in (i, j) is drawn to be drawn:
                    leaves.add(Leaf.create(treetopCenter, i, j, random.nextFloat()));
                } else if (random.nextFloat() <= FRUIT_BIASED_LIMIT) {  // else if fruit in (i, j) is drawn:
                    fruits.add(fruitsGenerator.create(treetopCenter, i, j));
                }
            }
        }
    }


    /**
     * Updates the tree by updating its trunk, leaves, and fruits.
     */
    @Override
    public void update() {
        updateTrunk();
        updateLeaves();
        updateFruits();
    }

    /*
     * Updates the fruits of the tree by changing their colors.
     */
    private void updateFruits() {
        fruitsGenerator.incrementColorIndex();
        for (var fruit : fruits) {
            fruit.renderer().setRenderable(fruitsGenerator.getNextColoredImage());
        }
    }

    /*
     * Updates the leaves of the tree by rotating them.
     */
    private void updateLeaves() {
        for (var leaf : leaves) {
            float currentAngle = leaf.renderer().getRenderableAngle();
            new Transition<>(
                    leaf,
                    leaf.renderer()::setRenderableAngle,
                    currentAngle,
                    currentAngle + LEAF_UPDATE_ANGLE,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    LEAF_UPDATE_DURATION,
                    Transition.TransitionType.TRANSITION_ONCE,
                    null
            );
        }
    }

    /*
     * Updates the trunk of the tree by setting its renderable image.
     */
    private void updateTrunk() {
        trunk.renderer().setRenderable(Trunk.getApproximateColoredImage());
    }

    /* Getters: */
    /**
     * Retrieves the trunk GameObject of the tree.
     * @return The trunk GameObject.
     */
    public GameObject getTrunk() {
        return trunk;
    }
    /**
     * Retrieves an iterable collection of leaves GameObjects belonging to the tree.
     * @return An iterable collection of leaves GameObjects.
     */
    public Iterable<? extends GameObject> getLeaves() {
        return leaves;
    }
    /**
     * Retrieves an iterable collection of fruit GameObjects belonging to the tree.
     * @return An iterable collection of fruit GameObjects.
     */
    public Iterable<? extends GameObject> getFruits() {
        return fruits;
    }

}
