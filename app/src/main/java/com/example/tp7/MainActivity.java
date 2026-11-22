import android.graphics.Color;
import android.text.Editable;
import android.text.style.BackgroundColorSpan;

import java.text.BreakIterator;

private void openFile() {
    String name = fileNameTxt.getText().toString().trim();
    if (name.isEmpty()) {
        fileNameTxt.setError("File name is required");
        return;
    }
    try (FileInputStream fis = openFileInput(name);
         BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while ((line = reader.readLine()) != null) {
            if (!first) sb.append("\n");
            sb.append(line);
            first = false;
        }
        noteTxt.setText(sb.toString());
        rememberFileName(name);
        Toast.makeText(this, "File opened", Toast.LENGTH_SHORT).show();
    } catch (FileNotFoundException e) {
        Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        Toast.makeText(this, "Error while reading", Toast.LENGTH_SHORT).show();
    }
}

private void searchWord() {
    String word = searchTxt.getText().toString().trim();
    Editable text = noteTxt.getText();

    BackgroundColorSpan[] old = text.getSpans(0, text.length(), BackgroundColorSpan.class);
    for (BackgroundColorSpan span : old) {
        text.removeSpan(span);
    }

    BreakIterator occurrencesTxt = null;
    if (word.isEmpty()) {
        searchTxt.setError("Enter a word to search");
        occurrencesTxt.setText("");
        return;
    }

    String content = text.toString().toLowerCase();
    String target = word.toLowerCase();
    int count = 0;
    int index = content.indexOf(target);
    while (index >= 0) {
        text.setSpan(new BackgroundColorSpan(Color.YELLOW),
                index, index + target.length(), Editable.SPAN_EXCLUSIVE_EXCLUSIVE);
        count++;
        index = content.indexOf(target, index + target.length());
    }
    occurrencesTxt.setText("Occurrences of \"" + word + "\": " + count);
}
}