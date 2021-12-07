package com.kyonggi.restaurantmemo.Activity;

import static android.content.Intent.ACTION_SENDTO;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.kyonggi.restaurantmemo.R;
import com.kyonggi.restaurantmemo.Room.ItemDatabase;

public class SettingsFragment extends PreferenceFragmentCompat {

    ItemDatabase itemDatabase;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        itemDatabase = ItemDatabase.getInstance(getContext());
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals("email")) {
            Intent email = new Intent(ACTION_SENDTO);
            email.setData(Uri.parse("mailto:joonhoo0123@gmail.com"));
            FragmentActivity fragmentActivity = getActivity() != null ? getActivity() : null;
            if (fragmentActivity != null) {
                fragmentActivity.startActivity(email);
            }
        } else if (preference.getKey().equals("delete")) {
            deleteAllDialog();
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void deleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("전체 데이터를 삭제하시겠습니까?");
        builder.setNegativeButton("NO",(dialogInterface, i) -> {});
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            // 데이터 베이스에 삭제하기
            itemDatabase.itemDAO().deleteAll();
            Toast.makeText(getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
        });
        builder.create().show();
    }
}