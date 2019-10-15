package opensource.capinfo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class ChangeVideo {

	private static String ffmpegpath;

	private static String mencoderpath;

	private static String avifilepath;


	public static void setFfmpegpath(String ffmpegpath) {
		ChangeVideo.ffmpegpath = ffmpegpath;
	}
	public static void setMencoderpath(String mencoderpath) {
		ChangeVideo.mencoderpath = mencoderpath;
	}
	public static void setAvifilepath(String avifilepath) {
		ChangeVideo.avifilepath = avifilepath;
	}

	/**
	 * @param inputFile:需要转换的视频
	 * @param outputFile：转换后的视频
	 * @return
	 */
	public static boolean convert(String inputFile, String outputFile) {
		if (!checkfile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}
		if (process(inputFile, outputFile)) {
			System.out.println("ok");
			return true;
		}
		return false;
	}

	// 检查文件是否存在
	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * @param inputFile
	 * @param outputFile
	 * @return
	 * 转换视频文件
	 */
	private static boolean process(String inputFile, String outputFile) {
		int type = checkContentType(inputFile);
		boolean status = false;
		if (type == 0) {
			status = processMP4(inputFile, outputFile);// 直接将文件转为flv文件
		}
		return status;
	}

	private static int checkContentType(String inputFile) {
		String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
				inputFile.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv，mp3等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp3")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		}else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		}

		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 不支持
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}
	// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）直接转换为目标视频
	private static boolean processMP4(String inputFile, String outputFile) {
		if (!checkfile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}
		List<String> commend = new ArrayList<String>();
		
		commend.add(ffmpegpath);
		commend.add("-i");
		commend.add(inputFile);
		commend.add("-acodec");//转音频
		commend.add("aac");
		commend.add("-vcodec"); //转视频
		commend.add("libx264");
		commend.add("-preset");
		commend.add("superfast");
		commend.add("-s");
		commend.add("320x240");
		commend.add(outputFile);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}

		System.out.println(test);

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process process = builder.start();
			doWaitFor(process);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int doWaitFor(Process p) {
		InputStream in = null;
		InputStream err = null;
		int exitValue = -1; // returned to caller when p is finished
		try {
			System.out.println("comeing");
			in = p.getInputStream();
			err = p.getErrorStream();
			boolean finished = false; // Set to true when p is finished

			while (!finished) {
				try {
					while (in.available() > 0) {
						Character c = new Character((char) in.read());
						System.out.print(c);
					}
					while (err.available() > 0) {
						Character c = new Character((char) err.read());
						System.out.print(c);
					}

					exitValue = p.exitValue();
					finished = true;

				} catch (IllegalThreadStateException e) {
					Thread.currentThread().sleep(500);
				}
			}
		} catch (Exception e) {
			System.err.println("doWaitFor();: unexpected exception - "
					+ e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			if (err != null) {
				try {
					err.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return exitValue;
	}


	
	// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
	// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
	private static String processAVI(int type, String inputFile) {
		File file = new File(avifilepath);
		if (file.exists())
			file.delete();
		List<String> commend = new ArrayList<String>();
		commend.add(mencoderpath);
		commend.add(inputFile);
		commend.add("-oac");
		commend.add("mp3lame");
		commend.add("-lameopts");
		commend.add("preset=64");
		commend.add("-ovc");
		commend.add("xvid");
		commend.add("-xvidencopts");
		commend.add("bitrate=600");
		commend.add("-of");
		commend.add("avi");
		commend.add("-o");
		commend.add(avifilepath);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start();

			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null)
								System.out.println(lineB);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null)
								System.out.println(lineC);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			// 等Mencoder进程转换结束，再调用ffmepg进程
			p.waitFor();
			//System.out.println("who cares");
			return avifilepath;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

}
