package cn.djrj.jrjrm.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来产生随机验证码
 * @author lynn
 */
public class VerificationCode {
	
	private Random random;
	private int width;
	private int height;
	private int codeNum;
	private int fontHeight;
	private int fontWidth;
	private String code;
	private char[] codeSequence={'1','2','3','4','5','6','7','8','9','A','B','C','D',
								'E','F','G','H','I','J','K','L','M','N','O','P','Q',
								'R','S','T','U','V','W','X','Y','Z'};
	
	public VerificationCode(){
		
		this.width=60;
		this.height=25;
		this.codeNum=4;
		this.fontHeight=20;
		this.fontWidth=11;
		this.random=new Random();
		
	}
	
	public VerificationCode(int width, int height, int codeNum, int fontHeight,int fontWidth) {
		this.width = width;
		this.height = height;
		this.codeNum = codeNum;
		this.fontHeight = fontHeight;
		this.fontWidth = fontWidth;
		this.random=new Random();
	}

	/**
	 * 	创建图片验证码，以流的形式通过response传递到浏览器
	 * @param response
	 * @return	产生的随机验证码字符串，用于保存在服务端session中
	 * @throws IOException
	 */
	public String getImgCode(HttpServletResponse response) throws IOException{
		
		//创建图片缓存
		BufferedImage bImg=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//获取绘图类（画笔）
		Graphics2D g=bImg.createGraphics();
		//设置图片背景，及画出背景矩形
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		//创建字体，字体的大小根据图片大小来定
		Font font=new Font("Fixedsys", Font.PLAIN, fontHeight);
		//设置字体
		g.setFont(font);
		
		//画边框 
		//g.setColor(Color.BLACK); 
		//g.drawRect(0, 0, width, height); 
		
		//随机产生16条干扰线，使图像中的认证码不易被其他程序探测到 
		for(int i=0;i<16;i++){
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			int x=random.nextInt(width); 
			int y=random.nextInt(height); 
			int x1=random.nextInt(12); 
			int y1=random.nextInt(12); 
			g.drawLine(x, y, x+x1, y+y1); 
		} 
		
		StringBuffer randomCode=new StringBuffer();
		
		//随机产生codeCount数字的验证码 
		for(int i=0;i<codeNum;i++){ 
			
			//得到随机产生的验证码数字 (0-36个字符序列不包含36)
			String strRand=String.valueOf(codeSequence[random.nextInt(36)]); 
			
			//产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同 
			//用随机产生的颜色将验证码绘制到图像中 
			g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); 
			g.drawString(strRand, (i+1)*fontWidth-fontWidth/2,height-(height-fontHeight)/2); 
			
			//将产生的四个随机数组合在一起。 
			randomCode.append(strRand); 
		}
		
		this.code=randomCode.toString();
		
		//禁止图像缓存 
		response.setHeader("Paragma", "no-cache"); 
		response.setHeader("Cache-Control", "no-cache"); 
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		ImageIO.write(bImg, "jpeg", response.getOutputStream());
		response.getOutputStream().close();
		
		return code;
	}
	
	
}
















