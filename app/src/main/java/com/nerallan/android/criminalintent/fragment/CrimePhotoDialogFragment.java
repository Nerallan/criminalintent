package com.nerallan.android.criminalintent.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nerallan.android.criminalintent.PictureUtils;
import com.nerallan.android.criminalintent.R;

import java.io.File;

/**
 * Created by Nerallan on 11/5/2018.
 */

public class CrimePhotoDialogFragment extends DialogFragment{

    private static final String ARG_PHOTO_FILE = "photoFile";

    private ImageView mPhotoView;
    private File mPhotoFile;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mPhotoView = (ImageView) view.findViewById(R.id.photo_view_dialog);

        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
        return new AlertDialog.Builder(getActivity(), R.style.ThemeOverlay_AppCompat_Dialog)
                .setView(view)
                .create();
    }


    public static CrimePhotoDialogFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE, photoFile);

        CrimePhotoDialogFragment fragment = new CrimePhotoDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
