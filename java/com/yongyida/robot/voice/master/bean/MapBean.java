package com.yongyida.robot.voice.master.bean;

public class MapBean extends BaseBean {
    public class Semantic{
        public Slots slots;
    }
    public class Slots{
        public Location location;
        public StartLoc startLoc;
        public EndLoc endLoc;
    }
    public class Location{
        public String city;
        public String type;
        public String poi;
    }
    public class StartLoc{
        public String city;
        public String type;
        public String poi;
    }
    public class EndLoc{
        public String city;
        public String type;
        public String cityAddr;
    }
    public Semantic semantic;
    public Slots slots;
}
