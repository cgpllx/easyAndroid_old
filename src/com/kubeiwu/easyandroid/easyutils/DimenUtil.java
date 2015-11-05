package com.kubeiwu.easyandroid.easyutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class DimenUtil {
	private static String PATH_BASE = "K:/ccc/BevaErgeTV_V2/";

	/** path_large */
	private static String path_large = PATH_BASE + "res/values-large/";
	/** path_xlarge */
	private static String path_xlarge = PATH_BASE + "res/values-xlarge/";
	/** path_sw540dp */
	private static String path_sw540dp = PATH_BASE + "res/values-sw540dp/";
	/** path_sw600dp */
	private static String path_sw600dp = PATH_BASE + "res/values-sw600dp/";
	/** path_sw672dp */
	private static String path_sw672dp = PATH_BASE + "res/values-sw672dp/";
	/** path_sw720dp */
	private static String path_sw720dp = PATH_BASE + "res/values-sw720dp/";
	/** path_sw1080dp */
	private static String path_sw1080dp = PATH_BASE + "res/values-sw1080dp/";

	/** values_large scaled value */
	private static float scale_values_large = 0.75f;
	/** values_xlarge scaled value */
	private static float scale_values_xlarge = 1.00f;// base case.
	/** values_xlarge scaled value */
	private static float scale_values_xxlarge = 1.50f;
	/** values_large scaled value */
	private static float scale_values_sw540dp = 0.75f;
	/** values_sw600dp scaled value */
	private static float scale_values_sw600dp = 1.0f;// exception.
	/** values_sw672dp scaled value */
	private static float scale_values_sw672dp = 0.9f;
	/** values_xlarge scaled value */
	private static float scale_values_sw720dp = 1.00f;// base case.
	/** values_sw1080dp scaled value */
	private static float scale_values_sw1080dp = 1.50f;

	public static void main(String[] args) {
		File src = new File(
				"D:/Users/SilentKnight//res/values-sw1080dp/dimen.xml");
		File target720 = new File(
				"D:/Users/SilentKnight/res/values-sw720dp/dimen.xml");
		File target540 = new File(
				"D:/Users/SilentKnight/res/values-sw540dp/dimen.xml");
		File targetLarge = new File(
				"D:/Users/SilentKnight/res/values-large/dimen.xml");
		File targetXLarge = new File(
				"D:/Users/SilentKnight/res/values-xlarge/dimen.xml");
		File targetXXLarge = new File(
				"D:/Users/SilentKnight/res/values-xxlarge/dimen.xml");
		// 720
		String tmp = convertStreamToString(src.getAbsolutePath(), (float) 2 / 3);
		writeFile(target720.getAbsolutePath(), tmp);
		writeFile(targetXLarge.getAbsolutePath(), tmp);
		// 540
		tmp = convertStreamToString(src.getAbsolutePath(),
				(float) 2 / 3 * 0.75f);
		writeFile(target540.getAbsolutePath(), tmp);
		writeFile(targetLarge.getAbsolutePath(), tmp);
		// 1080
		tmp = convertStreamToString(src.getAbsolutePath(), 1.0f);
		writeFile(targetXXLarge.getAbsolutePath(), tmp);
//		File file = new File(path_sw720dp);
//		File[] files = file.listFiles();
//		String temp = "";
//		for (File file2 : files) {
//			// write 540
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_sw540dp);
//			writeFile(path_sw540dp + file2.getName(), temp);
//			// write 600
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_sw600dp);
//			writeFile(path_sw600dp + file2.getName(), temp);
//			// write 672
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_sw672dp);
//			writeFile(path_sw672dp + file2.getName(), temp);
//			// write large
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_large);
//			writeFile(path_large + file2.getName(), temp);

			// write xlarge
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_sw720dp);
//			writeFile(path_xlarge + file2.getName(), temp);

//			// write 1080
//			temp = convertStreamToString(file2.getAbsolutePath(),
//					scale_values_sw1080dp);
//			writeFile(path_sw1080dp + file2.getName(), temp);
//		}
	}

	/**
	 * @Title: convertStreamToString
	 * @Description: read file and then convert it to String
	 * @param @param filepath
	 * @param @param f
	 * @param @return
	 * @return String
	 */
	public static String convertStreamToString(String filepath, float f) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdir();
			}
			BufferedReader bf = new BufferedReader(new FileReader(filepath));
			String line = null;
			String endMarkSP = "sp</dimen>";
			String endMarkDP = "dp</dimen>";
			String endMark = "</string>";
			String startmark = ">";
			while ((line = bf.readLine()) != null) {
				if (line.contains(endMarkSP) || line.contains(endMarkDP)
						|| line.contains(endMark)) {
					int end = -1;
					if (line.contains(endMarkSP)) {
						end = line.lastIndexOf(endMarkSP);
					} else if (line.contains(endMarkDP)) {
						end = line.lastIndexOf(endMarkDP);
					} else if (line.contains(endMark)) {
						end = line.lastIndexOf(endMark);
					}

					int start = line.indexOf(startmark);
					String temp = line.substring(start + 1, end);
					double tempValue = Double.parseDouble(temp);
					double newValue = (tempValue * f);
					BigDecimal bigDecimal = new BigDecimal(newValue);
					newValue = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					String newValueStr = subZeroAndDot(String.valueOf(newValue));
					String newline = "";
					if (line.contains(endMarkSP)) {
						newline = line.replace(temp + "sp", newValueStr + "sp");
					} else if (line.contains(endMarkDP)) {
						newline = line.replace(temp + "dp", newValueStr + "dp");
					} else if (line.contains(endMark)) {
						newline = line.replace(temp, newValueStr);
					}

					sb.append(newline + "\r\n");
				} else {
					sb.append(line + "\r\n");
				}
			}
			System.out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * @Title: DeleteFolder
	 * @Description: delete directory of the alignment
	 * @param @param sPath
	 * @param @return
	 * @return boolean
	 */
	public static boolean DeleteFolder(String sPath) {
		File file = new File(sPath);
		if (!file.exists()) {
			return true;
		} else {
			if (file.isFile()) {
				return deleteFile(sPath);
			} else {
				// return deleteDirectory(sPath);
			}
		}
		return false;
	}

	/**
	 * @Title: subZeroAndDot
	 * @Description: delete Excess 0 and .
	 * @param @param s
	 * @param @return
	 * @return String
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// delete Excess 0
			s = s.replaceAll("[.]$", "");// delete . at the last index if any
		}
		return s;
	}

	/**
	 * @Title: writeFile
	 * @Description: write String into new file
	 * @param @param filepath
	 * @param @param st
	 * @return void
	 */
	public static void writeFile(String filepath, String st) {
		try {
			File file = new File(filepath);
			file = new File(file.getParent());
			if (!file.exists()) {
				file.mkdir();
			}
			FileWriter fw = new FileWriter(filepath);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(st);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: deleteFile
	 * @Description: delete file.
	 * @param @param sPath
	 * @param @return
	 * @return boolean
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
}
