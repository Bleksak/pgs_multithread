public abstract class Talk {

    private final String name;

    protected Talk(String name) {
        this.name = name;
    }

    public void talk(String message) {
        System.out.println(name + ": " + message);
    }
}
