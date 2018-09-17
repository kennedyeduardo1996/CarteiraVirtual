package com.carteira.minha.carteiravirtual.helper;

import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

//    separa a data e pega apens mes e ano
    public static String mesAnoDataEscolhida(String data){

//        separa a data pela / e colocar cada parametro no array
        String retornoData[] = data.split("/");
        String dia = retornoData[0];//dia 17
        String mes = retornoData[1];//mes 09
        String ano = retornoData[2];//ano 2018

        String mesAno = mes + ano;
        return mesAno;
    }


    //    retira da data os parentes
    public static String DataEscolhida(String data){

//        separa a data pela / e colocar cada parametro no array
        String retornoData[] = data.split("/");
        String dia = retornoData[0];//dia 17
        String mes = retornoData[1];//mes 09
        String ano = retornoData[2];//ano 2018

        String mesAno = dia + mes + ano;
        return mesAno;
    }
}
