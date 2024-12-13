package ro.tucn.energy_mgmt_chat.config.WebSocket;

public class wsUtils {
    /**
     * Utility method to extract a query parameter from the WebSocket URI.
     */
    static protected String extractQueryParam(String query, String key) {
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }
}
