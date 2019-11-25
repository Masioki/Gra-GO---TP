import java.io.*;
import java.net.Socket;

/*


                    KLASA PRZYKLADOWA


 */

    /**
     * Klasa klienta, nowy watek oraz wlasne drzewo
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public class Client<T extends Comparable<T> & Serializable> extends Thread {

        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private BinaryTree<T> tree;


        /**
         * Konstruktor przyjmuje streamy polaczenia do obslugi zapytan
         * @param socket gniazdo
         * @param out stream wyjsciowy
         * @param in stream wejsciowy
         */
        public Client(Socket socket, ObjectOutputStream out, ObjectInputStream in){
            this.in = in;
            this.out = out;
            this.socket = socket;
            try { tree = new BinaryTree<T>((T)in.readObject());}
            catch(IOException |ClassNotFoundException e){System.out.println("blad gniazda");}
        }

        /**
         * Metoda watku, oczekuje na zapytania od klienta, oraz przesyla odpowiedzi
         */
        @Override
        public void run(){
            System.out.println("startklienta");
            boolean end = false;

            while(!end ){
                try {
                    String temp = "";
                    try {
                        temp = (String) in.readObject();
                    }
                    catch(StreamCorruptedException f){f.printStackTrace();System.out.println("stream");}
                    catch(OptionalDataException g){g.printStackTrace();System.out.println("optional");}
                    catch(IOException h){h.printStackTrace();System.out.println("io"); end = true;}

                    switch (temp) {
                        case "INSERT": {
                            System.out.println("insert");
                            tree.insert((T)in.readObject());
                            break;
                        }
                        case "DELETE": {
                            System.out.println("delete");
                            tree.delete((T)in.readObject());
                            break;
                        }
                        case "GET": {
                            System.out.println("get");
                            out.reset();
                            out.writeObject(tree.draw());
                            break;
                        }
                        case "SEARCH": {
                            System.out.println("search");
                            boolean exists = tree.search((T)in.readObject());
                            out.writeObject(exists);
                            break;
                        }
                        case "END": {
                            System.out.println("end");
                            end = true;
                            break;
                        }
                        default: System.out.println("default");
                    }
                }
                catch(IOException h){h.printStackTrace();}
                catch(ClassNotFoundException j){System.out.println("blad klasy");}
            }

            try{socket.close();System.out.println("close");}
            catch(IOException e){System.out.println("blad zamykania gniazda");}
        }
    }

