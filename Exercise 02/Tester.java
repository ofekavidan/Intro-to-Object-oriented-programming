//
//switch between bots: genius, clever, whatever
//check: genius-whatever, clever-whatever, genius-clever
//cases i found problematic (size,streak): (9,3), (9,8), (9,9), (4,4), (4,3)
// good luck :)
// Tal N
public class Tester {

    public static void main(String[] args) {
            for (int i = 4; i <= 9; i++) {
                for (int j = 3; j <= i; j++) {
                    System.out.println("**********\nSize: " + i + " Streak: " + j);
                    Tournament.main(new String[]{"10000", String.valueOf(i),
                            String.valueOf(j), "none", "genius", "whatever"});
                }
            }
        }


}
