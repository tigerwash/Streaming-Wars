package streaming.gui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class StdoutToTextAreaStream extends OutputStream {
    private JTextArea textArea;

    public StdoutToTextAreaStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int contents) throws IOException {
        textArea.append(String.valueOf((char) contents));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
