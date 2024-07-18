package a2_2001040208.common;

import javax.swing.text.MaskFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Helper {
    private static SimpleDateFormat DATE_FORMAT = null;
    private static MaskFormatter DATE_FORMATTER = null;

    public static SimpleDateFormat getDateFormatInstance() {
        if (DATE_FORMAT == null) {
            DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
            DATE_FORMAT.setLenient(false);
        }
        return DATE_FORMAT;
    }

    public static MaskFormatter getDateFormatterInstance() {
        if (DATE_FORMATTER == null) {
            try {
                DATE_FORMATTER = new MaskFormatter("##/##/####");
                DATE_FORMATTER.setPlaceholderCharacter('_');
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return DATE_FORMATTER;
    }

    public static class NumberFilter extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            if (!Character.isDigit(e.getKeyChar())) {
                e.consume();
            }
        }
    }
}
