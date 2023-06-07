package com.soeguet.gui.translate;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class TranslateDialogImpl extends TranslateDialog {

  public TranslateDialogImpl(Window owner, String selectedText) {
    super(owner);
    form_selectedText.setText(selectedText);
  }

  @Override
  protected void translateButtonMouseClicked(MouseEvent e) {

    try {

      String textToTranslate = form_selectedText.getText();
      String sourceLanguage =
          Objects.requireNonNull(form_fromComboBox.getSelectedItem()).toString();
      String targetLanguage = Objects.requireNonNull(form_toComboBox.getSelectedItem()).toString();

      String url =
          "https://api.mymemory.translated.net/get?q="
              + URLEncoder.encode(textToTranslate, "UTF-8")
              + "&langpair="
              + sourceLanguage
              + "|"
              + targetLanguage;
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");

      int responseCode = con.getResponseCode();
      StringBuilder response;
      try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
        String inputLine;
        response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }

      log.info("responseCode = " + responseCode);

      JSONObject jsonObject = new JSONObject(response.toString());
      JSONObject responseData = jsonObject.getJSONObject("responseData");
      String translatedText = responseData.getString("translatedText");
      double match = responseData.getDouble("match") * 100;

      log.info("Translated text: " + translatedText);
      log.info("Match: " + match);

      form_translatedText.setText("");
      form_translatedText.append("match: \n" + match + " %" + "\n\n");
      form_translatedText.append("translated text:\n" + translatedText);

    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
