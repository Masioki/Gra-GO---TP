package Domain;

/*
Powinno byc rzucane w przypadku ruchu wbrew zasada.
Wiadomosc w konstruktorze powinna byc wiadomoscia o zlamanej zasadzie.
Zostanie ona przekazana uzytkownikowi
 */
public class WrongMoveException extends Exception {

    public WrongMoveException(String errorMessage) {
        super(errorMessage);
    }

}
