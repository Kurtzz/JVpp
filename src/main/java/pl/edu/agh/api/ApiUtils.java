package pl.edu.agh.api;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class ApiUtils {
    public static String formatReply(Object object) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder(object.getClass().getSimpleName()).append(":\n");
        List<Field> fields = Arrays.asList(object.getClass().getFields());
        for (Field field : fields) {
            builder.append("\t").append(field.getName()).append("=");
            Object fieldData = field.get(object);
            if (fieldData instanceof Number) {
                builder.append(fieldData);
            } else if (field.getName().equals("l2Address")){
                builder.append(decodeMAC((byte[]) fieldData));
            } else if (field.getName().equals("address")){
                builder.append(byteArrayToIpString((byte[]) fieldData));
            } else if (field.getType() == byte[].class){
                builder.append(new String((byte[]) fieldData).trim());
            } else if (field.getType().isArray()){
                for (Object objecti : (Object[]) fieldData) {
                    builder.append(objecti.toString());
                }
            } else {
                builder.append(field.toString());
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String byteArrayToIpString(byte[] bytes) {
        try {
            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    private static String decodeMAC(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
            result.append(":"); // delimiter
        }
        return result.toString();
    }
}
