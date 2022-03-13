package com.simple_loyalty_cards_manager.myloyalitycards;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CodePrinter {

    public static void stampaQR(String code, ImageView image){
        QRGEncoder qrgEncoder = new QRGEncoder(code,null, QRGContents.Type.TEXT,200);
        try{
            Bitmap qrBits = qrgEncoder.getBitmap();
            image.setImageBitmap(qrBits);
        }
        catch (Exception e){  }
    }

    public static void stampaBarCode(String code, ImageView image){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        int imgWidth = 800;
        int imgHeight = 400;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.CODE_128, imgWidth, imgHeight);
            Bitmap barcodeBits = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.RGB_565);
            for (int i = 0; i < imgWidth; i++) {
                for (int j = 0; j < imgHeight; j++)
                    barcodeBits.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK:Color.WHITE);
            }
            image.setImageBitmap(barcodeBits);
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public static void stampaCodiceCarta(Card card, ImageView image) {
        if (card.getCodeType() == 0)
            stampaQR(card.getCode(), image);

        else {
            stampaBarCode(card.getCode(), image);
        }
    }
}
