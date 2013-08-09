package azuwis.webtoolbox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public abstract class BasePlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        String url = uri.toString();
        String playUrl = getPlayUrl(url);
        if (playUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // trick to bring up built-in player(Gallery)
            intent.setDataAndType(Uri.parse(playUrl), "video/*");
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, R.string.error_getting_play_url, Toast.LENGTH_SHORT);
            toast.show();
        }
        finish();
    }

    protected abstract String getPlayUrl(String url);

}
