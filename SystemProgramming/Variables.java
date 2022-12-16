import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables
{
    /*-----------------Declarations--------------------*/ 
    public static int i, j, k, z, num;
    public static int dcI; // Convert Hexdecimal number to decimal number (INSTRUCTIONS)
    public static int dcLocc; // Convert Hexdecimal number to decimal number (LOCATION ADDRESS)
    public static int dcOP; // Operations between instructions' format and location address
    public static int dis, TA, pc, base; // Variables Used For Object Code Operations
    public static String N, I, X, B, P, E; // Flag Bits
    public static String format, opcode, opcodeBi, refName, refLocation, finalCode, disBi, numBi;
    public static char zero;
    public static String register[] = new String [2];
    public static String Label[] = new String [46]; // Store The Labels
    public static String Inst[] = new String [46]; // Store The Instructions
    public static String Ref[] = new String [46]; // Store The References
    public static String Locc[] = new String [46]; // Store The Location Counter
    public static String ObjectCode[] = new String [46]; // Store The Object Code
    public static Pattern pattern, patternHashNum, patternHashRef, patternIN;
    public static Matcher matcher, matcherHashNum, matcherHashRef, matcherIN;
    public static String h, t, e;
    public static String start, end, lengthStr;
    public static int changeStartHex, changeEndHex, length;
    public static boolean flag, flagBase;
}