package com.carteira.minha.carteiravirtual.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFreagment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(), ano, mes, dia);
    }
}
