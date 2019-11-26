package Services;

import Commands.Builder.CommandBuilderProvider;
import Commands.Command;
import Commands.GameCommandType;

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


    /*

    PRZYKLADOWE BUDOWANIE KOMENDY

     */
    void gameMove() {
        Command c;
        try{
            c = CommandBuilderProvider.newGameCommandBuilder()
                    .newCommand()
                    .withHeader(GameCommandType.MOVE)
                    .withPosition(0 ,0)
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }
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
