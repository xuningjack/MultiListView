package multilistview.sortlist.vo;

import java.io.Serializable;

/**
 * SideBar中使用的实体类
 * @author Jack
 * @version 创建时间：2014-10-29 上午9:43:30
 */
public class BaseFilterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mId;
	private String mName;
	private String sortLetter; // 显示数据拼音的首字母

	public BaseFilterEntity(String mId, String mName) {
		this.mId = mId;
		this.mName = mName;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getSortLetter() {
		return sortLetter;
	}

	public void setSortLetter(String sortLetter) {
		this.sortLetter = sortLetter;
	}

	public BaseFilterEntity() {
		super();
	}

}
