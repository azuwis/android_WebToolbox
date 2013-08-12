package azuwis.webtoolbox;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public abstract class BaseDownloadActivity extends Activity {

    private static final String TAG = "BaseDownloadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        String url = uri.toString();
        new downloadTask().execute(url);
    }

    protected class downloadTask extends AsyncTask<String, Object, List<String>> {

        private ProgressDialog dialog;

        @Override
        protected List<String> doInBackground(String... param) {
            List<String> downloadInfo;
            String url = param[0];
            downloadInfo = getDownloadInfo(url);
            return downloadInfo;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(BaseDownloadActivity.this);
            dialog.setMessage("Working...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<String> downloadInfo) {
            dialog.dismiss();
            if (downloadInfo.size() > 1) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                String path = Environment.DIRECTORY_MOVIES + "/" + downloadInfo.get(0);
                new File(path).mkdirs();
                for (int i = 1; i < downloadInfo.size(); i++) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadInfo.get(i)));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                    request.setDestinationInExternalPublicDir(path, String.format("%03d.mp4", i));
                    request.setTitle(String.format("%s #%03d", downloadInfo.get(0), i));
                    downloadManager.enqueue(request);
                }
            } else {
                Toast toast = Toast.makeText(BaseDownloadActivity.this, R.string.error_getting_download_urls, Toast.LENGTH_SHORT);
                toast.show();
            }
            finish();
        }
    }

    protected abstract List<String> getDownloadInfo(String url);

}
