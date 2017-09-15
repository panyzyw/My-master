package com.yongyida.robot.voice.master.bean;

/**
 * Created by panyzyw on 2017/6/13.
 */

public class StockBean2 extends BaseBean {
    public class Semantic{
        public Slots slots;
    }
    public class Slots{
        public String category;
        public String name;
        public String stock;
        public String code;
    }
}
