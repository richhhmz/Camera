package himes_industries.cameraserver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import himes_industries.cameraserver.util.Connect;

public class CameraServerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_server);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        new Connect(this).execute();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {
            Bundle b = message.getData();
            String text = (String)b.get("response");
            setButtonText(text);
            super.handleMessage(message);
        };
    };

    public void setButtonText(String text){
        Button mButton=(Button)findViewById(R.id.camera_server_button);
        mButton.setText(text);
    }

}
