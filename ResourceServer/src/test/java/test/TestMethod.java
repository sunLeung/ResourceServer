package test;

public class TestMethod {

	public TestMethod() {
		try {
			SiteInfoBean bean = new SiteInfoBean(
					"http://127.0.0.1:8080/ResourceServer/test", "D:/",
					"weblogic60b2_win", 5);
			// SiteInfoBean bean = new
			// SiteInfoBean(" http://localhost:8080/down.zip";;,"L:	emp","weblogic60b2_win.exe",5);
			SiteFileFetch fileFetch = new SiteFileFetch(bean);
			fileFetch.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestMethod();
	}
}
