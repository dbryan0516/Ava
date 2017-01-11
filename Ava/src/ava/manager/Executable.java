package ava.manager;

public class Executable {

    public static void main(String[] args) {
        System.out.println("Starting");
        AvaManager manager = new AvaManager();
        manager.listen();
        System.out.println("Ending");

    }

}
