package za.co.morse.morsesender;

public class Main {
    public static void main(String[] args) {
        MorseSender sender = new MorseSender(800, 12);
        sender.sender();
    }
}