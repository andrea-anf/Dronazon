package Amministrazione.Statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsList {
    @XmlElement(name = "statlist")
    private ArrayList<Stat> statlist;
    private static StatsList instance;

    private StatsList() {
        statlist = new ArrayList<>();
    }

    //singleton class
    public synchronized static StatsList getInstance() {
        if (instance == null) {
            instance = new StatsList();
        }
        return instance;
    }

    public synchronized  Stat getByTs(String ts){
        for(Stat s : this.statlist){
            if(s.getTimestamp() == ts){
                return s;
            }
        }
        return null;
    }
    public synchronized ArrayList<Stat> getAllStats() {
        return new ArrayList<>(statlist);
    }

    public synchronized ArrayList<Stat> getLastNStats(int lastNStats) {
        List<Stat> nStatList = statlist.subList(statlist.size()-Math.min(statlist.size(),lastNStats), statlist.size());
        return new ArrayList<>(nStatList);
    }

//    Media del numero di consegne effettuate dai droni della smart-city tra due timestamp t1 e t2
    public synchronized String getAvgDeliveries(Timestamp tMin, Timestamp tMax) {
        int counter=0;
        int i=0;

        for (Stat stat : statlist) {
            Timestamp statTimestamp = Timestamp.valueOf(stat.getTimestamp().replace("T", " "));
            //check if there are deliveries in the specified range
            if (statTimestamp.compareTo(tMin)>=0 && statTimestamp.compareTo(tMax)<=0 ) {
                counter += stat.getDeliveriesCount();
                i++;
            }
        }
        //if there are deliveries in the specified range
        if(counter != 0){
            return "{ \"averageDeliveries\" : \"" + counter/i+"\"}";
        }
        //else zero
        else{
            return "{ \"averageDeliveries\" : \"0\"}";
        }
    }

//    Media dei chilometri percorsi dai droni della smart-city tra due timestamp t1 e t2
    public synchronized String getAvgKilometers(Timestamp tMin, Timestamp tMax) {
        double counter=0;
        double i=0;

        for (Stat stat : statlist) {
            Timestamp statTimestamp = Timestamp.valueOf(stat.getTimestamp().replace("T", " "));
            //check if there are deliveries in the specified range
            if (statTimestamp.compareTo(tMin)>=0 && statTimestamp.compareTo(tMax)<=0 ) {
                counter += stat.getKilometers();
                i++;
            }
        }
        if(counter != 0){
            return "{ \"averageKilometers\" : \""+counter/i+"\"}";
        }
        else{
            return "{ \"averageKilometers\" : \"0\"}";
        }
    }

    public synchronized boolean addStat(Stat stat) {
        if (statlist.contains(stat)) {
            return false;
        } else {
            statlist.add(stat);
            return true;
        }
    }

}

