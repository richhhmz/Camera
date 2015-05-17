package himes_industries.cameraserver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import himes_industries.cameraserver.util.Connect;

public class CameraServerActivity extends Activity {
    private Button camera_server_button;
    private Uri imageUri;
    private Bitmap bitmap;
    private File photo;
    private final static String TAG = "CameraMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
    };

    public void setButtonText(String text){
        Button mButton=(Button)findViewById(R.id.camera_server_button);
        mButton.setText(text);
    }

    public void activateCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            getContentResolver().notifyChange(imageUri, null);

            ContentResolver cr = getContentResolver();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                synchronized (Connect.sync) {
                    Connect.sync.notify();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

    }

    public File getPhoto() {
        return photo;
    }
}
