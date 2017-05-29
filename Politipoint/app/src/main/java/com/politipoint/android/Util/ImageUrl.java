package com.politipoint.android.Util;



public class ImageUrl {

    private String Strange;

    public static String getImage(String name){
        String result = "";

        switch (name){
            case "S001202":
                result = "http://vote-al.org/Image.aspx?Id=ALSessionsJeff&Col=Headshot100&Def=Headshot100";
                break;
            case  "S000320":
                result = "http://vote-al.org/Image.aspx?Id=ALShelbyRichardC&Col=Headshot100&Def=Headshot100";
                break;
            case "M001176":
                result = "http://vote-al.org/Image.aspx?Id=ORMerkleyJeff&Col=Headshot100&Def=Headshot100";
                break;
            case  "W000779":
                result = "http://vote-al.org/Image.aspx?Id=ORWydenRon&Col=Headshot100&Def=Headshot100";
                break;
            case "H001041":
                result = "http://vote-al.org/Image.aspx?Id=NVHellerDean&Col=Headshot100&Def=Headshot100";
                break;
            case  "C001113":
                result = "http://vote-al.org/Image.aspx?Id=NVCortezmastoCatherine&Col=Headshot100&Def=Headshot100";
                break;
            case "S000033":
                result = "http://vote-al.org/Image.aspx?Id=VTSandersBernard&Col=Headshot100&Def=Headshot100";
                break;
            case  "L000174":
                result = "http://vote-al.org/Image.aspx?Id=VTLeahyPatrickJ&Col=Headshot100&Def=Headshot100";
                break;
            case "C000174":
                result = "http://vote-al.org/Image.aspx?Id=DECarperThomasR&Col=Headshot100&Def=Headshot100";
                break;
            case  "C001088":
                result = "http://vote-al.org/Image.aspx?Id=DECoonsChristopherA&Col=Headshot100&Def=Headshot100";
                break;
            case "P000612":
                result = "http://vote-al.org/Image.aspx?Id=GAPerdueDavidAlfred&Col=Headshot100&Def=Headshot100";
                break;
            case  "I000055":
                result = "http://vote-al.org/Image.aspx?Id=GAIsaksonJohnny&Col=Headshot100&Def=Headshot100";
                break;
            case "T000476":
                result = "http://vote-al.org/Image.aspx?Id=NCTillisThom&Col=Headshot100&Def=Headshot100";
                break;
            case  "B001135":
                result = "http://vote-al.org/Image.aspx?Id=NCBurrRichard&Col=Headshot100&Def=Headshot100";
                break;
            case "H001069":
                result = "http://vote-al.org/Image.aspx?Id=NDHeitkampHeidi&Col=Headshot100&Def=Headshot100";
                break;
            case  "H001061":
                result = "http://vote-al.org/Image.aspx?Id=NDHoevenJohn&Col=Headshot100&Def=Headshot100";
                break;
        }

        return result;
    }

}
