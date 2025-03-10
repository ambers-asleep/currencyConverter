package org.example.currencyconverter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class HelloController {
    @FXML ImageView currencySymbolsImageView;
    @FXML ImageView usFlagImageView;
    @FXML
    ImageView countryFlagImageView;
    @FXML
    ChoiceBox<String> countryChoiceBox;
    @FXML Label usdToConvertLabel;
    @FXML Label otherCountryLabel;
    @FXML
    TextField currencyEnteredTextField;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void initialize() {

        //CHOICE BOX SET UP
        countryChoiceBox.getItems().addAll("British Pound (GBP)", "Chinese Yuan (CNY)", "European Euro (EUR)", "Japanese Yen (JPY)", "Mexican Peso (MXN)", "Russian Ruble (RUB)", "Australian Dollar (AUD)", "Canadian Dollar (CAD)", "Swiss Franc (CHF)", "Indian Rupee (INR)");
        countryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String country){
                String apiCallString = "https://open.er-api.com/v6/latest/usd";
                String jsonString;

                Double userEnteredNumber = Double.parseDouble(currencyEnteredTextField.getText());

                country = countryChoiceBox.getSelectionModel().getSelectedItem().toLowerCase();
                String countryAbreviated = country.substring(country.length() - 4, country.length() - 1);
                URL apiURL = null;
                StringBuffer content = new StringBuffer();
                try{
                    apiURL = new URL(apiCallString);
                    HttpURLConnection httpConnection = (HttpURLConnection)apiURL.openConnection();
                    httpConnection.setRequestMethod("GET");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    while((jsonString = bufferedReader.readLine()) != null){
                        content.append(jsonString);
                    }
                    bufferedReader.close();
                    httpConnection.disconnect();
                    System.out.println("Test Success 2");
                    System.out.println(content.toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //asigning json objects to in app objects
                JSONObject countryObject = new JSONObject();
                JSONObject currencyObject = countryObject.getJSONObject("rates");
                Double otherCurrency = currencyObject.getDouble(countryAbreviated.toUpperCase());

                System.out.println("Test Success 3");

                //IMAGE FOR CURRENCY SYMBOLS
                Image countryFlag = new Image(getClass().getResourceAsStream("/images/"+ countryAbreviated +".png"));
                countryFlagImageView.setImage(countryFlag);

                //user interacts with text field
                double finalDouble = userEnteredNumber * otherCurrency;
                otherCountryLabel.setText(df.format(finalDouble) + "\n" + country.substring(0, country.length() - 5).toUpperCase());
                usdToConvertLabel.setText(String.valueOf(userEnteredNumber) + "\n USD");

            }
        });
        Image currencySymbols = new Image(getClass().getResourceAsStream("/images/currency_symbols.png"));
        currencySymbolsImageView.setImage(currencySymbols);
        Image usFlag = new Image(getClass().getResourceAsStream("/images/um.png"));
        usFlagImageView.setImage(usFlag);


    }

}
