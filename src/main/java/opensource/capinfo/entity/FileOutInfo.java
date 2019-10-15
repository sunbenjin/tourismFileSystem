package opensource.capinfo.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.springframework.util.CollectionUtils;

@Data
public class FileOutInfo implements FileOutUploader{
		/**
		 * 上传错误名字
		 */
		@Expose
		private String error;//错误名字

		private List<String> initialPreview = new ArrayList<String>();

		@Expose
		private List<InitialPreviewConfig> initialPreviewConfig = new ArrayList<InitialPreviewConfig>();
		/**
		 * append 接着搞  true false
		 */
		private boolean append;

		public void setInitialPreview(List<InitialPreview> data) {
			if(!CollectionUtils.isEmpty(data)){
				data.forEach(d->{
					initialPreview.add(d.toString());
				});
			}
		}
}
