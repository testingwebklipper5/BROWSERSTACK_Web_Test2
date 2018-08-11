package TESTCASES;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class automation {

	private static final String FILENAME = "/Users/apple/Downloads/browsers.json";

	public static String return_content_out_file() {
		String sCurrentLine = null;
		String api_content = "";
		BufferedReader br = null;
		FileReader fr = null;

		try {

			// br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				api_content = api_content + sCurrentLine;

			}
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return api_content;

	}
}
