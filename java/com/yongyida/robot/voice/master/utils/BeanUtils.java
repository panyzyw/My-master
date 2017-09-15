package com.yongyida.robot.voice.master.utils;

import com.google.gson.Gson;
import com.yongyida.robot.voice.master.bean.ArithmeticBean;
import com.yongyida.robot.voice.master.bean.BaseBean;
import com.yongyida.robot.voice.master.bean.ChatBean;
import com.yongyida.robot.voice.master.bean.CookBean;
import com.yongyida.robot.voice.master.bean.DanceBean;
import com.yongyida.robot.voice.master.bean.HabitBean;
import com.yongyida.robot.voice.master.bean.HealthBean;
import com.yongyida.robot.voice.master.bean.JokeBean;
import com.yongyida.robot.voice.master.bean.MapBean;
import com.yongyida.robot.voice.master.bean.MovieInfoBean;
import com.yongyida.robot.voice.master.bean.MusicBean;
import com.yongyida.robot.voice.master.bean.NewsBean;
import com.yongyida.robot.voice.master.bean.PoetryLearnBean;
import com.yongyida.robot.voice.master.bean.SenseBean;
import com.yongyida.robot.voice.master.bean.SinologyBean;
import com.yongyida.robot.voice.master.bean.SmarthomeBean;
import com.yongyida.robot.voice.master.bean.SmsBean;
import com.yongyida.robot.voice.master.bean.SquaredanceBean;
import com.yongyida.robot.voice.master.bean.StockBean2;
import com.yongyida.robot.voice.master.bean.StoryBean;
import com.yongyida.robot.voice.master.bean.TelephoneBean;
import com.yongyida.robot.voice.master.bean.WeatherBean;

public class BeanUtils {
	public static BaseBean parseBaseBeanJson(String json,Class<BaseBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));		
			return null;
		}
	}
	
	public static ChatBean parseChatJson(String json,Class<ChatBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static CookBean parseCookJson(String json,Class<CookBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static JokeBean parseJokeJson(String json,Class<JokeBean> cls){
		try{
			Gson g = new Gson();
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static MusicBean parseMusicJson(String json,Class<MusicBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static SenseBean parseSenseJson(String json,Class<SenseBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static SmarthomeBean parseSmarthomeJson(String json,Class<SmarthomeBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static SmsBean parseSmsJson(String json,Class<SmsBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static StoryBean parseStoryJson(String json,Class<StoryBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static WeatherBean parseWeatherJson(String json,Class<WeatherBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static PoetryLearnBean parsePoetryLearnJson(String json,Class<PoetryLearnBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static SinologyBean parseSinologyJson(String json,Class<SinologyBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static DanceBean parseDanceJson(String json,Class<DanceBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static HealthBean parseHealthJson(String json,Class<HealthBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static MovieInfoBean parseMoveInfoJson(String json,Class<MovieInfoBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static NewsBean parseNewsJson(String json,Class<NewsBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}				
	}
	
	public static HabitBean parseHabitJson(String json,Class<HabitBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}		
	}
	
	public static ArithmeticBean parseArithmeticJson(String json,Class<ArithmeticBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}		
	}
	

	public static TelephoneBean parseTelephoneJson(String json,Class<TelephoneBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}		
	}	
	
	public static SquaredanceBean parseSquaredanceJson(String json,Class<SquaredanceBean> cls){
		try{
			Gson g = new Gson();					
			return g.fromJson(json, cls);
		}catch(Exception e){
			MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
			return null;
		}		
	}

    public static MapBean parseMapJson(String json, Class<MapBean> cls){
        try{
            Gson g = new Gson();
            return g.fromJson(json, cls);
        }catch(Exception e){
            MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
            return null;
        }
    }

    public static StockBean2 parseStockBean2Json(String json, Class<StockBean2> cls){
        try{
            Gson g = new Gson();
            return g.fromJson(json, cls);
        }catch(Exception e){
            MyLog.e("BeanUtils", MyExceptionUtils.getStringThrowable(e));
            return null;
        }
    }
	
	
}
