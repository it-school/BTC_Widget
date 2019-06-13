package com.itschool.btc_widget;

import java.net.URL;
import java.util.Calendar;

class HTTPRequestThread extends Thread {
    private static final String urlString = "https://btc-e.nz/api/3/ticker/btc_usd";

    String getInfoString() {
        return output;
    }

    private String output = "";

    private void requestPrice() {

        try {
            URL url = new URL(urlString);
            // HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // con.setRequestMethod("GET");
            //BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuilder response = new StringBuilder();
            response.append("{\"btc_usd\":{\"high\":3988.00,\"low\":3956.99,\"avg\":3980.00,\"vol\":3774491.17766,\"vol_cur\":4368.01172,\"last\":3981.60,\"buy\":3871,\"sell\":3981.701,\"updated\":1482754417}}");
 /*
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
*/
            output = "Price: " + JSONParser.getPrice(response.toString()) + "\n" + getTimeStamp();

        } catch (Exception e) {
            output = e.toString();
        }
    }

    @Override
    public void run() {
        requestPrice();
    }

    private String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.MINUTE)>9) {

            return "Time: " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE);
        } else {
            return "Time: " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":0" + calendar.get(Calendar.MINUTE);
        }

    }
}
