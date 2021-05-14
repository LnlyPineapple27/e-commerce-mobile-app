package com.example.greentea.Buyers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greentea.NotificationModels.MixedNotification;
import com.example.greentea.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class MixedNotificationFragment extends Fragment {
    public static int MODE_ALL = 0, MODE_ORDER = 1, MODE_DISCOUNTS = 2;
    ArrayList<MixedNotification> notilist;
    RecyclerView rv;
    int mode;
    public static MixedNotificationFragment new_instance(ArrayList<MixedNotification> listNotif, int mode){
        MixedNotificationFragment f = new MixedNotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable("NOTIF_LIST", (Serializable)listNotif);
        args.putInt("NOTIF_MODE", mode);
        f.setArguments(args);
        return f;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        mode = args.getInt("NOTIF_MODE", MODE_ALL);
        ArrayList<MixedNotification> all_notilist = (ArrayList<MixedNotification>) args.getSerializable("NOTIF_LIST");
        notilist = filter_data(all_notilist, mode);

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        rv = rootView.findViewById(R.id.notification_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(lm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), lm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(new NotificationAdapter(getContext() , notilist));
        return rootView;
    }
    ArrayList<MixedNotification> filter_data(ArrayList<MixedNotification> input, int mode) {
        if (mode == MODE_ALL)
            return input;
        else if (mode == MODE_DISCOUNTS) {
            ArrayList<MixedNotification> result = new ArrayList<>();
            for (MixedNotification i : input) {
                if (i.getType() == MixedNotification.TYPE_DISCOUNT)
                    result.add(i);
            }
            return result;
        }
        else {
            ArrayList<MixedNotification> result = new ArrayList<>();
            for (MixedNotification i : input) {
                if (i.getType() == MixedNotification.TYPE_ORDER)
                    result.add(i);
            }
            return result;
        }
    }
}
