/**   
 * @Title: Installation.java 
 * @Package me.pc.mobile.helper.v14.util 
 * @Description: TODO
 * @author SilentKnight || happychinapc[at]gmail[dot]com   
 * @date 2014 2014年12月30日 下午4:02:35 
 * @version V1.0.0   
 */
package com.kubeiwu.easyandroid.easyutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;

/**
 * @ClassName: AppInstallUtil
 * @Description: TODO
 * @author SilentKnight || happychinapc@gmail.com
 * @date 2014年12月30日 下午4:02:35
 * 
 */
public final class AppInstallUtil {
	private static String sID = null;
	private static final String INSTALLATION = "app_installation_identifier";

	public synchronized static String id(Context context) {
		if (sID == null) {
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists()) {
					writeInstallationFile(installation);
				}
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static void writeInstallationFile(File installation)
			throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}
}
