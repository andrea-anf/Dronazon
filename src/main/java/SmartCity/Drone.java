package SmartCity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {
    private int id;
    private String port;
    private String server;

    public Drone(String port, String server){
        this.id = (int)Math.floor(Math.random()*(999-100+1)+999);
        this.port = port;
        this.server = server;
    }


}
