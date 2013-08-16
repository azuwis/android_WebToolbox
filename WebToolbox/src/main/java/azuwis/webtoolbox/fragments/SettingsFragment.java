package azuwis.webtoolbox.fragments;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import azuwis.util.PreferenceListFragment;
import azuwis.webtoolbox.R;

public class SettingsFragment extends PreferenceListFragment {

    private PackageManager pm;

    public static SettingsFragment newInstance(int xml) {
        SettingsFragment f = new SettingsFragment(xml);
        Bundle b = new Bundle();
        b.putInt("xml", xml);
        f.setArguments(b);
        return f;
    }

    public SettingsFragment(int xmlId) {
        super(xmlId);
    }

    public SettingsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = getActivity().getPackageManager();

        CheckBoxPreference youkuPlayHd = (CheckBoxPreference) findPreference("youku_play_hd");
        setupComponentCheckbox(youkuPlayHd, "azuwis.webtoolbox.YoukuHdPlayActivity");

        CheckBoxPreference youkuPlay = (CheckBoxPreference) findPreference("youku_play");
        setupComponentCheckbox(youkuPlay, "azuwis.webtoolbox.YoukuMp4PlayActivity");

        CheckBoxPreference youkuDownloadHd = (CheckBoxPreference) findPreference("youku_download_hd");
        setupComponentCheckbox(youkuDownloadHd, "azuwis.webtoolbox.YoukuHdDownloadActivity");

        CheckBoxPreference youkuDownload = (CheckBoxPreference) findPreference("youku_download");
        setupComponentCheckbox(youkuDownload, "azuwis.webtoolbox.YoukuMp4DownloadActivity");
    }

    private void setEnabled(String name, boolean bool) {
        ComponentName componentName = new ComponentName(getActivity().getApplicationContext(), name);
        pm.setComponentEnabledSetting(componentName,
                bool ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private boolean getEnabled(String name) {
        ComponentName componentName = new ComponentName(getActivity().getApplicationContext(), name);
        return (pm.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    private void setupComponentCheckbox(CheckBoxPreference pref, final String name) {
        pref.setChecked(getEnabled(name));
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                setEnabled(name, checked);
                return true;
            }
        });
    }

}
