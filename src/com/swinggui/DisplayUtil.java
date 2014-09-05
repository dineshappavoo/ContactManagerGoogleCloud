
package com.swinggui;

/**
 * 
 */


import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * @author Dany
 *
 */
public class DisplayUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static JLabel addInfoLabel(String message)
	{
	    final JLabel infoLabel;
	    StringBuilder sb = new StringBuilder();
        sb.append("<html>\"").append(message).append("\"</html>");
    	infoLabel = new JLabel(sb.toString());
    	infoLabel.setBounds(25, 30, 130, 300);
    	infoLabel.setForeground(Color.gray);
    	infoLabel.setFont(new Font("Verdana", Font.ITALIC, 10));
    	return infoLabel;

	}
}
