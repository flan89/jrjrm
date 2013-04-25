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
 * �������������֤��
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
	 * 	����ͼƬ��֤�룬��������ʽͨ��response���ݵ������
	 * @param response
	 * @return	�����������֤���ַ��������ڱ����ڷ����session��
	 * @throws IOException
	 */
	public String getImgCode(HttpServletResponse response) throws IOException{
		
		//����ͼƬ����
		BufferedImage bImg=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//��ȡ��ͼ�ࣨ���ʣ�
		Graphics2D g=bImg.createGraphics();
		//����ͼƬ��������������������
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		//�������壬����Ĵ�С����ͼƬ��С����
		Font font=new Font("Fixedsys", Font.PLAIN, fontHeight);
		//��������
		g.setFont(font);
		
		//���߿� 
		//g.setColor(Color.BLACK); 
		//g.drawRect(0, 0, width, height); 
		
		//�������16�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽ 
		for(int i=0;i<16;i++){
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			int x=random.nextInt(width); 
			int y=random.nextInt(height); 
			int x1=random.nextInt(12); 
			int y1=random.nextInt(12); 
			g.drawLine(x, y, x+x1, y+y1); 
		} 
		
		StringBuffer randomCode=new StringBuffer();
		
		//�������codeCount���ֵ���֤�� 
		for(int i=0;i<codeNum;i++){ 
			
			//�õ������������֤������ (0-36���ַ����в�����36)
			String strRand=String.valueOf(codeSequence[random.nextInt(36)]); 
			
			//�����������ɫ������������ɫֵ�����������ÿλ���ֵ���ɫֵ������ͬ 
			//�������������ɫ����֤����Ƶ�ͼ���� 
			g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); 
			g.drawString(strRand, (i+1)*fontWidth-fontWidth/2,height-(height-fontHeight)/2); 
			
			//���������ĸ�����������һ�� 
			randomCode.append(strRand); 
		}
		
		this.code=randomCode.toString();
		
		//��ֹͼ�񻺴� 
		response.setHeader("Paragma", "no-cache"); 
		response.setHeader("Cache-Control", "no-cache"); 
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		ImageIO.write(bImg, "jpeg", response.getOutputStream());
		response.getOutputStream().close();
		
		return code;
	}
	
	
}
















