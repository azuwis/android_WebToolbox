package azuwis.webtoolbox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public abstract class BaseOpenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        String url = uri.toString();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            setupIntent(intent, url);
            startActivity(intent);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        finish();
    }

    protected abstract void setupIntent(Intent intent, String url) throws Exception;

}
