package com.yongyida.robot.voice.master.bean;



public class WeatherBean  extends BaseBean{
	public Semantic semantic;

	public class Semantic {
		public Slots slots;
	}

	public class Slots {	
		public Datetime datetime;
		public Location location;
	}
	
	public class Datetime{
		public String date;
		public String type;
		public String dateOrig;
	}
	
	public class Location{
		//1
		public String city;
		public String poi;
		public String type;
		
		//2
		public String cityAddr;
		//public String city;
		//public String type;
			
		//3
		public String provinceAddr;
		public String province;
		//public String type;
		
		//4
		public String country;
		//public String type;
	}
	
}

/*

//一 、 地区的4种不同情况
// 1 没说地方名
    "operation": "QUERY",
    "rc": 0,
    "semantic": {
        "slots": {
            "datetime": {
                "date": "CURRENT_DAY",
                "type": "DT_BASIC"
            },
            "location": {
                "city": "CURRENT_CITY",
                "poi": "CURRENT_POI",
                "type": "LOC_POI"
            }
        }
    },
    "service": "weather",
    "text": "天气怎么样",

	
//2 说了城市名
  "semantic": {
    "slots": {
      "location": {
        "cityAddr": "深圳", 
        "city": "深圳市", 
        "type": "LOC_BASIC"
      }, 
      "datetime": {
        "date": "CURRENT_DAY", 
        "type": "DT_BASIC"
      }
    }
  }, 
  "rc": 0, 
  "operation": "QUERY", 
  "service": "weather", 
  "text": "深圳的天气怎么样",
  
  
//3 说了省份名
  "semantic": {
    "slots": {
      "location": {
        "provinceAddr": "湖北", 
        "type": "LOC_BASIC", 
        "province": "湖北省"
      }, 
      "datetime": {
        "date": "CURRENT_DAY", 
        "type": "DT_BASIC"
      }
    }
  }, 
  "rc": 0, 
  "operation": "QUERY", 
  "service": "weather", 
  "text": "湖北的天气怎么样",

//4 说了国家名
   "semantic": {
    "slots": {
      "location": {
        "country": "中国", 
        "type": "LOC_BASIC"
      }, 
      "datetime": {
        "date": "CURRENT_DAY", 
        "type": "DT_BASIC"
      }
    }
  }, 
  "rc": 0, 
  "operation": "QUERY", 
  "service": "weather", 
  "text": "中国的天气怎么样",
 
 
 //二 、datetime 在 时间说法不同的4种情况
 
 	//1 没说时间 : 深圳的天气怎么样
       "datetime": {
        "date": "CURRENT_DAY", 
        "type": "DT_BASIC"
      }
      
     //2 说了日常时间 : (今 明 后 大后 昨 前  , 天) 的天气怎么样 
  "semantic": {
    "slots": {
      "location": {
        "type": "LOC_POI", 
        "city": "CURRENT_CITY", 
        "poi": "CURRENT_POI"
      }, 
      "datetime": {
        "date": "2016-04-20", 
        "type": "DT_BASIC", 
        "dateOrig": "今天"
      }
    }
    
    //3 说了具体日期 4月20号的天气怎么样
  "semantic": {
    "slots": {
      "location": {
        "type": "LOC_POI", 
        "city": "CURRENT_CITY", 
        "poi": "CURRENT_POI"
      }, 
      "datetime": {
        "date": "2016-04-20", 
        "type": "DT_BASIC", 
        "dateOrig": "4月20号"
      }
    }
 
 
 注意: 上面是讯飞的普通语义,
 但是以前版本  没说地区  (今 明 后 大后 昨 前  , 天) 的天气怎么样 , 被 李敏的私有 语义拦截了 (完全没必要拦截)，结果如下

 {
    "operation": "123",
    "rc": 0,
    "semantic": {
        "slots": {
            "datetime": {
                "date": "今天"
            },
            "weather": {
                "wea": "天气"
            }
        }
    },
    "service": "weather",
    "text": "今天的天气怎么样？"
}
 
*/
