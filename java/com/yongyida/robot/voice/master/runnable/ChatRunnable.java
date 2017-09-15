package com.yongyida.robot.voice.master.runnable;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.ChatBean;
import com.yongyida.robot.voice.master.bean.ChatBean.CmccChatBean;
import com.yongyida.robot.voice.master.bean.RobotAgeBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.HttpUtil;
import com.yongyida.robot.voice.master.utils.JsonParser;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.MyRegularUtils;
import com.yongyida.robot.voice.master.utils.RobotInfoUtils;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.version.VersionControl;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

public class ChatRunnable extends BaseRunnable {
    private SharedPreferences sp;
    private ChatBean mChatBean;

    public ChatRunnable(String result) {
        mIsEndSendRecycle = true;
        mPriority = PRIORITY_OTHER;
        mInterrupt_state = INTERRUPT_STATE_STOP;
        sp = MasterApplication.mAppContext.getSharedPreferences("answer", MasterApplication.mAppContext.MODE_PRIVATE);
        mResult = result;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        MyLog.i("ChatRunnable", "run()");
        ChatBean chatBean = BeanUtils.parseChatJson(mResult, ChatBean.class);
        mChatBean = chatBean;
        if (chatBean == null) {
            MyLog.e("ChatRunnable", "chatBean == null");
            mIsEndSendRecycle = true;
            voiceTTS(Constant.comman_unkonw);
            return;
        }
        doChat(chatBean);
    }

    private void doChat(ChatBean cb) {
        try {

            if (VersionControl.mVersion == VersionControl.VERSION_CMCC ||
                    VersionControl.mVersion == VersionControl.VERSION_CMCC_HK) {
                //拦截移动 临时的问答
                MyLog.i("ChatRunnable", "getYiDongDefineAnswer  start");
                String yidongdefine = getYiDongDefineAnswer(cb.text);
                if (!TextUtils.isEmpty(yidongdefine)) {
                    voiceTTS(yidongdefine);
                    return;
                }
            }

            //下面的if处理都是私有语义或对普通语义的特殊处理
            //...

            if (cb.operation.equals("shutup")) { // 特殊处理,operation=shutup ,
                MyLog.i("ChatRunnable", "cb.operation == shutup");
                operation_shutdown();
                return;
            }

            if (cb.operation.equals("isname")) {// 确认机器人名字
                MyLog.i("ChatRunnable", "cb.operation == isname");
                if (!isRepeatSpecilQuestion("isname")) {
                    operation_isname(cb.semantic.slots.isname);
                }
                return;
            }

            if (cb.operation.equals("query_name")) { // 查询机器人名字
                MyLog.i("ChatRunnable", "cb.operation == query_name");
                if (!isRepeatSpecilQuestion("query_name")) {
                    operation_queryName(cb.text);
                }
                return;
            }

            if (cb.operation.equals("change_name")) { // 修改机器人名字
                MyLog.i("ChatRunnable", "cb.operation == change_name");
                operation_changeName(cb.semantic.slots.name);
                return;
            }

            if (cb.operation.equals("emotionchat")) {
                MyLog.i("ChatRunnable", "cb.operation == emotionchat");
                operation_emotionChat(cb.semantic.slots.action,cb.semantic.slots.answer);
                return;
            }

            if (cb.operation.equals("cmcc")) {
                MyLog.i("ChatRunnable", "cb.operation == cmcc");
                operation_cmccChat();
                return;
            }

            if (cb.operation.equals("patch")) {
                MyLog.i("ChatRunnable", "cb.operation == patch");
                operation_patch(cb.semantic.slots.what);
                return;
            }

            // 上面代码逻辑 是处理的都是讯飞私有语义的内容 或对普通聊天的特殊处理
            //下面的代码逻辑都是处理来自讯飞的语料库的普通聊天答案
            operation_commonChat(cb);

        } catch (Exception e) {
            MyLog.e("", MyExceptionUtils.getStringThrowable(e));
            voiceTTS("这个问题好难哦");
        }

    }

    /**
     * 问名字和确认名字的特殊处理
     */
    private boolean isRepeatSpecilQuestion(String operation) {
        String temp_operation = sp.getString("operation", "");
        int repeat_count = sp.getInt("count", 0);
        if (temp_operation == null || temp_operation.equals("")) {
            Editor edit = sp.edit();
            edit.putString("operation", operation);
            edit.putString("answer", "");
            edit.putInt("count", 1);
            edit.commit();
        } else {
            if ((temp_operation.equals("isname") && operation.equals("isname"))
                    || (temp_operation.equals("query_name") && operation.equals("query_name"))) {
                //voiceTTS(repeatAnswer());
                if (repeat_count == 2) {
                    dealRepeatedproblems();
                    return true;
                } else if (repeat_count == 1) {
                    Editor edit = sp.edit();
                    edit.putString("operation", operation);
                    edit.putString("answer", "");
                    edit.putInt("count", 2);
                    edit.commit();
                    return false;
                }

            } else {
                Editor edit = sp.edit();
                edit.putString("operation", operation);
                edit.putString("answer", "");
                edit.putInt("count", 1);
                edit.commit();
            }
        }
        return false;

    }

    public void voiceTTS(String text) {
        voiceTTS(text, null);
    }

    public void voiceTTS(String text, String voicerName) {
        MyLog.i("ChatRunnable", "voiceTTS");
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, new MySynthesizerListener());
        if (voicerName == null) {
            voicerName = Constant.xiaoai;
        }
        VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mChatBean,text);
    }

    public void specialTTS(String text,MySynthesizerListener mySynthesizerListener){
        TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mChatBean,text);
    }

    public void voiceMp(String uri,MyOnCompletionListener myOnCompletionListener){
        MP mp = new MP(uri, BaseVoice.MP_ASSETS_RESOURCE, mInterrupt_state,
                mPriority, mIsEndSendRecycle, new MyOnPreparedListener(),
                myOnCompletionListener);
        VoiceQueue.getInstance().add(mp);
    }

    public void voiceMp(String uri) {
        MP mp = new MP(uri, BaseVoice.MP_ASSETS_RESOURCE, mInterrupt_state,
                mPriority, mIsEndSendRecycle, new MyOnPreparedListener(),
                new MyOnCompletionListener());
        VoiceQueue.getInstance().add(mp);
    }

    public static String fourGPlus = "(4|四|世|自|实)(济|机|具|季|句|鸡|记|既|寂|际|己|纪|G|g|j|J)(家|加|佳|嘉)";

    private String getYiDongDefineAnswer(String question) {
        //
        if (MyRegularUtils.myMatch(new String[]{".*你有老?妈妈?吗.*",
                ".*你的?老?妈妈?(的?名字)?(又?(叫做?|是))(什么|谁).*"}, question)) {
            return Constant.answer_who_mothers[new Random().nextInt(2)];
        }

        // String[] s1 = new String[]{".*你有妈妈吗.*" ,
        // ".*你妈妈的名字叫什么.*",".*你妈叫什么名字.*",
        // ".*你的妈妈是谁.*",".*你妈妈叫什么名字.*",".*你妈是谁.*"};
        // if( MyRegularUtils.myMatch(s1, question) ){
        // return Constant.answer_who_mothers[new Random().nextInt(2)];
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{".*你有老?爸爸?吗.*",
                ".*你的?老?爸爸?(的?名字)?(又?(叫做?|是))(什么|谁).*"}, question)) {
            return Constant.answer_who_fathers[new Random().nextInt(2)];
        }
        // String[] s2 = new String[]{".*你有爸爸吗.*" ,
        // ".*你爸爸的名字叫什么.*",".*你爸叫什么名字.*",
        // ".*你的爸爸是谁.*",".*你爸爸叫什么名字.*",".*你爸是谁.*"};
        // if( MyRegularUtils.myMatch(s2, question) ){
        // return Constant.answer_who_fathers[new Random().nextInt(2)];
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{".*你有老?爸爸?妈妈?吗.*",
                ".*你的?老?爸爸?老?妈妈?(的?名字)?(又?(叫做?|是))(什么|谁).*"}, question)) {
            return Constant.your_father_mother_name;
        }
        // String[] s3 = new String[]{".*你爸妈叫什么.*",".*你的爸妈叫什么.*"};
        // if( MyRegularUtils.myMatch(s3, question) ){
        // return Constant.your_father_mother_name;
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{".*你来自哪.*",
                        ".*你(从|是从?)?哪里的?(来|人)?.*", ".*你的?老?家乡?(在|是)(哪|什么地方).*"},
                question)) {
            return Constant.answer_homes[new Random().nextInt(2)];
        }
        // String[] s4 = new String[]{".*你来自哪里.*" , ".*你从哪里来的.*",".*你是哪里的.*",
        // ".*你是哪里人.*"};
        // if( MyRegularUtils.myMatch(s4, question) ){
        // return Constant.answer_who_mothers[new Random().nextInt(2)];
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{".*你的?老板(是|叫)(谁|什么).*"},
                question)) {
            return Constant.answer_boss;
        }
        // String[] s5 = new String[]{".*你的老板是谁.*" };
        // if( MyRegularUtils.myMatch(s5, question) ){
        // return Constant.answer_boss;
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{
                ".*你?是谁(把你)?(创造|研发|制造|造出|发明)(出来)?(的|了)你?.*",
                ".*你是谁(创造|研发|制造|造出|发明).*"}, question)) {
            return Constant.answer_makers[new Random().nextInt(2)];
        }
        // String[] s6 = new String[]{".*是谁把你创造出来的呢.*" ,
        // ".*是谁创造的你.*",".*你是谁研发的.*",
        // ".*是谁研发的你.*",".*你是谁制造的.*",".*是谁制造的你.*"};
        // if( MyRegularUtils.myMatch(s6, question) ){
        // return Constant.answer_makers[new Random().nextInt(2)];
        // }

        //
        if (MyRegularUtils.myMatch(new String[]{".*什么是" + fourGPlus + ".*",
                ".*" + fourGPlus + "什么是" + ".*"}, question)) {
            return "四寂加是在国际范围及移动互联行业对四寂网络升级的统称";
        }
        //
        // if(MyRegularUtils.myMatch(new
        // String[]{".*(怎|如何)(.{0,8})用"+fourGPlus+".*"
        // ,".*"+fourGPlus+"(怎|如何)(.{0,8})用"+".*"}, question)){
        // return "具备支持四寂加功能的手机，开通四寂加功能，所在区域有四寂加加网络的覆盖是使用四寂加的三要素";
        // }

        return "";
    }

    void operation_shutdown() {
        // 发送闭嘴的表情
        Utils.sendBroadcast(Constant.ACTION_QUITE, "Chat", "show action");

        String[] shutups = {Constant.chat_shutup1, Constant.chat_shutup2};
        voiceTTS(shutups[new Random().nextInt(2)]);
    }

    void operation_isname(String isName) {
        try {
            if (RobotInfoUtils.getRobotName().equals(isName)) {
                voiceTTS(Constant.is_name + RobotInfoUtils.getRobotName());
            } else {
                voiceTTS(Constant.not_is_name + RobotInfoUtils.getRobotName());
            }
        } catch (Exception e) {
            MyLog.e("", MyExceptionUtils.getStringThrowable(e));
            voiceTTS(Constant.answer_name2 + RobotInfoUtils.getRobotName());
        }
    }

    void operation_queryName(String text) {

        if (VersionControl.mVersion == VersionControl.VERSION_DEMO ||
                VersionControl.mVersion == VersionControl.VERSION_DEMO_HK) {

            if (text.length() <= 12 && text.contains("你是谁")) {
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/video/video.mp4");
                Intent intentWhoAreYou = new Intent(Intent.ACTION_VIEW);
                intentWhoAreYou.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intentWhoAreYou.setDataAndType(uri, "video/mp4");
                MasterApplication.mAppContext.startActivity(intentWhoAreYou);
                return;
            }

        }

        voiceTTS(RobotInfoUtils.getQueryNameAnswer());
    }

    void operation_changeName(String newName) {
        try {
            String name = Constant.robot_default_name;
            if (newName != null)
                name = newName;
            voiceTTS(RobotInfoUtils.updateRobotName(name));
        } catch (Exception e) {
            MyLog.e("ChatRunnable", "operation_changeName , err : "
                    + MyExceptionUtils.getStringThrowable(e));
        }
    }

    private void operation_emotionChat(String action, String answer) {
        // 发送表情
        if (action != null) {
            sendEmotion(action);
        }
        // 合成语音
        String content = getRandAnswer(answer);
        voiceTTS(content);
    }

    public void operation_cmccChat() {

        CmccChatBean ccb = null;
        try {
            Gson g = new Gson();
            ccb = g.fromJson(mResult, CmccChatBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ccb == null) {
            voiceTTS("这个问题好难啊");
            return;
        }
        String answer = getAnswers(ccb);

        if (TextUtils.isEmpty(answer)) {
            answer = "抱歉,没有找到答案";
        }
        voiceTTS(answer);
    }

    private void operation_patch(String what) { // 补丁函数 , 处理前期没考虑到或后期要求增加的问题,
        if (what.equals("query_time")) { // 询问时间补丁 : 这会儿几点了
            String time = "时间获取异常";
            time = myLocaltime();
            voiceTTS(time);
        }
    }

    public void operation_commonChat(ChatBean cb) {

        // 判断聊天的结果内容,发送相应的表情,并返回答案是否是纯文本
        boolean isText = isText(cb.answer.text);
        // 根据用户对机器人说的内容,解析获得相应的发音人
        String voicerName = getVoicePerson(cb.text);
        // 判断答案是否是路径(语料添加的音频路径) //让语料在路径后加上文本答案,防止sd卡找不到音频
        if (isText == false) { // 不是纯文本,即有音频路径
            String path = "";
            String text = "";
            String temp_text = "";
            if (cb.text.contains("白话")) {
                temp_text = "白话";
            } else if (cb.text.contains("会做什么")) {
                temp_text = "会做什么";
            }
            if (cb.text.contains("几岁") || cb.text.contains("多大")) {
                temp_text = "几岁";
                if (isRepeatQuestion(temp_text, cb.service)) {
                    dealRepeatedproblems();
                    return;
                } else if (!getDeviceString().equals("y20a_dev")) {
                    GetAge();
                    return;
                }
            }
            if (isRepeatQuestion(temp_text, cb.service)) {
                dealRepeatedproblems();
                return;
            }
            text = parseAudioText((cb.answer.text)); // 截取音频对应的文本内容
            path = parseAudioPath((cb.answer.text)); // 截取音频对应的音频路径

            voiceMp(path);
        } else {
            String text = cb.answer.text;
            if (TextUtils.isEmpty(text)) {
                String[] arr = new String[]{"这个问题好难啊!", "这个我还没有学会!"};
                text = arr[new Random().nextInt(2)];
            } else {
                String temp_text = text;
                if (cb.text.contains("功能")) {
                    temp_text = "功能";
                }
                if (isRepeatQuestion(temp_text, cb.service)) {
                    dealRepeatedproblems();
                    return;
                }
            }
            text = isGetSystemCode(cb, text);
            if ( (cb.text.contains("几岁") || cb.text.contains("多大")) && !getDeviceString().equals("y20a_dev")) {
                GetAge();
            } else {
                voiceTTS(text, voicerName);
            }
        }

    }

    /**
     * 重复问题的处理
     */
    private void dealRepeatedproblems() {
        int index = sp.getInt("repeat_answer", 0);
        int randomNum = new Random().nextInt(2);
        switch (randomNum){
            case 0:
                voiceMp(MyData.REPEAT_ANSWER[index]);
                index++;
                if (index >= 8) {
                    index = 0;
                }
                Editor edit = sp.edit();
                edit.putInt("repeat_answer", index);
                edit.commit();
                break;
            case 1:
                int randomNum2 = new Random().nextInt(100);
                final int randomNum3 = new Random().nextInt(MyData.REPEAT_ANSWER2.length);
                if(randomNum2 % 2 == 0){
                    mIsEndSendRecycle = false;
                    voiceMp("repeat_hint.mp3",new MP.MyOnCompletionListener(){
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            specialTTS(MyData.REPEAT_ANSWER2[randomNum3],new MySynthesizerListener(){
                                @Override
                                public void onCompleted(SpeechError error) {
                                    Utils.sendBroadcast(MyIntent.INTENT_RECYCLE, "master", "");
                                }
                            });
                        }
                    });
                }else {
                    voiceTTS(MyData.REPEAT_ANSWER2[randomNum3]);
                }
                break;
        }
    }

    /**
     * 判断是否获取系统版本号
     *
     * @param cb
     * @param text
     */
    private String isGetSystemCode(ChatBean cb, String text) {
        if (cb.text.contains(MasterApplication.mAppContext.getString(R.string.system_verson))) {
            text = getSystemPlatform();
            text = text.replace(".", MasterApplication.mAppContext.getString(R.string.dot));
        }
        return text;
    }

    private String getSystemPlatform() {
        String key = "robot.os_version";
        Class<?> clazz;
        try {
            clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class);
            return (String) method.invoke(clazz.newInstance(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断普通提问是否为重复问题
     *
     *
     */
    private boolean isRepeatQuestion(String text, String service) {
        String temp_answer = sp.getString("answer", "");
        int repeat_count = sp.getInt("count", 0);
        if (service == null) {
            service = "";
        }
        if (service.equals("calc")) {
            return false;
        }
        if (temp_answer == null || temp_answer.equals("") || !temp_answer.equals(text)) {
            Editor edit = sp.edit();
            edit.putString("answer", text);
            edit.putString("operation", "");
            edit.putInt("count", 1);
            edit.commit();
        } else {
            if (repeat_count == 2) {
                return true;
            } else if (repeat_count == 1) {
                Editor edit = sp.edit();
                edit.putString("answer", text);
                edit.putString("operation", "");
                edit.putInt("count", 2);
                edit.commit();
                return false;
            }
            //text = repeatAnswer();
        }
        return false;
    }
    // -------------------------------

    public static String getEmotionAction(String emotion) {
        String emotionAction = "emotion";
        if (emotion.equals("afraid")) {
            emotionAction = Constant.ACTION_AFRAID;
        } else if (emotion.equals("laugh")) {
            emotionAction = Constant.ACTION_LAUGH;
        } else if (emotion.equals("angry")) {
            emotionAction = Constant.ACTION_ANGRY;
        } else if (emotion.equals("cry")) {
            emotionAction = Constant.ACTION_CRY;
        } else if (emotion.equals("hungry")) {
            emotionAction = Constant.ACTION_HUNGRY;
        } else if (emotion.equals("jiayou")) {
            emotionAction = Constant.ACTION_JIAYOU;
        } else if (emotion.equals("ku")) {
            emotionAction = Constant.ACTION_KU;
        } else if (emotion.equals("learn")) {
            emotionAction = Constant.ACTION_LEARN;
        } else if (emotion.equals("meng")) {
            emotionAction = Constant.ACTION_MENG;
        } else if (emotion.equals("normal")) {
            emotionAction = Constant.ACTION_NORMAL;
        } else if (emotion.equals("qinqin")) {
            emotionAction = Constant.ACTION_QINQIN;
        } else if (emotion.equals("sleep")) {
            emotionAction = Constant.ACTION_SLEEP;
        } else if (emotion.equals("quite")) {
            emotionAction = Constant.ACTION_QUITE;
        } else if (emotion.equals("speak")) {
            emotionAction = Constant.ACTION_SPEAK;
        } else if (emotion.equals("yun")) {
            emotionAction = Constant.ACTION_YUN;
        }
        return emotionAction;
    }

    public static void sendEmotion(String emotion) {
        String emotionAction = getEmotionAction(emotion);

        Intent intent = new Intent();
        intent.setAction(emotionAction);
        MasterApplication.mAppContext.sendBroadcast(intent);
        MasterApplication.mAppContext.sendBroadcast(intent);
        MasterApplication.mAppContext.sendBroadcast(intent);
    }

    /**
     * @param answer 例如: 答案1;答案2
     * @return
     */
    public static String[] getAnswerList(String answer) {
        if (answer == null)
            return null;
        return answer.split(";"); // 按 ; 分割
    }

    /**
     * 获取随机的answer
     *
     * @param answer
     * @return
     */
    public static String getRandAnswer(String answer) {
        String[] answerList = getAnswerList(answer);
        if (answerList == null)
            return "";
        int len = answerList.length;
        Random r = new Random();
        int rand = Math.abs(r.nextInt()) % len;
        return answerList[rand];
    }

    private String getAnswers(CmccChatBean ccb) {
        String answers;
        try {
            answers = ccb.semantic.slots.answer1;
            if (ccb.semantic.slots.n == null) {
                return answers;
            }

            String[] answerList = new String[]{ccb.semantic.slots.answer1,
                    ccb.semantic.slots.answer2, ccb.semantic.slots.answer3,
                    ccb.semantic.slots.answer4, ccb.semantic.slots.answer5,
                    ccb.semantic.slots.answer6, ccb.semantic.slots.answer7,
                    ccb.semantic.slots.answer8, ccb.semantic.slots.answer9,
                    ccb.semantic.slots.answer10, ccb.semantic.slots.answer11,
                    ccb.semantic.slots.answer12, ccb.semantic.slots.answer13,
                    ccb.semantic.slots.answer14, ccb.semantic.slots.answer15,
                    ccb.semantic.slots.answer16, ccb.semantic.slots.answer17,
                    ccb.semantic.slots.answer18, ccb.semantic.slots.answer19,
                    ccb.semantic.slots.answer20};

            String sN = ccb.semantic.slots.n;
            int n = Integer.parseInt(sN);
            for (int i = 2; i <= n; i++) {
                answers += answerList[i - 1];
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return answers;
    }

    public String myLocaltime() {
        String sTime = "";
        String dateString = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd EEEE HH:mm:ss");
            dateString = format.format(new Date(System.currentTimeMillis()));

            String[] datetime = dateString.split(" ");

            String[] date = datetime[0].split("-");
            String week = datetime[1];
            String[] time = datetime[2].split(":");

            if (date[1].charAt(0) == '0')
                date[1] = date[1].replace("0", "");

            if (date[2].charAt(0) == '0')
                date[2] = date[2].replace("0", "");

            sTime = date[0] + "年" + date[1] + "月" + date[2] + "日" + week + ",";

            if (time[0].charAt(0) == '0')
                time[0] = time[0].replace("0", "");

            if (time[1].charAt(0) == '0')
                time[1] = time[1].replace("0", "");

            if (time[1].length() == 0) { //处理整点的情况
                sTime += time[0] + "点整";
            } else {
                sTime += time[0] + "点" + time[1] + "分";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sTime;
    }

    boolean isText(String answer) {
        boolean isText = true;
        String recordPath;
        String text;
        // 判断是否是纯文本,截取文本内容
        if (isHaveRecordPath(answer)) {
            isText = false;
            // 有路径,先截取路径和文本
            String[] arr = answer.split(",");
            text = Constant.chat_can_not_know;
            if (arr.length == 2)
                text = arr[1];

        } else { // 纯文本
            text = answer;
            isText = true;
        }
        return isText;
    }

    boolean isHaveRecordPath(String answer) {
        return answer.contains("YongYiDaRecord");
    }

    String getVoicePerson(String text) {
        if (text == null)
            text = "";
        long startT = System.currentTimeMillis();
        String voicePerson = Constant.xiaoai; // 默认发音人
        // 解析文本内容确定要使用的发音人
        if (text.length() <= 12) { // 12字以内的才判断发音人,其他默认
            boolean isHunan = false;
            boolean isTaiwan = false;
            boolean isDongbei = false;
            boolean isSichuan = false;
            boolean isGuangdong = false;
            boolean isHenan = false;

            for (String s : Constant.hunans) {
                if (text.contains(s)) {
                    isHunan = true;
                    break;
                }
            }
            for (String s : Constant.taiwans) {
                if (text.contains(s)) {
                    isTaiwan = true;
                    break;
                }
            }
            for (String s : Constant.dongbeis) {
                if (text.contains(s)) {
                    isDongbei = true;
                    break;
                }
            }
            for (String s : Constant.sichuans) {
                if (text.contains(s)) {
                    isSichuan = true;
                    break;
                }
            }
            for (String s : Constant.guangdongs) {
                if (text.contains(s)) {
                    isGuangdong = true;
                    break;
                }
            }
            for (String s : Constant.henans) {
                if (text.contains(s)) {
                    isHenan = true;
                    break;
                }
            }

            if (isHunan
                    && (!(isTaiwan || isDongbei || isSichuan || isGuangdong || isHenan))) { // 湖南方言
                voicePerson = Constant.aisxqiang;
            } else if (isGuangdong
                    && (!(isTaiwan || isDongbei || isSichuan || isHunan || isHenan))) { // 粤语
                voicePerson = Constant.xiaomei;
            } else if (isDongbei
                    && (!(isTaiwan || isGuangdong || isSichuan || isHunan || isHenan))) { // 东北
                voicePerson = Constant.aisxqian;
            } else if (isSichuan
                    && (!(isTaiwan || isGuangdong || isDongbei || isHunan || isHenan))) { // 四川
                voicePerson = Constant.aisxrong;
            } else if (isTaiwan
                    && (!(isDongbei || isGuangdong || isSichuan || isHunan || isHenan))) { // 台湾
                voicePerson = Constant.aisxlin;
            } else if (isHenan
                    && (!(isTaiwan || isGuangdong || isSichuan || isHunan || isDongbei))) { // 河南
                voicePerson = Constant.aisxkun;
            }
        }

        return voicePerson;
    }

    public void GetAge() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String age_text = "";
                //HttpUtil.SetUrlType(5);
                HttpUtil.SetUrlType(4);
                String result = HttpUtil.submitPostData(null, Domain.getRid(), null, "utf-8", 1);
                MyLog.e("ChatRunnable", "request result age:" + result);
                RobotAgeBean age_info = JsonParser.praseServiceAgeJsonResult(result);
                if (age_info == null) {
                    voiceTTS(MasterApplication.mAppContext.getString(R.string.age_query_error));
                    return;
                }
                if (age_info.getRet() != 0) {
                    voiceTTS(MasterApplication.mAppContext.getString(R.string.age_tip_top) + "3" + MasterApplication.mAppContext.getString(R.string.age_text));
                    return;
                }
                age_info = countAge(age_info);
                if (age_info.getMonth() > 0 && age_info.getMonth() <= 9) {
                    age_text = MasterApplication.mAppContext.getString(R.string.age_tip_top) + age_info.getYear() + MasterApplication.mAppContext.getString(R.string.age_text) + MasterApplication.mAppContext.getString(R.string.age_text_zero) + age_info.getMonth() + MasterApplication.mAppContext.getString(R.string.age_text_month);
                } else if (age_info.getMonth() > 9 && age_info.getMonth() < 12) {
                    age_text = MasterApplication.mAppContext.getString(R.string.age_tip_top) + age_info.getYear() + MasterApplication.mAppContext.getString(R.string.age_text) + age_info.getMonth() + MasterApplication.mAppContext.getString(R.string.age_text_month);
                } else if (age_info.getMonth() == 0) {
                    age_text = MasterApplication.mAppContext.getString(R.string.age_tip_top) + age_info.getYear() + MasterApplication.mAppContext.getString(R.string.age_text);
                }
                voiceTTS(age_text);
            }
        }).start();

        //	return mResult;

    }

    protected RobotAgeBean countAge(RobotAgeBean age_info) {
        long old_time = age_info.getOldTime();
        long new_time = age_info.getNewTime();
        Calendar cal = Calendar.getInstance();
        //   old_time = Long.parseLong("1279712742351");
        cal.setTimeInMillis(old_time);
        int yearOld = cal.get(Calendar.YEAR);
        int monthOld = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthOld = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(new_time);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearOld;
        int month = monthNow - monthOld;
        if (monthNow <= monthOld) {
            if (monthNow == monthOld) {
                if (dayOfMonthNow < dayOfMonthOld) {
                    age--;
                    month = month + 12;
                }
            } else {
                age--;
                month = month + 12;
            }
        }
        age_info.setMonth(month);
        age_info.setYear(age + 3);
        return age_info;
    }

    String parseAudioText(String answer) {
        String[] arr = answer.split(",");
        if (arr.length == 2)
            return arr[1];
        else
            return Constant.chat_what;
    }

    String parseAudioPath(String answer) {
        return answer.split(",")[0];
    }


    private String getDeviceString() {
        return SystemProperties.get("ro.product.device", "y20a_dev");
    }

}
