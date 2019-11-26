package Services;

//Class Service should be singleton
public class Service {
    static Service service = null;

    //lista metod - na razie zwracaja void a nie naszą klasę z body i enumem
    void signUp(String login, String password) {
        System.out.println("Zalogowano pomyślnie podany login: " + login + " podane hasło: " + password);
    }

    void loadGames() {
        System.out.println("Games loaded");
    }

    void gameMove() {

    }

    void joinGame() {
        System.out.println("You joined a game");
    }


    /**
     * Private Constructor
     */
    private Service() {

    }

    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }
}
