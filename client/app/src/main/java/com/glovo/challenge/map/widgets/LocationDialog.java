package com.glovo.challenge.map.widgets;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.glovo.challenge.R;

public class LocationDialog extends AppCompatDialogFragment {

    public static String TAG = LocationDialog.class.getSimpleName();

    private static final int REQUEST_CODE = 633;

    private Button locateButton;
    private Button notNowButton;

    private Callback callback;

    public static LocationDialog newInstance() {
        return new LocationDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locateButton = view.findViewById(R.id.locate);
        notNowButton = view.findViewById(R.id.not_now);

        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE);
            }
        });

        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                callback.onPermissionNotGranted();
                dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
        @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermissionGranted();
            } else {
                callback.onPermissionNotGranted();
            }
            dismiss();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            callback = (Callback) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(
                context.getClass().getSimpleName() + " must implement " + Callback.class.getSimpleName());
        }
    }

    public interface Callback {

        void onPermissionGranted();

        void onPermissionNotGranted();
    }
}
