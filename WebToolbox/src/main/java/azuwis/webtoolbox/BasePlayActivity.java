package azuwis.webtoolbox;

import android.content.Intent;
import android.net.Uri;

public abstract class BasePlayActivity extends BaseOpenActivity {

    @Override
    protected void setupIntent(Intent intent, String url) throws Exception {
        String playUrl = getPlayUrl(url);
        if (playUrl == null) {
            throw new Exception(getString(R.string.error_getting_play_url));
        }
        // trick to bring up built-in player(Gallery)
        intent.setDataAndType(Uri.parse(playUrl), "video/*");
        // start as new task, ease switching back to browser
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    protected abstract String getPlayUrl(String url);
}
