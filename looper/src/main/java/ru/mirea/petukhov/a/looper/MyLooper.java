package ru.mirea.petukhov.a.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread{
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler){
        mainHandler = mainThreadHandler;
    }

    public void run(){
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String data = msg.getData().getString("KEY");

                int delay;
                try {
                    delay = Integer.parseInt(msg.getData().getString("AGE"));
                }
                catch (Exception e){
                    delay = 0;
                }

                Log.d("MyLoop delay: ", Integer.toString(delay) + "ms");

                Log.d("MyLooper get message: ", data);
                int count = data.length();
                Message message = new Message();
                Bundle bundle = new Bundle();

                if(delay == 0){
                    bundle.putString("result", String.format("The number of letters in the word %s is %d ",data,count));
                }
                else{
                    bundle.putString("result", data + " " + delay);
                }
                
                message.setData(bundle);
// Send the message back to main thread message queue use main thread message Handler.
                //mainHandler.sendMessage(message);
                mainHandler.sendMessageDelayed(message, delay);
            }
        };

        Looper.loop();
    }

}
