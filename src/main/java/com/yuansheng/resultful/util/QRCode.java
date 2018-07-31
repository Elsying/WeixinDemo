package com.yuansheng.resultful.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;


public class QRCode {
	/*
	 * 生成二维码
	 */
	public static void createQRcode(HttpServletResponse response) {
		int width=300;      //图片的宽度
        int height=300;     //图片的高度
        String format="png";    //图片的格式

        String appid="wxe817dbc86823b97b";
        String url=NetUtil.urlEncodeUTF8("http://lsying.j2eeall.com/users/weixinLogin");
        //内容
        String content = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        HashMap hints=new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //指定字符编码为“utf-8”
        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);  //指定二维码的纠错等级为中级
        hints.put(EncodeHintType.MARGIN, 2);    //设置图片的边距
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	
	
	/*
	 * 解析二维码
	 */
	public static Result getQRresult(String filePath) {
		MultiFormatReader formatReader=new MultiFormatReader();
        File file=new File("D:/2barcode/code.png");
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer
                                    (new BufferedImageLuminanceSource(image)));

            HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //指定字符编码为“utf-8”

            Result result=formatReader.decode(binaryBitmap,hints);

            System.out.println("解析结果："+result.toString());
            System.out.println("二维码格式："+result.getBarcodeFormat());
            System.out.println("二维码文本内容："+result.getText());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
