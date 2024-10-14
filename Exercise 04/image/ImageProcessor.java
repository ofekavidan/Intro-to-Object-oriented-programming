package image;

import java.awt.*;
import java.util.Arrays;

/**
 * Provides methods for processing images, such as padding, partitioning, and calculating brightness.
 * This class contains static methods and cannot be instantiated - utility class.
 * @author Ofek Avidan, Roei Dahuki
 */
public class ImageProcessor {

    private static final double RED_FACTOR = 0.2126d;
    private static final double GREEN_FACTOR = 0.7152d;
    private static final double BLUE_FACTOR = 0.0722d;
    private static final int MAX_RGB_SCORE = 255;
    private static final int BINARY_BASE = 2;

    /*
     * a private-empty constructor.
     * we chose to make it private such that other classes would not create an instance of it, that is make it
     * utility class.
     */
    private ImageProcessor() {
    }


    /**
     * Retrieves a padded version of the input image to the closest power of two dimensions.
     * @param image The original image to pad.
     * @return A new Image object representing the padded image.
     */
    public static Image getPaddedImage(Image image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int paddedHeight = getClosestPowerOfTwo(height);
        int paddedWidth = getClosestPowerOfTwo(width);
        int heightAddition = (paddedHeight - height) / BINARY_BASE;  // always divided by 2
        int widthAddition = (paddedWidth - width) / BINARY_BASE;  // always divided by 2

        Color[][] paddedImagePixels = new Color[paddedHeight][paddedWidth];
        for (int row = 0; row < paddedHeight; row++) {
            for (int col = 0; col < paddedWidth; col++) {
                if (isNewPixel(row, col, paddedHeight, paddedWidth, heightAddition, widthAddition)) {
                    paddedImagePixels[row][col] = new Color(MAX_RGB_SCORE, MAX_RGB_SCORE, MAX_RGB_SCORE);
                } else {
                    paddedImagePixels[row][col] = image.getPixel(row - heightAddition,
                            col - widthAddition);
                }
            }
        }
        return new Image(paddedImagePixels, paddedWidth, paddedHeight);
    }

    /*
     * Checks if the specified pixel coordinates correspond to a newly added pixel during padding.
     * @param row The row index of the pixel.
     * @param col The column index of the pixel.
     * @param newHeight The height of the padded image.
     * @param newWidth The width of the padded image.
     * @param heightAddition The number of rows added during padding.
     * @param widthAddition The number of columns added during padding.
     * @return true if the pixel is newly added during padding, false otherwise.
     */
    private static boolean isNewPixel(int row, int col,
                                      int newHeight, int newWidth,
                                      int heightAddition,  int widthAddition) {
        return (row < heightAddition || (newHeight - heightAddition) <= row) ||
                (col < widthAddition || (newWidth - widthAddition) <= col);
    }

    /*
     * Finds the closest power of two greater than or equal to the given number.
     * @param n The input number.
     * @return The closest power of two greater than or equal to n.
     */
    private static int getClosestPowerOfTwo(int n) {
        int powerOfTwo = BINARY_BASE;
        while (powerOfTwo < n) {
            powerOfTwo = BINARY_BASE * powerOfTwo;
        }
        return powerOfTwo;
    }

    /**
     * Partitions the input image into sub-images of equal size.
     * @param image The original image to partition.
     * @param partitionHeight The number of rows in the partition grid.
     * @param partitionWidth The number of columns in the partition grid.
     * @return A 2D array of Image objects representing the partitioned sub-images.
     */
    public static Image[][] getImagePartition(Image image, int partitionHeight, int partitionWidth) {
        int subImageSize = image.getWidth() / partitionWidth;
        Image[][] subImages = new Image[partitionHeight][partitionWidth];
        for (int i = 0; i < partitionHeight; i++) {
            for (int j = 0; j < partitionWidth; j++) {
                subImages[i][j] = new Image(
                        getSubImagePixels(image, subImageSize, i, j),
                        subImageSize, subImageSize
                );
            }
        }
        return subImages;
    }

    /*
     * Extracts the pixels of a sub-image from the given image.
     * @param image The original image.
     * @param subImageSize The size of the sub-image (both width and height).
     * @param i The row index of the sub-image.
     * @param j The column index of the sub-image.
     * @return A 2D array of Color representing the pixels of the sub-image.
     */
    private static Color[][] getSubImagePixels(Image image, int subImageSize, int i, int j) {
        Color[][] subImagePixels = new Color[subImageSize][subImageSize];
        for (int row = 0; row < subImageSize; row++) {
            for (int col = 0; col < subImageSize; col++) {
                subImagePixels[row][col] =
                        image.getPixel(i * subImageSize + row, j * subImageSize + col);
            }
        }
        return subImagePixels;
    }

    /**
     * Calculates the brightness of the input image.
     * @param image The image for which to calculate brightness.
     * @return The brightness value normalized to the range [0, 1].
     */
    public static double getImageBrightness(Image image) {
        int height = image.getHeight();
        int width = image.getWidth();
        double[][] greyPixels = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = image.getPixel(row, col);
                greyPixels[row][col] =
                        color.getRed() * RED_FACTOR +
                        color.getGreen() * GREEN_FACTOR +
                        color.getBlue() * BLUE_FACTOR;
            }
        }
        return (Arrays.stream(greyPixels).flatMapToDouble(Arrays::stream).sum()) /
                (double) (height * width * MAX_RGB_SCORE);
    }

}
