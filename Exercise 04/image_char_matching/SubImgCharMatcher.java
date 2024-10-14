package image_char_matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * A class for matching characters to subimages based on brightness.
 * This class maintains a set of characters and their corresponding brightness values.
 * Characters can be added to or removed from the set, and the closest matching character
 * to a given brightness value can be retrieved.
 *
 * @author Ofek Avidan, Roei Dahuki
 */
public class SubImgCharMatcher {

    private TreeMap<Double, Character> brightnessToChar = new TreeMap<>();  // normalized
    private final HashMap<Character, Double> charsToBrightness = new HashMap<>();  // un-normalized
    private boolean charsetChanged = false;  // true iff the charset has been changed from last normalization

    /**
     * A constructor that receives as a parameter an array of characters that will make up the set of
     * characters for the algorithm to use.
     *
     * @param charset an array of legal chars for matching
     */
    public SubImgCharMatcher(char[] charset) {
        for (char c : charset) {
            addChar(c);
        }
    }

    /**
     * Adds a character to the character set and updates the brightness value map.
     * If the character is successfully added, sets charsetChanged flag to true.
     *
     * @param c the character to be added
     */
    public void addChar(char c) {
        double brightness = getCharBrightness(c);
        if (charsToBrightness.put(c, brightness) == null) {
            charsetChanged = true;
        }
    }

    /*
     * This method gets a char and calculates its brightness.
     * @param c - the char that we want to calculate its brightness
     * @return the brightness of the given char
     */
    private static double getCharBrightness(char c) {
        // First, convert the character into a two-dimensional Boolean array, representing it as a
        // black&white image, using the provided convertToBoolArray.CharConverter method:
        boolean[][] blackWhiteCharImage = CharConverter.convertToBoolArray(c);
        // Then count the values of the true pixels - that is the white pixels:
        int totalPixelsCounter = 0;
        int whitePixelsCounter = 0;
        for (boolean[] row : blackWhiteCharImage) {
            for (boolean pixel : row) {
                totalPixelsCounter++;
                if (pixel) {
                    whitePixelsCounter++;
                }
            }
        }
        // Then normalize the brightness by the total number of pixels in the array, in order to get a number
        // between 0-1 that represents the brightness level of the character:
        return (double) whitePixelsCounter / (double) totalPixelsCounter;
    }

    /**
     * A method that removes the character c from the character set.
     *
     * @param c - the char to remove
     */
    public void removeChar(char c) {
        Double brightness = charsToBrightness.get(c);
        if (brightness != null) {
            charsToBrightness.remove(c);
            charsetChanged = true;
        }
    }

    /**
     * Given a brightness value of a subimage, the method will return the character from the set of characters
     * with the closest absolute value to the given brightness.
     * Important note: if there are several characters from the set with the same brightness,
     * the character with the lowest ASCII value among them will be returned.
     * in short, Retrieves the character corresponding to the given brightness value.
     *
     * @param brightness the brightness value of the character
     * @return the character corresponding to the given brightness value
     */
    public char getCharByImageBrightness(double brightness) {
        if (charsetChanged) {
            double minBrightness = Collections.min(charsToBrightness.values());
            double maxBrightness = Collections.max(charsToBrightness.values());
            brightnessToChar = new TreeMap<>();
            for (Character c : charsToBrightness.keySet()) {
                double normalizedBrightness = getNormalizedBrightness(charsToBrightness.get(c),
                        minBrightness, maxBrightness);
                Character lastC = brightnessToChar.get(normalizedBrightness);
                if (lastC == null || ((int) ((char) c) < (int) ((char) lastC))) {
                    brightnessToChar.put(normalizedBrightness, c);
                }
            }
            charsetChanged = false;
        }
        return brightnessToChar.get(findClosestBrightness(brightness));
    }


    /*
     * Calculates the normalized brightness value of a character given its brightness,
     * minimum brightness, and maximum brightness.
     * assumes there are at least two valid chars with different brightnesses.
     *
     * @param brightness     the brightness value of the character
     * @param minBrightness  the minimum brightness value in the character set
     * @param maxBrightness  the maximum brightness value in the character set
     * @return the normalized brightness value of the character
     */
    private static double getNormalizedBrightness(double brightness,
                                                  double minBrightness, double maxBrightness) {
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /*
     * Finds the closest brightness value to the given brightness.
     * @param brightness The brightness value for which to find the closest value.
     * @return The closest brightness value found.
     */
    private double findClosestBrightness(double brightness) {
        Double floorBrightness = brightnessToChar.floorKey(brightness);
        Double ceilingBrightness = brightnessToChar.ceilingKey(brightness);

        // assumes there is at least one key in the brightnessToChar map.
        if (floorBrightness == null) {  // then ceilingBrightness isn't null.
            return ceilingBrightness;
        }
        if (ceilingBrightness == null) {
            return floorBrightness;
        }

        double diffFloor = brightness - floorBrightness;
        double diffCeiling = ceilingBrightness - brightness;
        return (diffFloor < diffCeiling) ? floorBrightness : ceilingBrightness;
    }
}
