package com.politipoint.android.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.politipoint.android.app.R;

public class FilterFragment extends DialogFragment implements DialogInterface.OnClickListener {


    private EditText editQuantity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editQuantity = new EditText(getActivity());
        editQuantity.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage("Please Enter State")
                .setPositiveButton("OK", this).setNegativeButton("CANCEL", null).setView(editQuantity).create();

    }

    @Override
    public void onClick(DialogInterface dialog, int position) {
        String value = editQuantity.getText().toString();
        Log.d("Quantity: ", value);
        MainActivity callingActivity = (MainActivity) getActivity();
        callingActivity.onUserSelectValue(value);
        dialog.dismiss();
    }
}
