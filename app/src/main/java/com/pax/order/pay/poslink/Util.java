package com.pax.order.pay.poslink;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by linhb on 2015-08-21.
 */
public class Util {
    public static int findStringId(String[] arr, String targetValue) {
        return Arrays.asList(arr).indexOf(targetValue);
    }

    public static String paddingLine(String left, String right) {
        return "<tr><td align=\"left\">" + left + "</td><td align=\"right\">" + right + "</td></tr>";
    }

    public static String paddingLine(String text) {
        return "<tr><td colspan=\"2\" align=\"center\">" + text + "</td></tr>";
    }

    public static String findXMl(String data, String node) {
        String extData = "<root>" + data + "</root>";
        ByteArrayInputStream input;
        input = new ByteArrayInputStream(extData.getBytes());

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(input);

            Element rootElement = document.getDocumentElement();
            NodeList items = rootElement.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                if (item.getNodeName().equals(node))
                    return item.getFirstChild().getNodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get value from input text
     *
     * @param edt ""
     * @param def if edt is null, or with no value
     * @return input value
     */
    public static String getStringFromEdit(EditText edt, String def) {
        if (null == edt) {
            return def;
        }
        String value = edt.getText().toString();
        if(value.length() == 0)
            return def;
        return value;
    }

    public static Bitmap generateBmp(String data){
        String div = "\\^";
        String signature_divide[] = data.split(div);
        String x;
        String y;
        String x_y;

        int magin =10;
        int minx;
        int miny;
        int newWidth;
        int newHeight;

        ArrayList<Integer> xVal =new ArrayList<Integer>(signature_divide.length);
        ArrayList<Integer> yVal =new ArrayList<Integer>(signature_divide.length);
        System.out.println("size ="+signature_divide.length);
        for(int i=0; i<signature_divide.length-1; i++)
        {
            try
            {
                x_y = signature_divide[i];
                int pos = x_y.indexOf(",");
                x = x_y.substring(0, pos);
                y = x_y.substring(pos+1);
                if(Integer.parseInt(y)==65535)
                    continue;
                xVal.add(Integer.parseInt(x));
                yVal.add(Integer.parseInt(y));
                //if(Integer.parseInt(x) <= 480 && Integer.parseInt(y) <= 320)
                //bmp.setPixel(Integer.parseInt(x), Integer.parseInt(y), Color.BLACK);

            }catch(Exception e)
            {
                //e.printStackTrace(); ignore correct point
            }
        }

        Collections.sort(yVal);
        Collections.sort(xVal);

        //workaroud  to avoid crash when the sigdata may be invalid.
        if(xVal.isEmpty()) {
            for (int i = 0; i < signature_divide.length-1; i++) {
                yVal.add(0);
                xVal.add(0);
            }
        }

        minx = Integer.parseInt(xVal.get(0).toString());
        miny = Integer.parseInt(yVal.get(0).toString());

        System.out.println("minx="+minx);
        System.out.println("miny="+miny);
        newWidth = Integer.parseInt(xVal.get(xVal.size()-1).toString()) - minx +1 +magin*2;
        newHeight = Integer.parseInt(yVal.get(yVal.size()-1).toString()) - miny +1 + magin*2;

        System.out.println("widht1 = "+newWidth);
        System.out.println("height1 = "+newHeight);

        //BufferedImage bimage = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_RGB);
        Bitmap bmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        //set background white
        for(int i=0;i<newWidth;i++)
            for(int j=0;j<newHeight;j++)
                bmp.setPixel(i, j, Color.WHITE);


        //connect dots
        Point p1 = new Point();
        Point p2 = new Point();
        Paint blackPen = new Paint();
        blackPen.setColor(Color.BLACK);
        blackPen.setStrokeWidth(2);

        Canvas canvas = new Canvas();
        canvas.setBitmap(bmp);
        for(int i=1;i<signature_divide.length-1;i++)
        {
            x_y =  signature_divide[i-1];
            int pos = signature_divide[i-1].indexOf(",");
            y = x_y.substring(pos + 1);
            x = x_y.substring(0, pos);
            if(Integer.parseInt(y) == 65535)
                continue;
            p1.x = Integer.parseInt(x) + magin - minx;
            p1.y = Integer.parseInt(y) + magin - miny;

            x_y =  signature_divide[i];
            pos = signature_divide[i].indexOf(",");
            y = x_y.substring(pos + 1);
            x = x_y.substring(0, pos);
            if(Integer.parseInt(y) == 65535)
                continue;
            p2.x = Integer.parseInt(x)+ magin - minx;
            p2.y = Integer.parseInt(y)+ magin - miny;

            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, blackPen);

        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }
}
