package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import static java.lang.Math.max;

/**
 * Represents an ASCII art conversion algorithm.
 * This algorithm converts image file into ASCII art using specified ASCII characters and resolution.
 * It performs the conversion based on the provided image, resolution, and character matching strategy.
 * For any change in the image path or in the resolution - new AsciiArtAlgorithm's instance must be
 * constructed, and all the instances compose the same subImgCharMatcher, so a change it the valid characters
 * wouldn't demand new instance.
 * @author Ofek Avidan, Roei Dahuki
 */
public class AsciiArtAlgorithm {

    private final int partitionHeight;  // the number of sub-images in a column
    private final int partitionWidth;  // the number of sub-images in a row
    private final double[][] subImagesBrightnesses;  // the brightnesses of sub-images
    private final SubImgCharMatcher subImgCharMatcher;


    /**
     * Constructor for AsciiArtAlgorithm class.
     * The given image is being padded and then parts, all by the ImageProcessor. Then saves in a field the
     * brightnesses of all sub-images in the partition.
     *
     * @param imagePath          Path to the input image file.
     * @param res                Resolution of the converted image (number of characters per line).
     * @param subImgCharMatcher  Object of SubImgCharMatcher class used for character matching.
     * @throws ResolutionOutOfBoundsException  Thrown if the specified resolution is out of image bounds.
     * @throws IOException  Thrown if there's an I/O error while creating the image.
     */
    public AsciiArtAlgorithm(String imagePath, int res, SubImgCharMatcher subImgCharMatcher)
            throws ResolutionOutOfBoundsException, IOException {
        this.subImgCharMatcher = subImgCharMatcher;

        Image paddedImage = ImageProcessor.getPaddedImage(new Image(imagePath));

        // check res validity:
        this.partitionWidth = res;
        this.partitionHeight = getPartitionHeight(res, paddedImage);;

        this.subImagesBrightnesses = new double[this.partitionHeight][this.partitionWidth];
        Image[][] imagePartition = ImageProcessor.getImagePartition(paddedImage, this.partitionHeight,
                this.partitionWidth);
        for (int i = 0; i < this.partitionHeight; i++) {
            for (int j = 0; j < this.partitionWidth; j++) {
                this.subImagesBrightnesses[i][j] = ImageProcessor.getImageBrightness(imagePartition[i][j]);
            }
        }
    }


    /*
     * Calculates the partition height based on the specified resolution.
     *
     * @param res          Resolution of the converted image (number of characters per line).
     * @param paddedImage  Padded input image.
     * @return Partition height.
     * @throws ResolutionOutOfBoundsException  Thrown if the specified resolution is out of bounds.
     */

    private static int getPartitionHeight(int res, Image paddedImage) throws ResolutionOutOfBoundsException {
        int maxRes = paddedImage.getWidth();
        int minRes = max(1, paddedImage.getWidth() / paddedImage.getHeight());
        if (res > maxRes || res < minRes) {
            throw new ResolutionOutOfBoundsException(res);
        }
        // assumes (paddedImage.getWidth() | res) && (paddedImage.getHeight() | subImageSize)
        int subImageSize = paddedImage.getWidth() / res;
        return paddedImage.getHeight() / subImageSize;
    }


    /**
     * Runs the ASCII art conversion process.
     *
     * @return  Two-dimensional array of characters representing the converted ASCII art.
     */
    public char[][] run() {
        char[][] charsImage = new char[partitionHeight][partitionWidth];
        for (int i = 0; i < partitionHeight; i++) {
            for (int j = 0; j < partitionWidth; j++) {
                charsImage[i][j] = subImgCharMatcher.getCharByImageBrightness(subImagesBrightnesses[i][j]);
            }
        }
        return charsImage;
    }
}
