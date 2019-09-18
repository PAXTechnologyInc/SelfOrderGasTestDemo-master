package com.pax.order.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.easylink.R;
//import com.pax.easylink.paxandroiddemo.ChooseCardActivity;
//import com.pax.easylink.paxandroiddemo.ProcessCardActivity;
import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.menu.MenuActivity;
import com.pax.order.menu.ShoppingCartActivity;

//import dmax.dialog.SpotsDialog;

/**
 * Created by lixc on 2017/2/15.
 * This
 */

public class DialogClass {

    private DialogClass() {

    }
    /*
    public static void popPromptDialog(Context context, String title){
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setCancelable(false);
        alert.setTitle(title);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_sure),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                    }
                });
        alert.show();
    }
    */
    public static void showPromptDialog(Context context, String title_string){

        final AlertDialog alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        alert.setCancelable(false);
        // alert.setContentView(R.layout.custom_dialog);

        alert.setTitle(title_string);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                    }
                });

        alert.show();

    }



/*
    public static void showPromptDialog(final Context context, String title_string, final Intent intent ){

        final AlertDialog alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        alert.setCancelable(false);
        // alert.setContentView(R.layout.custom_dialog);

        alert.setTitle(title_string);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        context.startActivity(intent);
                        alert.dismiss();
                    }
                });

        alert.show();

    }


    public static void showCardChooseOption(final Context context, final GlobalVariable global_var){

        // prompt up the dialog to select option
        final CharSequence options[] = new CharSequence[] {"Insert/Swipe/Tap card", "Manual Input Card Info"};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                System.out.println("This is the option:" + options[which]);
                if (options[which] == "Manual Input Card Info"){
                    Intent intent = new Intent(context.getApplicationContext(), ProcessCardActivity.class);
                    context.startActivity(intent);
                    global_var.setSwipCardStatus(false);

                }else{
                    // this is insert/swipe card process
                    System.out.println("This is the option in else :" + options[which]);
                    System.out.println("this is the choosecard");
                    Intent intent = new Intent(context.getApplicationContext(), ChooseCardActivity.class);
                    context.startActivity(intent);
                    global_var.setSwipCardStatus(true);

                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();

    }


    // card present or not
    public static void showCardPresentOption(final Context context, final GlobalVariable global_var, final String avsOnCardPrsnt){

        // prompt up the dialog to select option
        final CharSequence options[] = new CharSequence[] {"Card Present", "Card Not Present"};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                System.out.println("This is the option:" + options[which]);
                if (options[which] == "Card Present"){

                    global_var.setCardPresentStatus(true);

                    if(avsOnCardPrsnt.equals(APPConstants.enable)){
                        showAVSInputDialog(context, global_var);
                    }

                }else{
                    // this is insert/swipe card process
                    global_var.setCardPresentStatus(false);
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();


    }




    // address input dialog
    public static void showAVSInputDialog(final Context context, final GlobalVariable global_var){

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.avs_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setView(promptView);

        // get edit input info
        final EditText address_input = (EditText) promptView.findViewById(R.id.address_input);
        final EditText zipcode_input = (EditText) promptView.findViewById(R.id.zipcode_input);

        // setup a dialog window
        alertDialogBuilder.setTitle("Please Input Address Info");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        global_var.setAddress(address_input.getText().toString());
                        global_var.setZip(zipcode_input.getText().toString());

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    private static ConditionVariable conditionVariable = new ConditionVariable();

    // system password input dialog
    public static void showPasswordInputDialog(final Context context, final GlobalVariable global_var){

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.avs_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setView(promptView);

        // get edit input info
        TextView address_view = (TextView) promptView.findViewById(R.id.address_view);
        TextView zipcode_view = (TextView) promptView.findViewById(R.id.zipcode_view);
        final EditText address_input = (EditText) promptView.findViewById(R.id.address_input);
        final EditText zipcode_input = (EditText) promptView.findViewById(R.id.zipcode_input);

        // view gone
        zipcode_view.setVisibility(View.GONE);
        zipcode_input.setVisibility(View.GONE);
        address_view.setVisibility(View.GONE);

        // setup a dialog window
        alertDialogBuilder.setTitle(R.string.prompt_password_input);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String user_input = address_input.getText().toString();
                        global_var.setInputSysPassword(user_input);
                        global_var.setZip(zipcode_input.getText().toString());


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        // conditionVariable.block();


    }

    public static void showCVVExplainReason(final Context context, final GlobalVariable global_var){

        // prompt up the dialog to select option
        final CharSequence options[] = new CharSequence[] {APPConstants.WANTBYPASS, APPConstants.CANNOTREAD, APPConstants.NOTEXIST};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                global_var.setCVVReason(options[which].toString());
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();

    }


    public static void showExitDialog(Context context){
        final AlertDialog alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK).create();
        alert.setCancelable(false);
        alert.setTitle(R.string.prompt_exit);
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which) {
                alert.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_sure),
                new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which) {
                alert.dismiss();

                // exit the app and kill the process APP
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });
        alert.show();
    }

    public static void showProgressDialog(final ProgressDialog progressDialog, final String message){
        if (progressDialog != null) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(message);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void showSpotsProgressDialog(Context context, String title){

        new SpotsDialog(context, R.style.BTSearch).show();

    }

    */
}
