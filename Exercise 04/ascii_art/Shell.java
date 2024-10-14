package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.TreeSet;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * The Shell class manages user interaction and controls the ASCII art generation program.
 * It initializes necessary components, reads user input, and executes corresponding actions.
 * It handles errors, such as empty character sets or image file problems,
 * and dynamically updates program configurations based on user input.
 * Overall, it serves as the main control unit for the program,
 * orchestrating the generation of ASCII art from images.
 */
public class Shell {

    // default parameters for the algorithm:
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int DEFAULT_RES = 128;
    private static final String CONSOLE_TYPE = "console";
    private static final String HTML_TYPE = "html";
    private static final String DEFAULT_OUTPUT_FILE_NAME = "out.html";
    private static final String DEFAULT_OUTPUT_FONT_NAME = "Courier New";

    // The basic commands we want to generate for our interface:
    private static final String INPUT_PROMPT = ">>> ";
    private static final String EXIT_CASE = "exit";
    private static final String CHARS_CASE = "chars";
    private static final String ADD_CASE = "add";
    private static final String REMOVE_CASE = "remove";
    private static final String RES_CASE = "res";
    private static final String IMAGE_CASE = "image";
    private static final String OUTPUT_CASE = "output";
    private static final String ALGORITHM_CASE = "asciiArt";
    private static final String ALL_CASE = "all";
    private static final String SPACE_CASE = "space";
    private static final String UP_CASE = "up";
    private static final String DOWN_CASE = "down";

    // ASCII analyzing constants:
    private static final char SPACE_CHAR = ' ';
    private static final String SPACE_DELIM = " ";
    private static final String RANGE_DELIM = "-";
    private static final int SINGLE_CHAR = 1;
    private static final int TWO = 2;
    private static final int MIN_ASCII = 32;
    private static final int MAX_ASCII = 126;

    // Errors and other messages:
    private static final String BASIC_COMMAND_ERROR = "Did not execute due to incorrect command.";
    private static final String EMPTY_CHARSET_ERROR = "Did not execute. Charset is empty.";
    private static final String OUTPUT_ERROR = "Did not change output method due to incorrect format.";
    private static final String IMAGE_FILE_ERROR = "Did not execute due to problem with image file.";
    private static final String RESOLUTION_FORMAT_ERROR =
            "Did not change resolution due to incorrect format.";
    private static final String RESOLUTION_BOUND_ERROR =
            "Did not change resolution due to exceeding boundaries.";
    private static final String ADD_OR_REMOVE_ERROR_FORMAT =  "Did not %s due to incorrect format.\n";
    private static final String NEW_RES_SET_FORMAT = "Resolution set to %d.\n";


    // fields - the current algorithm's parameters:
    private final TreeSet<Character> charset;
    private final SubImgCharMatcher subImgCharMatcher;
    private String imagePath;
    private int res;
    private AsciiArtAlgorithm algorithm;
    private char[][] output;
    private AsciiOutput asciiOutput;

    /**
     * Constructor for the Shell class.
     * Initializes default parameters and constructs necessary objects.
     *
     * @throws ResolutionOutOfBoundsException  Thrown if the specified resolution is out of bounds.
     * @throws IOException                    Thrown if there's an I/O error during initialization.
     */
    public Shell() throws ResolutionOutOfBoundsException, IOException {
        charset = new TreeSet<>();
        for (char c : DEFAULT_CHARSET) {
            charset.add(c);
        }
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
        imagePath = DEFAULT_IMAGE_PATH;
        res = DEFAULT_RES;
        algorithm = new AsciiArtAlgorithm(imagePath, res, subImgCharMatcher);
        asciiOutput = new ConsoleAsciiOutput();
    }

    /**
     * Starts the shell and interacts with the user.
     * Handles user input and executes corresponding actions.
     */
    public void run() {
        boolean charsetChanged = true, algorithmChanged = true;
        while (true) {
            System.out.print(INPUT_PROMPT);
            String input = KeyboardInput.readLine();
            switch (input) {
            case EXIT_CASE:
                return;
            case CHARS_CASE:
                handleChars();
                break;
            case ALGORITHM_CASE:
                if (handleAlgorithm(charsetChanged || algorithmChanged)) {
                    charsetChanged = false;
                    algorithmChanged = false;
                }
                break;
            default:
                String[] inputWords = input.split(SPACE_DELIM);
                String command = inputWords[0];
                String commandArgument = (inputWords.length == TWO) ? inputWords[1] : null;
                switch (command) {
                case ADD_CASE:
                    charsetChanged = handleAdd(commandArgument) || charsetChanged;
                    break;
                case REMOVE_CASE:
                    charsetChanged = handleRemove(commandArgument) || charsetChanged;
                    break;
                case RES_CASE:
                    algorithmChanged = handleRes(commandArgument) || algorithmChanged;
                    break;
                case IMAGE_CASE:
                    algorithmChanged = handleImage(commandArgument) || algorithmChanged;
                    break;
                case OUTPUT_CASE:
                    handleOutput(commandArgument);
                    break;
                default:
                    System.out.println(BASIC_COMMAND_ERROR);
                    break;
                }
            }
        }
    }

    /*
     * Handles the ASCII art conversion process and output.
     * If the charset is empty, it prints a message and returns false.
     * If 'needToRun' is true, it runs the ASCII art conversion algorithm and outputs the result.
     * @param needToRun  Flag indicating whether the conversion algorithm needs to be run.
     * @return  True if the algorithm is successfully handled, false otherwise.
     */
    private boolean handleAlgorithm(boolean needToRun) {
        if (charset.isEmpty()) {
            System.out.println(EMPTY_CHARSET_ERROR);
            return false;
        }
        if (needToRun) {
            output = algorithm.run();
        }
        asciiOutput.out(output);
        return true;
    }

    /*
     * Handles changing the output method based on the provided argument.
     * If the output argument is valid, it updates the 'asciiOutput' object accordingly.
     * If the argument is incorrect or null, it prints a message indicating the issue.
     * @param outputArgument  The argument specifying the new output method.
     */
    private void handleOutput(String outputArgument) {
        if (outputArgument != null) {
            AsciiOutput newAsciiOutput = null;
            switch (outputArgument) {
            case CONSOLE_TYPE:
                newAsciiOutput = new ConsoleAsciiOutput();
                break;
            case HTML_TYPE:
                newAsciiOutput = new HtmlAsciiOutput(DEFAULT_OUTPUT_FILE_NAME, DEFAULT_OUTPUT_FONT_NAME);
                break;
            }
            if (newAsciiOutput != null) {
                asciiOutput = newAsciiOutput;
                return;
            }
        }
        System.out.println(OUTPUT_ERROR);
    }

    /*
     * Handles changing the input image path and generating a new ASCII art algorithm if necessary.
     * If the provided image path is not null,
     * it tries to generate a new algorithm with the updated path and resolution.
     * If successful, it updates the 'res' and 'imagePath' fields.
     * If there's an issue with the image file, it prints an error message.
     * @param newImagePath  The new image path to be set.
     * @return  True if the algorithm was successfully changed, false otherwise.
     */
    private boolean handleImage(String newImagePath) {
        if (newImagePath != null) {
            return tryGenerateNewAlgorithm(res, newImagePath);
        }
        System.out.println(IMAGE_FILE_ERROR);
        return false;
    }

    /*
     * Handles changing the resolution of the ASCII art conversion algorithm.
     * If the resolution argument is not null, it adjusts the resolution based on the argument.
     * If successful, it generates a new algorithm with the updated resolution and updates the 'res' field.
     * If the resolution was changed, it prints a message indicating the change.
     * If there's an issue with the resolution format or value, it prints an error message.
     * @param resArgument  The resolution adjustment argument (UP_CASE or DOWN_CASE).
     * @return  True if the resolution was successfully changed, false otherwise.
     */
    private boolean handleRes(String resArgument) {
        if (resArgument != null) {
            int newRes = res;
            switch (resArgument) {
            case UP_CASE:
                newRes *= TWO;
                break;
            case DOWN_CASE:
                newRes /= TWO;
                break;
            }
            if (newRes != res) {
                boolean algorithmChanged = tryGenerateNewAlgorithm(newRes, imagePath);
                if (algorithmChanged) {
                    // since we sent the same imagePath:
                    // the algorithm was changed iff the resolution was changed
                    System.out.printf(NEW_RES_SET_FORMAT, res);
                }
                return algorithmChanged;
            }
        }
        System.out.println(RESOLUTION_FORMAT_ERROR);
        return false;
    }

    /*
     * Attempts to generate a new ASCII art algorithm with the specified resolution and image path.
     * If successful, it updates the 'algorithm', 'res', and 'imagePath' fields.
     * If there's an issue with the image file or resolution boundaries, it prints an error message.
     * @param newRes        The new resolution to be set.
     * @param newImagePath  The new image path to be set.
     * @return  True if a new algorithm was successfully generated, false otherwise.
     */
    private boolean tryGenerateNewAlgorithm(int newRes, String newImagePath) {
        boolean newAlgorithmWasGenerated = false;
        try {
            algorithm = new AsciiArtAlgorithm(newImagePath, newRes, subImgCharMatcher);
            res = newRes;
            imagePath = newImagePath;
            newAlgorithmWasGenerated = true;
        } catch (IOException e) {
            System.out.println(IMAGE_FILE_ERROR);
        } catch (ResolutionOutOfBoundsException e) {
            System.out.println(RESOLUTION_BOUND_ERROR);
        }
        return newAlgorithmWasGenerated;
    }

    /*
     * Handles adding characters to the charset or removing them based on the provided argument.
     * If the argument is valid, it adds or removes characters accordingly.
     * If successful, it updates the charset and returns true.
     * If there's an issue with the argument format, it prints an error message and returns false.
     * @param removeArgument  The argument specifying characters to be removed.
     * @return  True if the charset was successfully updated, false otherwise.
     */
    private boolean handleRemove(String removeArgument) {
        return handleAddOrRemove(removeArgument, false);
    }

    /*
     * Handles adding characters to the charset based on the provided argument.
     * If the argument is valid, it adds characters to the charset.
     * If successful, it updates the charset and returns true.
     * If there's an issue with the argument format, it prints an error message and returns false.
     * @param addArgument  The argument specifying characters to be added.
     * @return  True if the charset was successfully updated, false otherwise.
     */
    private boolean handleAdd(String addArgument) {
        return handleAddOrRemove(addArgument, true);
    }


    /*
     * Handles adding or removing characters to/from the charset based on the provided argument.
     * If the argument is a single character, it adds or removes the character accordingly.
     * If the argument is "all", it adds or removes all ASCII characters in the range [MIN_ASCII, MAX_ASCII].
     * If the argument is "space", it adds or removes the space character (' ').
     * If the argument is in the format "x-y" (where x and y are single characters), it adds or
     * removes characters in the ASCII range from x to y.
     * If successful, it updates the charset and returns true.
     * If there's an issue with the argument format, it prints an error message and returns false.
     * @param resetArgument  The argument specifying characters to be added or removed.
     * @param add  A boolean indicating whether characters should be added (true) or removed (false).
     * @return  True if the charset was successfully updated, false otherwise.
     */
    private boolean handleAddOrRemove(String resetArgument, boolean add) {
        if (resetArgument != null) {
            if (resetArgument.length() == SINGLE_CHAR) {
                if (add) {
                    subImgCharMatcher.addChar(resetArgument.charAt(0));
                    return charset.add(resetArgument.charAt(0));
                } else {
                    subImgCharMatcher.removeChar(resetArgument.charAt(0));
                    return charset.remove(resetArgument.charAt(0));
                }
            }
            switch (resetArgument) {
                case ALL_CASE:
                    return resetCharsInRange(MIN_ASCII, MAX_ASCII, add);
                case SPACE_CASE:
                    if (add) {
                        subImgCharMatcher.addChar(SPACE_CHAR);
                        return charset.add(SPACE_CHAR);
                    } else {
                        subImgCharMatcher.removeChar(SPACE_CHAR);
                        return charset.remove(SPACE_CHAR);
                    }
                default:
                    String[] asciiLimits = resetArgument.split(RANGE_DELIM);
                    if (asciiLimits.length == TWO &&
                            asciiLimits[0].length() == SINGLE_CHAR &&
                            asciiLimits[1].length() == SINGLE_CHAR) {
                        int minAscii = min(asciiLimits[0].charAt(0), asciiLimits[1].charAt(0));
                        int maxAscii = max(asciiLimits[0].charAt(0), asciiLimits[1].charAt(0));
                        return resetCharsInRange(minAscii, maxAscii, add);
                    }
            }
        }
        System.out.printf(ADD_OR_REMOVE_ERROR_FORMAT, (add ? ADD_CASE : REMOVE_CASE));
        return false;
    }


    /*
     * Resets the charset by adding or removing characters within the specified ASCII range
     * [minAscii, maxAscii].
     * If the 'add' parameter is true, it adds characters within the range to the charset.
     * If 'add' is false, it removes characters within the range from the charset.
     * If successful, it updates the charset and returns true.
     * @param minAscii  The minimum ASCII value in the range.
     * @param maxAscii  The maximum ASCII value in the range.
     * @param add       A boolean indicating whether characters should be added (true) or removed (false).
     * @return          True if the charset was successfully updated, false otherwise.
     */
    private boolean resetCharsInRange(int minAscii, int maxAscii, boolean add) {
        boolean charsetChanged = false;
        for (int asciiIndex = minAscii; asciiIndex <= maxAscii; asciiIndex++) {
            if (add) {
                subImgCharMatcher.addChar((char) asciiIndex);
                if (charset.add((char) asciiIndex)) {
                    charsetChanged = true;
                }
            } else {
                subImgCharMatcher.removeChar((char) asciiIndex);
                if (charset.remove((char) asciiIndex)) {
                    charsetChanged = true;
                }
            }
        }
        return charsetChanged;
    }

    /*
     * Resets the charset to its default state and prints the characters in it.
     */
    private void handleChars() {
        for(char c: charset) {
            System.out.print(c + SPACE_DELIM);
        }
        System.out.println();
    }

    /**
     * The main method, try to create new Shell by default parameters, and if succeeded runs it.
     * @param args arguments in command line.
     */
    public static void main(String[] args) {
        try {
            Shell shell = new Shell();  // exception can be thrown only from here!
            shell.run();  // this method handles the exception occurred in it by itself.
        } catch (IOException | ResolutionOutOfBoundsException e) {
            // undefined behavior
        }
    }
}