package com.pax.order.pay.poslink;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.pax.order.R;
import com.pax.poslink.PaymentResponse;
import com.pax.poslink.PosLink;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by linhb on 2015-09-05.
 */
public class PaymentReceiptActivity extends BaseActivity implements View.OnClickListener {

    private String sigfilepath = null;
    private String mLastRequestTender = "mLastRequestTender";
    private String mLastRequestTrans = "mLastRequestTrans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt);
        Bundle bundle=getIntent().getExtras();
        String extData=bundle.getString("Payment_Receipt");

        Button m_Back = (Button)findViewById(R.id.payment_receipt_back);
        m_Back.setOnClickListener(this);

        WebView m_Receipt = (WebView)findViewById(R.id.payment_receipt);
        m_Receipt.loadDataWithBaseURL(null, extData, "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_receipt_back:
                PaymentReceiptActivity.this.finish();
                break;
        }
    }


    public String generateReceipt(PosLink poslink, Context context) {
        PaymentResponse response = poslink.PaymentResponse;
        String receiptWidth = "100%";
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            receiptWidth = "40%";
        }
        String content = "<html><body><table width=\"" + receiptWidth + "\" align=\"center\" border=\"0\"><tbody>";

        //time stamp
        String temp = response.Timestamp;
        String left, right;
        if (temp.length() > 0) {
            left = temp.substring(4, 6) + "/" + temp.substring(6, 8) + "/" + temp.substring(0, 4);
            right = temp.substring(8, 10) + ":" + temp.substring(10, 12) + ":" + temp.substring(12, 14);

            content += Util.paddingLine(left, right);
            content += Util.paddingLine("&nbsp;");
        }

        // edcType + transType
        left = mLastRequestTender + " " + mLastRequestTrans + ":";
        content += Util.paddingLine(left, "");
        content += Util.paddingLine("&nbsp;");

        //transaction number;
        left = "Transaction #:";
        right = response.RefNum;
        content += Util.paddingLine(left, right);

        //card Type:
        if (response.CardType.length() > 0) {
            left = "Card Type:";
            right = response.CardType;
            content += Util.paddingLine(left, right);
        }

        //account type
        left = "Account:";
        right = response.BogusAccountNum;
        content += Util.paddingLine(left, right);

        //entry mode
        //left = "Entry:";
        // right =
        left = "Entry";
        temp = Util.findXMl(poslink.PaymentResponse.ExtData, "PLEntryMode");

        if (temp.contains("0")) {
            right = "Manual";
        } else if (temp.contains("1")) {
            right = "Swipe";
        } else if (temp.contains("2")) {
            right = "Contactless";
        } else if (temp.contains("3")) {
            right = "Scanner";
        } else if (temp.contains("4")) {
            right = "Chip";
        } else if (temp.contains("5")) {
            right = "Chip Fall Back Swipe";
        }

        if (temp.length() > 0)
            content += Util.paddingLine(left, right);

        if (response.ResultCode.contains("000000")) {
            //amount
            temp = response.ApprovedAmount;
            if (temp.length() > 0) {
                left = "Amount:";

                int len = temp.length();
                if (len == 2) {
                    right = "$" + "0." + temp;
                } else if (len == 1) {
                    right = "$" + "0.0" + temp;
                } else {
                    right = "$" + temp.substring(0, len - 2) + "." + temp.substring(len - 2);
                }
                content += Util.paddingLine(left, right);
            }

            //order number
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "MOTOECommerceOrderNum");
            if (right.length() > 0) {
                left = "Order Number";
                content += Util.paddingLine(left, right);
            }

            content += Util.paddingLine("&nbsp;");

            //ref
            right = response.HostCode;
            if (right.length() > 0) {
                left = "Ref. Number:";
                content += Util.paddingLine(left, right);
            }

            //auth code
            right = response.AuthCode;
            if (right.length() > 0) {
                left = "Auth Code:";
                content += Util.paddingLine(left, right);
            }

            //response
            right = response.Message;
            if (right.length() > 0) {
                left = "Response:";
                content += Util.paddingLine(left, right);
            }

            //TC
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "TC");
            if (right.length() > 0) {
                left = "TC:";
                content += Util.paddingLine(left, right);
            }

            //TVR
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "TVR");
            if (right.length() > 0) {
                left = "TVR:";
                content += Util.paddingLine(left, right);
            }

            //AID
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "AID");
            if (right.length() > 0) {
                left = "AID:";
                content += Util.paddingLine(left, right);
            }

            //TSI
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "TSI");
            if (right.length() > 0) {
                left = "TSI:";
                content += Util.paddingLine(left, right);
            }

            //TSI
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "ATC");
            if (right.length() > 0) {
                left = "ATC:";
                content += Util.paddingLine(left, right);
            }

            //APPLIB
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "APPLAB");
            if (right.length() > 0) {
                left = "APPLAB:";
                content += Util.paddingLine(left, right);
            }

            //APPPN
            right = Util.findXMl(poslink.PaymentResponse.ExtData, "APPPN");
            if (right.length() > 0) {
                left = "APPPN:";
                content += Util.paddingLine(left, right);
            }

            content += Util.paddingLine("&nbsp;");

            content += Util.paddingLine("I AGREE TO PAY ABOVE TOTAL");
            content += Util.paddingLine("AMOUNT ACCORDING TO CARD ISSUER");
            content += Util.paddingLine("AGREEMENT (MERCHANT AGREEMENT");
            content += Util.paddingLine("IF CREDIT VOUCHER)");
            content += Util.paddingLine("&nbsp;");
            content += Util.paddingLine("X..............................", "");
            content += Util.paddingLine("SIGNATURE");
        } else if (!response.ResultCode.equals("000000")) {
            content += Util.paddingLine("///////////////////////////////");
            right = response.Message;
            if (right.length() > 0) {
                left = "Response:";
                content += Util.paddingLine(left, right);
            }
            content += Util.paddingLine("///////////////////////////////");
        }
        content += "</tbody></table></body></html>";
        return content;
    }

    private int convertSigToPic(String sigdata, String type, String outFile) throws IOException {
        if (sigdata.length() == 0)
            return -1;
        if (outFile.length() == 0)
            return -2;

        Bitmap bmp = Util.generateBmp(sigdata);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.length() == 0 || type.toLowerCase().equals("bmp")) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.toLowerCase().equals("ico")) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.toLowerCase().equals("jpg")) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } else if (type.toLowerCase().equals("png")) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else {
            return -4;   //fail
        }

        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -5;
        }

        return 0;  //sucess
    }




}
