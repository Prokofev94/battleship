package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Player {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static boolean turnPlayer1 = true;
    private char[][] playingField = new char[10][10];
    private char[][] fogOfWar = new char[10][10];
    private List<String> availableCells = new ArrayList<>(Arrays.asList(
            "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10",
            "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10",
            "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
            "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10",
            "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10",
            "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10",
            "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10",
            "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10",
            "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10"));
    private List<List<String>> shipCells = new ArrayList<>();

    public Player() {
        for (char[] rows : playingField) {
            Arrays.fill(rows, '~');
        }
        for (char[] rows : fogOfWar) {
            Arrays.fill(rows, '~');
        }
    }

    public static void passMove() {
        System.out.println("Press Enter and pass the move to another player");
        String enter = " ";
        do {
            try {
                enter = reader.readLine();
            } catch (java.io.IOException exc) {
                System.out.println();
            }
        } while ("\n".equals(enter));
    }

    public static void startGame() throws IOException {
        System.out.println("Player 1, place your ships on the game field");
        Player player1 = new Player();
        printPlayingField(player1.playingField);
        player1.placeShips();
        passMove();
        System.out.println("Player 2, place your ships to the game field");
        Player player2 = new Player();
        printPlayingField(player2.playingField);
        player2.placeShips();
        passMove();
        shot(player1, player2);
    }

    public static void printPlayingField(char[][] field) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        char ch = 'A';
        for (char[] row : field) {
            System.out.print(ch++);
            for (char cell : row) {
                System.out.print(" " + cell);
            }
            System.out.println();
        }
    }

    public void placeShips() {
        placeAircraftCarrier();
        placeBattleship();
        placeSubmarine();
        placeCruiser();
        placeDestroyer();
    }

    public void placeAircraftCarrier() {
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        placeShip(4);
    }

    public void placeBattleship() {
        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        placeShip(3);
    }

    public void placeSubmarine() {
        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        placeShip(2);
    }

    public void placeCruiser() {
        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        placeShip(2);
    }

    public void placeDestroyer() {
        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        placeShip(1);
    }

    public void placeShip(int range) {
        String[] coordinates;
        boolean correctCoordinates = false;
        while (!correctCoordinates) {
            coordinates = scanner.nextLine().split(" ");
            int x1 = Integer.parseInt(coordinates[0].substring(1));
            int x2 = Integer.parseInt(coordinates[1].substring(1));
            int y1 = coordinates[0].charAt(0);
            int y2 = coordinates[1].charAt(0);
            if (y1 == y2) {
                if (Math.abs(x1 - x2) != range) {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                } else {
                    correctCoordinates = true;
                    int from = Math.min(x1, x2);
                    int to = Math.max(x1, x2);
                    for (int i = from; i <= to; i++) {
                        String cell = String.valueOf((char) y1) + i;
                        if (!availableCells.contains(cell)) {
                            correctCoordinates = false;
                            System.out.println("Error! Wrong ship location! Try again:");
                            break;
                        }
                    }
                    if (correctCoordinates) {
                        toPlace(from, y1, to, y2);
                        printPlayingField(playingField);
                    }
                }
            } else if (x1 == x2) {
                if (Math.abs(y1 - y2) != range) {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                } else {
                    correctCoordinates = true;
                    int from = Math.min(y1, y2);
                    int to = Math.max(y1, y2);
                    for (int i = from; i <= to; i++) {
                        String cell = String.valueOf((char) i) + x1;
                        if (!availableCells.contains(cell)) {
                            correctCoordinates = false;
                            System.out.println("Error! Wrong ship location! Try again:");
                            break;
                        }
                    }
                    if (correctCoordinates) {
                        toPlace(x1, from, x2, to);
                        printPlayingField(playingField);
                    }
                }
            } else {
                System.out.println("Error! Wrong ship location! Try again:");
            }
        }
    }

    public void toPlace(int x1, int y1, int x2, int y2) {
        List<String> ship = new ArrayList<>();
        for (int i = x1 - 1; i < x2 + 2; i++) {
            for (int j = y1 - 1; j < y2 + 2; j++) {
                String cell = String.valueOf((char) j) + i;
                availableCells.remove(cell);
            }
        }
        for (int i = x1 - 1; i < 10 && i < x2; i++) {
            for (int j = y1 - 65; j < 10 && j < y2 - 64; j++) {
                playingField[j][i] = 'O';
                ship.add(String.valueOf((char) (j + 65)) + (i + 1));
            }
        }
        shipCells.add(ship);
    }

    public static void shot(Player player1, Player player2) {
        boolean win = false;
        Player attacker;
        Player defender;
        String yourTurn;
        while (!win) {
            if (turnPlayer1) {
                attacker = player1;
                defender = player2;
                yourTurn = "Player 1, it's your turn:";
            } else {
                attacker = player2;
                defender = player1;
                yourTurn = "Player 2, it's your turn:";
            }
            printPlayingField(defender.fogOfWar);
            System.out.println("---------------------");
            printPlayingField(attacker.playingField);
            System.out.println(yourTurn);
            String coordinate;
            int x;
            int y;
            do {
                coordinate = scanner.nextLine();
                x = coordinate.charAt(0) - 65;
                y = Integer.parseInt(coordinate.substring(1)) - 1;
                if (x < 0 || x > 9 || y < 0 || y > 9) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                }
            } while (x < 0 || x > 9 || y < 0 || y > 9);
            if (defender.playingField[x][y] == 'O') {
                defender.playingField[x][y] = 'X';
                attacker.fogOfWar[x][y] = 'X';
                for (List<String> ship : defender.shipCells) {
                    if (ship.contains(coordinate)) {
                        ship.remove(coordinate);
                        if (ship.size() == 0) {
                            defender.shipCells.remove(ship);
                            if (defender.shipCells.size() == 0) {
                                System.out.println("You sank the last ship. You won. Congratulations!");
                                win = true;
                            } else {
                                System.out.println("You sank a ship! Specify a new target:");
                            }
                        } else {
                            System.out.println("You hit a ship! Try again:");
                        }
                        break;
                    }
                }
            } else if (defender.playingField[x][y] == 'X') {
                System.out.println("You hit a ship! Try again:");
            } else {
                defender.playingField[x][y] = 'M';
                defender.fogOfWar[x][y] = 'M';
                System.out.println("You missed. Try again:");
            }
            passMove();
            turnPlayer1 = !turnPlayer1;
        }
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        Player.startGame();
    }
}
