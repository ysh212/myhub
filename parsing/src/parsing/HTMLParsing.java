package parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParsing {

	public static void main(String[] args) {
		// 다운로드 처럼 오래 걸릴 가능성이 있는 작업은 스레드를 이용하는 것이 좋습니다.
		// 스레드를 사용하면 다운로드 받는 도중에 다른 작업을 할 수 있습니다.
		Thread th = new Thread() {
			public void run() {
				try {
					// 다운로드 받을 주소 만들기
					String addr = "https://sbgrid.co.kr/document/guide";
					URL url = new URL(addr);
					// 연결
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					// 옵션 설정
					con.setConnectTimeout(30000);
					con.setUseCaches(false);

					// 다운로드 받을 준비
					StringBuilder sb = new StringBuilder();
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while (true) {
						String line = br.readLine();
						if (line == null)
							break;
						sb.append(line + "\n");
					}
					br.close();
					con.disconnect();
					String html = sb.toString();
					// System.out.println(html);

					// 문자열을 메모리에 펼치기
					Document doc = Jsoup.parse(html);
					// 태그를 이용해서 데이터 가져오기
					// 태그는 동일한 데이터가 여러 개 있을 수 있으므로 배열 형태로 리턴됩니다.
					// Elements elements = doc.getElementsByTag("img");

					// 클래스로 찾는 경우에도 배열로 리턴
					// Elements elements = doc.getElementsByClass("logobox");

					// id는 중복될 수 없기 때문에 1개로 리턴
					// Element elements = doc.getElementById("imgMenu");

					// 클래스가 more인 데이터 전부 찾아오기
					Elements elements = doc.getElementsByClass("more");
					for (int i =0; i < elements.size(); i++){
						Element ele = elements.get(i);
						Elements contents = ele.getElementsByTag("a");
						
						for(int j =0; j < contents.size(); j++){
							System.out.println(contents.get(j).text());
							
						}
						System.out.println("======================================");
						
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}

			}
		};
		th.start();
	}

}
