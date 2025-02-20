import java.util.*;

public class SymbolTable {

    public void printSymbolTable() {
    System.out.println("+-----------------------------------+----------------------+");
    System.out.println("|              Lexeme               |         Type         |");
    System.out.println("+-----------------------------------+----------------------+");
    System.out.printf("| %-33s | %-20s |\n", "[a-z]+", "IDENTIFIER");
    System.out.printf("| %-33s | %-20s |\n", "[0-9]", "INTEGER");
    System.out.printf("| %-33s | %-20s |\n", "[a-z]", "CHARACTER");
    System.out.printf("| %-33s | %-20s |\n", "[0-9]+.[0-9]+", "DECIMAL");
    System.out.printf("| %-33s | %-20s |\n", "sparkle,glossy,pinktruth,chatter", "KEYWORD");
    System.out.printf("| %-33s | %-20s |\n", "noway,sotrue", "BOOLEAN");
    System.out.printf("| %-33s | %-20s |\n", "fix", "CONSTANT");
    System.out.printf("| %-33s | %-20s |\n", "askqueen", "INPUT");
    System.out.printf("| %-33s | %-20s |\n", "showoff", "OUTPUT");
    System.out.printf("| %-33s | %-20s |\n", "#", "ONELINE_COMMENT");
    System.out.printf("| %-33s | %-20s |\n", "$/", "MULTILINE_COMMENT");
    System.out.printf("| %-33s | %-20s |\n", "queenbee", "GLOBAL");
    System.out.printf("| %-33s | %-20s |\n", "+,-,/,%,^", "ARITHMETIC_OPERATOR");
    System.out.printf("| %-33s | %-20s |\n", "=", "ASSIGNMENT_OPERATOR");
    System.out.println("+-----------------------------------+----------------------+");
    }

}
