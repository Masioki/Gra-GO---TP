package example;
//Class Service should be singleton
public class Service {
    static Service service = null;

    private Service()
    {

    }

    public static Service getInstance()
    {
        if(service == null)
        {
            service = new Service();
        }
        return service;
    }
}
