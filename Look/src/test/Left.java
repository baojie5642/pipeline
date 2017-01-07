package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;

public class Left {
	/**
	 * 把字节流写入文件
	 * 
	 * @param filePath
	 * @param bytes
	 */
	public static void write(final String filePath, final byte[] bytes) {
		File file = null;
		File fileDirectory = null;
		file = new File(filePath);
		fileDirectory = file.getParentFile();
		buildParentFile(fileDirectory);
		buildFile(file);
		writeBytes(file, bytes);
	}

	private static void buildParentFile(final File fileDirectory) {
		if (fileDirectory.exists()) {
			throw new IllegalArgumentException("fileDirectory is already exist.");
		} else {
			fileDirectory.setWritable(true);
			fileDirectory.mkdirs();// 包不存在，创建包
		}
	}

	private static void buildFile(final File file) {
		if (file.exists()) {
			throw new IllegalArgumentException("fileDirectory is already exist.");
		} else {
			innerCreateFile(file);
		}
	}

	private static void innerCreateFile(final File file) {
		file.setWritable(true);
		try {
			file.createNewFile();// 文件不存在，创建文件
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeBytes(final File file, final byte[] bytes) {
		FileOutputStream outputStream = createOutputStream(file);
		try {
			outputStream.write(bytes);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStream(outputStream);
		}

	}

	private static FileOutputStream createOutputStream(final File file) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (null == outputStream) {
			throw new NullPointerException("outputStream must not be null.");
		}
		return outputStream;
	}

	private static void closeStream(FileOutputStream fileOutputStream) {
		if (null != fileOutputStream) {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				fileOutputStream = null;
			}
		}else {
			return;
		}
	}

	public static Queue<File> getFileQueue(final File dir, final Queue<File> queue) {
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileQueue(files[i], queue); // 获取文件绝对路径
				} else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) { // 判断文件名是否是excel文件
					queue.add(files[i]);
				} else {
					continue;
				}
			}
		}
		return queue;
	}
}
