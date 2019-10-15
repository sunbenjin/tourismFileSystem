package opensource.capinfo.entity;
/**
 * 要显示的初始预览内容
 * 您可以传递用于显示图像、文本或文件的最小HTML标记。
 * 如果设置为字符串，在没有分隔符的情况下，将在初始预览中显示单个文件。
 * 可以设置分隔符(如initialDelimiter中定义的那样)，
 * 以便在初始预览中显示多个文件。如果设置为数组，
 * 它将显示数组中的所有文件作为初始预览(对于多个文件上传场景非常有用)。
 * 
 * 
 */

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * // for image files
initialPreview: [
    "<img src='/images/desert.jpg' class='file-preview-image' alt='Desert' title='Desert'>",
    "<img src='/images/jellyfish.jpg' class='file-preview-image' alt='Jelly Fish' title='Jelly Fish'>",
],
 
// for text files
initialPreview: "<div class='file-preview-text' title='NOTES.txt'>" +
    "This is the sample text file content upto wrapTextLength of 250 characters" +
    "<span class='wrap-indicator' onclick='$(\"#show-detailed-text\").modal(\"show\")' title='NOTES.txt'>[…]</span>" +
    "</div>"
 
// for other files
initialPreview: "<div class='file-preview-text'>" +
    "<h3><i class='glyphicon glyphicon-file'></i></h3>" +
    "Filename.xlsx" + "</div>"
    
    官方给的三种例子
 */

@Data
@AllArgsConstructor
@Builder
public class InitialPreview {

	private String data;

	@Override
	public String toString() {
		return data;
	}

	public static InitialPreview.InitialPreviewBuilder builder() {
		return new InitialPreview.InitialPreviewBuilder();
	}

	public static class InitialPreviewBuilder {
		/**
		 * 有三种情况
		 * 1.image  file-preview-image
		 * 2.text   file-preview-text
		 * 3.other  file-preview-other
		 *
		 */
		private String type;

		private String src;
		/**
		 * 可以自己设置 也可以默认
		 */
		private String clazz;
		/**
		 * alt
		 */
		private String alt;
		/**
		 * 标题
		 */
		private String title;
		/**
		 * 文件名
		 */
		private String summaryContent;

		InitialPreviewBuilder(){

		}

		public InitialPreview.InitialPreviewBuilder src(String src){
			this.src = src;
			return this;
		}

		public InitialPreview.InitialPreviewBuilder clazz(String clazz){
			this.clazz = clazz;
			return this;
		}

		public InitialPreview.InitialPreviewBuilder alt(String alt){
			this.alt = alt;
			return this;
		}

		public InitialPreview.InitialPreviewBuilder title(String title){
			this.alt = alt;
			return this;
		}
		public InitialPreview.InitialPreviewBuilder summaryContent(String summaryContent){

			this.summaryContent = summaryContent;


			return this;
		}

		public InitialPreview.InitialPreviewBuilder summaryContent(File fileContent){
			try {
				String temp = FileUtils.readFileToString(fileContent,"UTF-8");
				if(StringUtils.isNotBlank(temp)){
					String content = StringUtils.substring(temp, 0,500);
					if(temp.length()>500){
						content += "...";
					}
					this.summaryContent =content;
				}else {
					this.summaryContent = "";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return this;
		}

		public InitialPreview buildToImg() {
			StringBuffer sb = new StringBuffer();
			sb.append("<img src='"+src+"' class='"+PreviewType.IMG.getClazz()+"' alt='"+alt+"' title='"+title+"'>");
			return new InitialPreview(sb.toString());
		}
		public InitialPreview buildToText() {
			String temp = "<div class='"+PreviewType.TEXT.getClazz()+"' title='"+title+"'>" +summaryContent+
					"<span class='wrap-indicator' onclick='$(\"#show-detailed-text\").modal(\"show\")' " +
					"title='"+title+"'>[…]</span>" +
					"</div>";
			return new InitialPreview(temp);
		}
		public InitialPreview buildToOther() {
			String temp ="<div class='"+PreviewType.OTHER.getClazz()+"'>" +
					"<h3><i class='glyphicon glyphicon-file'></i></h3>" +
					title + "</div>";
			return new InitialPreview(temp);
		}

	}

}
