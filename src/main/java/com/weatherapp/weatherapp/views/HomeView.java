package com.weatherapp.weatherapp.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;
import com.weatherapp.weatherapp.controller.WeatherService;
import org.json.JSONException;
import org.json.JSONObject;

@Route("")
@CssImport("./styles/styles.css")
public class HomeView extends VerticalLayout {

    private WeatherService weatherService;
    private VerticalLayout mainL;
    private TextField cityField;
    private ComboBox<String> tempUnitDropdown;
    private Button searchButton;
    private HorizontalLayout cityDashboard, currentTempLayout, minTempLayout, maxTempLayout,
             humidityLayout, windLayout, commentLayout;
    private String currentTemperature, minTemperature, maxTemperature,
            humidity, wind;
    private VerticalLayout dashboard;

    public HomeView(WeatherService weatherService) {
        this.weatherService = weatherService;
        mainLayout();
        setHeader();
        addCityAndTemperatureSelection();
        add(mainL);
    }

    private void updateUI() {
        String city = cityField.getValue();
        String units = tempUnitDropdown.getValue();

        if (dashboard == null) {
            dashboard = new VerticalLayout();
            dashboard.setWidth("100%");
            dashboard.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            mainL.add(dashboard);
        } else {
            dashboard.removeAll(); // Clear existing dashboard items
        }

        weatherService.setCityName(city);
        if (units.equals("Fahrenheit")) {
            weatherService.setUnit("imperial");
        } else if (units.equals("Celsius")) {
            weatherService.setUnit("metric");
        }

        try {
            String weatherString = weatherService.getWeather().toString();
            JSONObject mainJsonArray = weatherService.getMainJsonArray();
            JSONObject windJsonObject = weatherService.getWindJsonArray();

            if (mainJsonArray == null || windJsonObject == null) {
                throw new JSONException("Failed to retrieve weather data");
            }

            currentTemperature = String.valueOf(mainJsonArray.getInt("temp"));
            minTemperature = String.valueOf(mainJsonArray.getInt("temp_min"));
            maxTemperature = String.valueOf(mainJsonArray.getInt("temp_max"));
            humidity = String.valueOf(mainJsonArray.getInt("humidity"));
            wind = String.valueOf(windJsonObject.getDouble("speed")) + " miles/hour";

            cityDashboard = createDashboardItem("Currently in", city, VaadinIcon.BUS);
            currentTempLayout = createDashboardItem("Current Temp", currentTemperature + "° " + units, VaadinIcon.ANGLE_DOUBLE_RIGHT);
            minTempLayout = createDashboardItem("Min Temp", minTemperature + "° " + units, VaadinIcon.ARROW_DOWN);
            maxTempLayout = createDashboardItem("Max Temp", maxTemperature + "° " + units, VaadinIcon.ARROW_UP);
            humidityLayout = createDashboardItem("Humidity", humidity + "%", VaadinIcon.CLOUD);
            windLayout = createDashboardItem("Wind", wind, VaadinIcon.BOAT);

            String comment = weatherService.generate(weatherString);
            commentLayout = createCommentLayout("AI Weather Analysis:", comment);

            dashboard.add(commentLayout, cityDashboard, currentTempLayout, minTempLayout, maxTempLayout, humidityLayout, windLayout);
            mainL.add(dashboard);
        } catch (JSONException e) {
            e.printStackTrace();
            Notification.show("Please enter the city name (Include spaces for citiy name with multiple words).", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Please enter the city name (Include spaces for citiy name with multiple words).", 3000, Notification.Position.MIDDLE);
        }
    }

    private void mainLayout() {
        mainL = new VerticalLayout();
        mainL.setWidth("100%");
        mainL.setSpacing(true);
        mainL.setMargin(true);
        mainL.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(mainL);
        System.out.println("Main layout configured.");
    }

    private void setHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        Image logo = new Image("cloudy-day.png", "Weather App Logo"); // Ensure the path is correct
        logo.setHeight("60px"); // Set height for the logo
        logo.setWidth("60px");  // Set width for the logo
        logo.getStyle().set("margin-right", "10px");

        NativeLabel label = new NativeLabel("☀️ Weather 🌦️");
        label.addClassName("gradient-text");

        header.add(label);
        mainL.add(logo);
        mainL.add(header);
    }

    private void addCityAndTemperatureSelection() {
        HorizontalLayout selectionLayout = new HorizontalLayout();
        selectionLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE); // Align components on the same baseline

        cityField = new TextField("Enter City");
        cityField.setPlaceholder("e.g., New York");
        cityField.setWidth("200px");

        tempUnitDropdown = new ComboBox<>("Temperature Unit");
        tempUnitDropdown.setItems("Celsius", "Fahrenheit");
        tempUnitDropdown.setValue("Celsius"); // Default value
        tempUnitDropdown.setWidth("150px");

        searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.addClickListener(event -> {
            if (!cityField.getValue().trim().isEmpty()) {
                try {
                    updateUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notification.show("Please enter the city name (include spaces for citiy name with multiple words)", 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Please enter the city name (include spaces for citiy name with multiple words)", 3000, Notification.Position.MIDDLE);
            }
        });

        selectionLayout.add(cityField, searchButton, tempUnitDropdown);
        mainL.add(selectionLayout);
    }

    private HorizontalLayout createDashboardItem(String label, String value, VaadinIcon icon) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        layout.setSpacing(true);

        Icon itemIcon = new Icon(icon);
        itemIcon.setSize("24px");
        itemIcon.getStyle().set("color", "#2c3e50"); // Dark blue color

        NativeLabel itemLabel = new NativeLabel(label + ": ");
        itemLabel.getStyle().set("font-size", "16px");
        itemLabel.getStyle().set("font-weight", "bold");
        itemLabel.getStyle().set("margin-right", "10px");

        NativeLabel itemValue = new NativeLabel(value);
        itemValue.getStyle().set("font-size", "16px");

        layout.add(itemIcon, itemLabel, itemValue);
        return layout;
    }

    private HorizontalLayout createCommentLayout(String label, String comment) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(Alignment.START);
        layout.setSpacing(true);

        try {
            // Check if comment or label is null or empty
            if (label == null || label.trim().isEmpty()) {
                //Notification.show("Label cannot be null or empty", 3000, Notification.Position.MIDDLE);
                return layout; // Return an empty layout in case of error
            }
            
            if (comment == null || comment.trim().isEmpty()) {
                //Notification.show("Comment cannot be null or empty", 3000, Notification.Position.MIDDLE);
                return layout; // Return an empty layout in case of error
            }

            Image aiIcon = new Image("ai-technology.png", "AI");
            aiIcon.setHeight("30px");
            aiIcon.setWidth("30px");

            NativeLabel commentLabel = new NativeLabel(label);
            commentLabel.getStyle().set("font-size", "16px");
            commentLabel.getStyle().set("font-weight", "bold");
            commentLabel.getStyle().set("margin-right", "10px");

            // Create a Div for the entire comment with wrapping text
            NativeLabel commentry = new NativeLabel(comment);
            Div commentDiv = new Div();
            commentDiv.add(commentry);

            // Apply CSS styling
            commentDiv.getStyle()
                    .set("white-space", "pre-wrap") // Preserve spaces and wrap text
                    .set("width", "100%")
                    .set("max-width", "400px")
                    .set("padding", "10px")
                    .set("border", "2px solid #153f70")
                    .set("border-radius", "10px")
                    .set("overflow", "hidden");

            layout.add(aiIcon, commentDiv);
            layout.setWidth("100%");
            layout.setAlignItems(Alignment.CENTER);

            layout.getStyle()
                    .set("margin", "0 auto")
                    .set("display", "flex")
                    .set("justify-content", "center");

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("An error occurred while creating the comment layout", 3000, Notification.Position.MIDDLE);
        }

        return layout;
    }
}