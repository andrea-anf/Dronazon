package Amministrazione.Statistics;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatLists {
    @XmlElement(name = "statlist")
    private ArrayList<Stat> statlist;
    private static StatLists instance;

    private StatLists() {
        statlist = new ArrayList<>();
    }

    //singleton class
    public synchronized static StatLists getInstance() {
        if (instance == null) {
            instance = new StatLists();
        }
        return instance;
    }

    public synchronized ArrayList<Stat> getLastNStats(int lastNStats) {
            List<Stat> nStatList = statlist.subList(statlist.size()-Math.min(statlist.size(),lastNStats), statlist.size());
            return new ArrayList<>(nStatList);
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

