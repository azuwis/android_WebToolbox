package azuwis.webtoolbox;

import android.content.Intent;
import android.net.Uri;

public class Html5VideoActivity extends BaseOpenActivity {

    @Override
    protected void setupIntent(Intent intent, String url) {
        // turn m.tv.sohu.com into tv.sohu.com
        url.replace("http://m.tv.sohu.com", "http://tv.sohu.com");
        intent.setData(Uri.parse("http://www.html5video.info/convert?url=" + url));
    }

}
