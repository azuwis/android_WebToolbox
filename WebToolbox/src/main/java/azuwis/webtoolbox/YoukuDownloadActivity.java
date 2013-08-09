package azuwis.webtoolbox;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import azuwis.util.JsonReader;

public abstract class YoukuDownloadActivity extends BaseDownloadActivity {

    private static final String TAG = "YoukuDownloadActivity";

    @Override
    protected List<String> getDownloadInfo(String url) {
        Pattern pattern = Pattern.compile(".*/id_([A-Za-z0-9]+)\\.html.*");
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return parseYoukuVideoItem(matcher.group(1), getType());
        } else {
            return null;
        }
    }

    // kanged from https://github.com/jovisayhehe/acfunm/blob/master/src/tv/avfun/api/ApiParser.java
    protected List<String> parseYoukuVideoItem(String vid, int parseMode){
        List<String> downloadInfo = new ArrayList<String>();
        downloadInfo.add(vid);
        String url = "http://v.youku.com/player/getPlayList/VideoIDS/" + vid;
        try {
            JSONObject jsonObject = JsonReader.readJsonFromUrl(url);
            if(jsonObject == null) return downloadInfo;
            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
            Double seed = data.getDouble("seed");
            JSONObject fileids = data.getJSONObject("streamfileids");

            String seg = null;
            String fids = null;
            if (parseMode >= 2 && fileids.has("hd2")) {
                seg = "hd2";
                if (BuildConfig.DEBUG) Log.i(TAG, "hd2");
            } else if (parseMode >= 1 && fileids.has("mp4")) {
                seg = "mp4";
                if (BuildConfig.DEBUG) Log.i(TAG, "mp4");
            } else if (fileids.has("flv")) {
                seg = "flv";
                if (BuildConfig.DEBUG) Log.i(TAG, "flv");
            }
            fids = fileids.getString(seg);
            String realFileid =getFileID(fids, seed);

            JSONObject segs = data.getJSONObject("segs");

            JSONArray vArray = segs.getJSONArray(seg);

            String vPath = seg.equals("mp4")?"mp4":"flv";
            for(int i=0;i<vArray.length();i++){
                JSONObject part = vArray.getJSONObject(i);
                String k = part.getString("k");
                String k2 = part.getString("k2");
                String u = "http://f.youku.com/player/getFlvPath/sid/00_" +
                        String.format("%02d", i) + "/st/" + vPath + "/fileid/" +
                        realFileid.substring(0, 8) + String.format("%02d", i) +
                        realFileid.substring(10) + "?K=" + k + ",k2:" + k2;
                if (BuildConfig.DEBUG) Log.i(TAG, "url= "+u);
                downloadInfo.add(u);
            }
        } catch (Exception e) {
            if(BuildConfig.DEBUG)
                Log.w(TAG, "failed to parse youku video " + url, e);
        }
        return downloadInfo;
    }

    public static String getFileIDMixString(double seed) {
        StringBuilder mixed = new StringBuilder();
        StringBuilder source = new StringBuilder(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890");
        int index, len = source.length();
        for (int i = 0; i < len; ++i) {
            seed = (seed * 211 + 30031) % 65536;
            index = (int) Math.floor(seed / 65536 * source.length());
            mixed.append(source.charAt(index));
            source.deleteCharAt(index);
        }
        return mixed.toString();
    }

    public static String getFileID(String fileid, double seed) {
        String mixed = getFileIDMixString(seed);
        String[] ids = fileid.split("\\*");
        StringBuilder realId = new StringBuilder();
        int idx;
        for (int i = 0; i < ids.length; i++) {
            idx = Integer.parseInt(ids[i]);
            realId.append(mixed.charAt(idx));
        }
        return realId.toString();
    }

    protected abstract int getType();

}
