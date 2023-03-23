package ru.mirea.petukhov.a.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMax(100);
        dialog.setTitle("Подарок судьбы");
        dialog.setMessage("Когда процесс завершится ты наконец сдашь Корягина...");
        return dialog;
    }
}
