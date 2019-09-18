package com.pax.order.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by Terry on 10/10/2017.
 */

public class CardCheck {



    private String card_type = null;
    public boolean check_sum_status = true;

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public String getCardBrandType(String card_number){

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if (card_number.isEmpty() || card_number==null || card_number=="NULL" || card_number.length()<15){
            return null;
        }

        int card_index = Integer.parseInt(card_number.substring(0, 8));
        System.out.println("card_index:"+card_index);
        // 1. get bin-range file from assets
        InputStream bin_range_stream= getClass().getResourceAsStream("/assets/sys_bin_range.txt");
        System.out.println("bin_range_stream: " + bin_range_stream);
        String bin_range = getStringFromInputStream(bin_range_stream);

        // 2. parse bing file and check the card type
        int num = Integer.parseInt(bin_range.substring(2,4));
        int sub_length = Integer.parseInt(bin_range.substring(6,8));
        String bin_pure = bin_range.substring(8);
        System.out.println("card_month:"+bin_range);
        System.out.println("num:"+num);
        System.out.println("sub_length:"+sub_length);
        System.out.println("bin_pure:"+bin_pure);

        for(int i=1; i<= num; i++ ){
            String brand_info = bin_pure.substring((i-1)*61, i*61);
            String card_type_take = brand_info.substring(4, 20).replace("0","");
            int begin = Integer.parseInt(brand_info.substring(24, 32));
            int end = Integer.parseInt(brand_info.substring(36, 44));

            System.out.println("brand_info:"+brand_info);
            System.out.println("card_type_take:"+card_type_take);
            System.out.println("begin:"+begin);
            System.out.println("end:"+end);


            if ((card_index>=begin) && (card_index <= end)){

                card_type = card_type_take;

                if (brand_info.substring(60,61).equals("N")){
                    check_sum_status = false;
                }

                break;
            }
        }

        return card_type;

    }


    public boolean checkExpireationTime(String expired_date){
        // check the expiration date
        if(expired_date==null){
            return false;
        }

        int card_year = Integer.parseInt(expired_date.substring(2));
        int card_month = Integer.parseInt(expired_date.substring(0,2));
        System.out.println("card_year:"+card_year);
        System.out.println("card_month:"+card_month);

        Calendar now = Calendar.getInstance();
        int current_year = now.get(Calendar.YEAR);
        int current_month = now.get(Calendar.MONTH) + 1;

        System.out.println("current_year:"+current_year);
        System.out.println("current_month:"+current_month);

        if ((((card_year+2000) == current_year) && (current_month <= card_month)) ||((card_year+2000) > current_year) ){
            return true;

        }else{
            return false;
        }
    }


    public boolean checkSumCardInfo(String card_num){

        boolean checkSum = true;

        if(check_sum_status){
            checkSum = LuhnCheck(card_num);
        }

        return checkSum;


    }


    public static boolean LuhnCheck(String ccNumber)
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            long n = Long.parseLong(ccNumber.substring(i, i + 1));
            System.out.println("n value:"+n);
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            System.out.println("sum value:"+sum);
            alternate = !alternate;
        }
        System.out.println("sum % 10 == 0 value:"+(sum % 10 == 0));
        return (sum % 10 == 0);
    }


    public String getCardType() {
        return card_type;
    }

    public void setCardType(String card_type) {
        this.card_type = card_type;
    }

    public static String parseTrack2Number(String track2_row){
        String track2;
        if(track2_row.contains("$")){
            String[] row = track2_row.split("\\$");
            System.out.println("row[] :"+row[0]);
            track2_row = row[1];
        }

        if(track2_row.contains("%")){
            String[] row = track2_row.split("%");
            System.out.println("row[] :"+row[0]);
            track2_row = row[1];
        }

        String[] card_number_row = track2_row.split("=");
        String card_number = card_number_row[0].replaceAll("\\D+","");
        String date_raw = card_number_row[1].replaceAll("\\D+","");

        track2 = card_number + "=" + date_raw.substring(0, 4);



        return track2;

    }






}
