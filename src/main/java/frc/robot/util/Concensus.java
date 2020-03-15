package frc.robot.util;

import java.util.HashMap;
import java.util.Map;

public class Concensus {
    private Map<String, Boolean> votes;
    private ConcensusMode mode;
    public enum ConcensusMode {
        Majority,
        All,
        Any
    }
    public Concensus(ConcensusMode mode) {
        votes = new HashMap<String, Boolean>();
        this.mode = mode;
    }
    public void vote(boolean shouldRun) {
        votes.put(new Exception().getStackTrace()[2].getClassName(), shouldRun);
    }
    public boolean getConcensus() {
        switch(mode) {
            case All:
                for(Boolean b : votes.values()) {
                    if(!b) return false;
                }
                return true;
            case Any:
                for(Boolean b : votes.values()) {
                    if(b) return true;
                }
                return false;
            case Majority:
                int count = 0;
                for(Boolean b : votes.values()) {
                    if(b) count++;
                }
                return count > votes.size() / 2;

        }
        return false;
    }
}